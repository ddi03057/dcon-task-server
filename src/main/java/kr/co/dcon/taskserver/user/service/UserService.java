package kr.co.dcon.taskserver.user.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.dcon.taskserver.auth.dto.UserDetailsDTO;
import kr.co.dcon.taskserver.auth.service.CurrentUserService;
import kr.co.dcon.taskserver.common.constants.CommonConstants;
import kr.co.dcon.taskserver.common.constants.UserOtherClaim;
import kr.co.dcon.taskserver.common.exception.UserAttributeException;
import kr.co.dcon.taskserver.common.util.DateUtils;
import kr.co.dcon.taskserver.common.util.KeycloakUtil;
import kr.co.dcon.taskserver.common.util.StringUtil;
import kr.co.dcon.taskserver.common.util.Utils;
import kr.co.dcon.taskserver.user.dto.UserChangeDTO;
import kr.co.dcon.taskserver.user.dto.UserChangePasswordDTO;
import kr.co.dcon.taskserver.user.dto.UserCreateDTO;
import kr.co.dcon.taskserver.user.dto.UserDTO;
import kr.co.dcon.taskserver.user.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang.ArrayUtils;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessToken;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

@Service
@Slf4j
//@AllArgsConstructor
public class UserService {

    @Autowired
    private CurrentUserService currentUserService;

    @Autowired
    private UserMapper userMapper;

    // @Autowired
    ResteasyClient resteasyClient;
    private static final String RESULT_STRING = "result";

    public static final Integer FIRST_INDEX = 0;
    public static final Integer MAX_RESULT = 10000000;

    public UserDTO selectCurrentUser() {

        AccessToken accessToken = KeycloakUtil.getAccessToken();

        String userName = KeycloakUtil.getAccessTokenAttribute(accessToken, UserOtherClaim.USER_NAME);
        String userLocale = KeycloakUtil.getAccessTokenAttribute(accessToken, UserOtherClaim.LOCALE);
        currentUserService.getCurrentUser().setUserName(userName);
        currentUserService.getCurrentUser().setLocale(userLocale);

        return new UserDTO(KeycloakUtil.getContext());
    }

    public UserDetailsDTO selectUserDetail(String userId) throws IOException {
        if (StringUtil.isEmpty(userId)) {
            throw new IllegalArgumentException("selectUserByUserId() userId can not be empty");
        }

        UserResource userResource = KeycloakUtil.getUserResource(userId);
        UserRepresentation user = userResource.toRepresentation();

        return KeycloakUtil.buildUserDetail(user);
    }



    public Map<String, String> updateUserPassword(UserChangePasswordDTO changePasswordmodel) {
        log.info("changePasswordmodel::{}", changePasswordmodel.toString());

        Map<String, String> resultMap = new HashMap<>();

        String userEmail = changePasswordmodel.getEmail();
        String userId = userMapper.selectKeyCloakUserId(userEmail);
        UserResource userResource = KeycloakUtil.getUserResource(userId);

        CredentialRepresentation credential = KeycloakUtil.setCredential(changePasswordmodel.getNewCredential());
        try {
            userResource.resetPassword(credential);

            UserRepresentation userRepresentation = userResource.toRepresentation();
            userRepresentation.getAttributes().put(UserOtherClaim.ERROR_CNT, Arrays.asList(CommonConstants.ZERO));
//            user.getAttributes().put(UserOtherClaim.PWD_INIT_YN,Arrays.asList(CommonConstants.YES));
//            user.getAttributes().put(UserOtherClaim.PWD_CHG_DT, Arrays.asList(String.valueOf(System.currentTimeMillis())));
//            user.getAttributes().put(UserOtherClaim.SKIP_PWD_CHG_DT, Arrays.asList(String.valueOf(System.currentTimeMillis())));
            userResource.update(userRepresentation);
            log.info("try::");
        } catch (BadRequestException e) {
            log.info("BadRequestException::");
            // credential setvalue시 전 암호와 같은 암호라면 400 에러 발생.
            resultMap.put(RESULT_STRING, CommonConstants.NO);
            return resultMap;
        } catch (Exception e) {
            log.info("Exception::{}", e.toString());
            resultMap.put(RESULT_STRING, "Server Error");
            return resultMap;
        }

        return resultMap;
    }


    @Transactional
    public Map<String, String> updateUser(UserChangeDTO useChg) {
        Map<String, String> resultMap = new HashMap<>();

        String userEmail = useChg.getUserEmail();
        String userId = userMapper.selectKeyCloakUserId(userEmail);

        try {
            log.info("updateUser3::");
            Map<String, String> param = new HashMap<>();
//                param.put(UserOtherClaim.USER_TEL_NO, Utils.nvl(useChg.getTelNo(),""));
//                param.put(UserOtherClaim.USER_MBL_TEL_CNTR_CD, useChg.getUserTelNoCtrCd());
//                param.put(UserOtherClaim.USER_REGIST_COMPANY_NAME, useChg.getRegistCompanyName());
            param.put(UserOtherClaim.LOCALE, useChg.getLocale());
            param.put("firstName", useChg.getFirstName());
            param.put("lastName", useChg.getLastName());
            param.put(UserOtherClaim.ERROR_CNT, "1");

            resultMap = updateKeyCloakUser(userId, param);
        } catch (NotFoundException notFoundException) {
            log.info("updateUser4::{}", notFoundException.getMessage());
        } catch (Exception e) {
            //  log.info("updateUser5::{}",e.getMessage());
            resultMap.put(RESULT_STRING, "Server Error");
        }
        resultMap.put(RESULT_STRING, CommonConstants.SUCCESS);
        return resultMap;
    }

    public Map<String, String> updateKeyCloakUser(String userId, Map<String, String> param) {

        Map<String, String> result = new HashedMap<>();

        UserResource userResource = KeycloakUtil.getUserResource(userId);
        UserRepresentation user = userResource.toRepresentation();

        if (param.containsKey("firstName") && param.get("firstName") != null) {
            user.setFirstName(param.get("firstName"));
        }
        if (param.containsKey("lastName") && param.get("lastName") != null) {
            user.setLastName(param.get("lastName"));
        }

        Map<String, List<String>> attributes = user.getAttributes();
        for (Map.Entry<String, String> entry : param.entrySet()) {
            if (Objects.equals("fistName", entry.getKey())
                    || Objects.equals("lastName", entry.getKey())) {
                continue;
            }

            attributes.put(entry.getKey(), Arrays.asList(entry.getValue()));
        }
        if (this.currentUserService.getCurrentUser() != null) {
            attributes.put(UserOtherClaim.UPDATED_ID, Arrays.asList(this.currentUserService.getCurrentUser().getUserId()));
        }
        attributes.put(UserOtherClaim.UPDATED, Arrays.asList(String.valueOf(System.currentTimeMillis())));
        try {
            userResource.update(user);
            result.put(RESULT_STRING, CommonConstants.YES);
        } catch (Exception e) {
            result.put(RESULT_STRING, CommonConstants.NO);
        }

        return result;
    }

    @Transactional
    public int createUser(UserCreateDTO user) {

        int insertResultCnt = 0;

        try {
            String password = Utils.getRandomString();
            insertResultCnt = insertKeycloakUserInfo(user, password);

        } catch (Exception e) {
        }
        return insertResultCnt;
    }

    public int insertKeycloakUserInfo(UserCreateDTO user, String password) {
        int cnt = 0;
        // 1-1. Configure Keycloak
        try {

            UsersResource usersResource = KeycloakUtil.getUsersResource();
            UserRepresentation createUser = new UserRepresentation();
            createUser = KeycloakUtil.setInsertUserRepresentation(createUser, user);
            CredentialRepresentation credential = KeycloakUtil.setCredential(password);
            createUser.setCredentials(Arrays.asList(credential));
            try {
                int userCount = userMapper.selectKeyCloakUserCount(user);
                log.info("userCount::{}", userCount);
                if (userCount > 0) {
                    cnt = userCount;
                } else {
                    usersResource.create(createUser);
                }

            } catch (Exception e) {
                cnt = 2;
            }
        } catch (Exception e) {
            cnt = 2;

        }
        return cnt;
    }

    public void withdraw(String userId) {
        UsersResource usersResource = KeycloakUtil.getUsersResource();
        usersResource.delete(userId);
    }
}
