package kr.co.dcon.taskserver.user.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.dcon.taskserver.auth.dto.UserDetailsDTO;
import kr.co.dcon.taskserver.auth.service.CurrentUserService;
import kr.co.dcon.taskserver.common.constants.CommonConstants;
import kr.co.dcon.taskserver.common.constants.ResultCode;
import kr.co.dcon.taskserver.common.constants.UserOtherClaim;
import kr.co.dcon.taskserver.common.exception.RuntimeExceptionBase;
import kr.co.dcon.taskserver.common.exception.UserAttributeException;
import kr.co.dcon.taskserver.common.util.StringUtil;
import kr.co.dcon.taskserver.common.util.Utils;
import kr.co.dcon.taskserver.user.dto.UserChangeDTO;
import kr.co.dcon.taskserver.user.dto.UserChangePasswordDTO;
import kr.co.dcon.taskserver.user.dto.UserCreateDTO;
import kr.co.dcon.taskserver.user.dto.UserDTO;
import kr.co.dcon.taskserver.user.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.map.HashedMap;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.OAuth2Constants;
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
import org.springframework.util.StringUtils;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import java.io.IOException;
import java.util.*;


@Service
@Slf4j
//@AllArgsConstructor
public class UserService implements UserServiceKeycloak {

    @Value("${keycloak.auth-server-url}")
    public String authServerUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.resource}")
    private String clientId;

    @Value("${dcon.keycloak.rest.clientSecret}")
    private String clientSecret;
    @Value("${dcon.keycloak.dconmaster.userid}")
    private String dconMasterUserId;
    @Autowired
    private CurrentUserService currentUserService;

    @Autowired
    private UserMapper userMapper;

    // @Autowired
    ResteasyClient resteasyClient;
    private static final String RESULT_STRING = "result";

    public static final Integer FIRST_INDEX = 0;
    public static final Integer MAX_RESULT = 10000000;

    private static final String[] attributeArr = {UserOtherClaim.USER_NAME, UserOtherClaim.LOCALE};
    private static final String[] ignoreProperties = {"userId", "userEmail", "email", "useYn", "firstName", "lastName"};


    public UserDTO selectCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) authentication;
        @SuppressWarnings("unchecked")
        KeycloakPrincipal<KeycloakSecurityContext> keycloakPrincipal = (KeycloakPrincipal<KeycloakSecurityContext>) token.getPrincipal();
        KeycloakSecurityContext context = keycloakPrincipal.getKeycloakSecurityContext();
        String nowToken = context.getTokenString();

        log.info("nowToken:::{}", "Bearer " + nowToken);

        AccessToken accessToken = context.getToken();
        String userName = String.valueOf(accessToken.getOtherClaims().get(UserOtherClaim.USER_NAME));
        String userLocale = String.valueOf(accessToken.getOtherClaims().get(UserOtherClaim.LOCALE));
        currentUserService.getCurrentUser().setUserName(userName);
        currentUserService.getCurrentUser().setLocale(userLocale);


        return new UserDTO(context);
    }

    public UserDetailsDTO selectUserDetail(String userId) throws IOException {
        if (StringUtil.isEmpty(userId)) {
            throw new IllegalArgumentException("selectUserByUserId() userId can not be empty");
        }

        UserResource userResource = getUserResource(userId);
        UserRepresentation user = userResource.toRepresentation();

        return buildUserDetail(user);
    }

    private UserResource getUserResource(String userId) {
        Keycloak keycloak = buildKeycloak();
        RealmResource realmResource = keycloak.realm(realm);
        UserResource userResource = realmResource.users().get(userId);
        return userResource;
    }

    private UserDetailsDTO buildUserDetail(UserRepresentation user) throws IOException {

        UserDetailsDTO userDetails = new UserDetailsDTO();
        Map<String, List<String>> attribute = user.getAttributes();

        if (attribute == null) {
            throw new UserAttributeException(user.getId());
        }

        userDetails.setUserId(user.getId());
        userDetails.setUserEmail(user.getEmail());
        userDetails.setEmail(user.getEmail());
        userDetails.setUseYn(Boolean.TRUE.equals(user.isEnabled()) ? CommonConstants.YES : CommonConstants.NO);
        userDetails.setFirstName(user.getFirstName());
        userDetails.setLastName(user.getLastName());
        fetchUserAttribute(userDetails, attribute);
        return userDetails;
    }

    private void fetchUserAttribute(UserDetailsDTO userDetails, Map<String, List<String>> attribute) throws IOException {

        // TODO : attribute 와 키값과 userDetails의 필드명이 같은지 확인할 것
        // attribute 의 키값(snake)을 카멜로 변환하였을 때 userDetails 의 필드명과 일치해야 한다!!
        Map<String,String> userDetailsMap = new HashMap<>();

        if (!ObjectUtils.isEmpty(attribute)) {
            attribute.forEach((k,v) ->{
                if (!ObjectUtils.isEmpty(v)) {
                    if (Arrays.asList(attributeArr).contains(k)) {
                        log.info("kkkk : {}", k);
                        userDetailsMap.put(StringUtil.snakeToCamel(k), v.get(0));
                    }
                }
            });

            ObjectMapper mapper = new ObjectMapper();
            UserDetailsDTO userDetailsDTO = mapper.readValue(mapper.writeValueAsString(userDetailsMap), UserDetailsDTO.class);

            BeanUtils.copyProperties(userDetails, userDetailsDTO, ignoreProperties);
        }

    }

    public Map<String, String> updateUserPassword(UserChangePasswordDTO changePasswordmodel) {
        log.info("changePasswordmodel::{}", changePasswordmodel.toString());

        Map<String, String> resultMap = new HashMap<>();

        String userEmail = changePasswordmodel.getEmail();
        String userId = userMapper.selectKeyCloakUserId(userEmail);

        if ( dconMasterUserId.equals(userId)) {
            throw new RuntimeExceptionBase(ResultCode.USER_NOT_AVAILABLE_EXCEPTION);
        }

        UserResource userResource = getUserResource(userId);
        CredentialRepresentation credential = setCredential(changePasswordmodel.getNewCredential());
        try {
            userResource.resetPassword(credential);

            UserRepresentation user = userResource.toRepresentation();
            user.getAttributes().put(UserOtherClaim.ERROR_CNT, Arrays.asList(CommonConstants.ZERO));
//            user.getAttributes().put(UserOtherClaim.PWD_INIT_YN,Arrays.asList(CommonConstants.YES));
//            user.getAttributes().put(UserOtherClaim.PWD_CHG_DT, Arrays.asList(String.valueOf(System.currentTimeMillis())));
//            user.getAttributes().put(UserOtherClaim.SKIP_PWD_CHG_DT, Arrays.asList(String.valueOf(System.currentTimeMillis())));
            userResource.update(user);
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

        if ( dconMasterUserId.equals(userId)) {
            resultMap.put(RESULT_STRING, CommonConstants.NO);
            return resultMap;
        }

        try {
            Map<String, String> param = new HashMap<>();
//                param.put(UserOtherClaim.USER_TEL_NO, Utils.nvl(useChg.getTelNo(),""));
//                param.put(UserOtherClaim.USER_MBL_TEL_CNTR_CD, useChg.getUserTelNoCtrCd());
//                param.put(UserOtherClaim.USER_REGIST_COMPANY_NAME, useChg.getRegistCompanyName());
            param.put(UserOtherClaim.LOCALE, useChg.getLocale());
            param.put("firstName", useChg.getFirstName());
            param.put("lastName", useChg.getLastName());
            param.put(UserOtherClaim.ERROR_CNT, "2");

            resultMap = updateKeyCloakUser(userId, param);
        } catch (NotFoundException notFoundException) {
            //   result = "NotFoundException";
            //    resultMap.put(RESULT_STRING, result);
        } catch (Exception e) {
            //   result = "DB ERROR";
            //   resultMap.put(RESULT_STRING, result);
            //  log.info("updateUser5::{}",e.getMessage());
        }

        return resultMap;
    }

    public Map<String, String> updateKeyCloakUser(String userId, Map<String, String> param) {

        Map<String, String> result = new HashedMap<>();

        UserResource userResource = getUserResource(userId);
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
            log.info("Exception : {}", e);
        }
        return insertResultCnt;
    }

    public int insertKeycloakUserInfo(UserCreateDTO user, String password) {
        int cnt = 0;
        // 1-1. Configure Keycloak
        try {
            Keycloak keycloak = buildKeycloak();

            RealmResource realmResource = keycloak.realm(realm);
            UsersResource usersResource = realmResource.users();

            UserRepresentation createUser = new UserRepresentation();
            createUser = setInsertUserRepresentation(createUser, user);
            CredentialRepresentation credential = setCredential(password);
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

//    public Keycloak buildKeycloak() {
//        return KeycloakBuilder.builder()
//                .serverUrl(authServerUrl) //
//                .realm("master")
//                .username("admin")
//                .password("1q2w3e4r5t!!Q")
//                .clientId("dcon-master")
//                .build();
//    }

    public Keycloak buildKeycloak() {
        currentUserService
        return KeycloakBuilder.builder()
                .serverUrl(authServerUrl) //
                .realm("dcon")
                .clientId("dcon-master")
                .clientSecret("jyFIw9SdojDrokPLjr8JY7bsp5jL6oZK")
//                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .grantType(OAuth2Constants.PASSWORD)
                .username("dcon-admin")
                .password("1q2w3e4r5t!!Q")

                .build();
    }


    public CredentialRepresentation setCredential(String password) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        return credential;
    }

    public UserRepresentation setInsertUserRepresentation(UserRepresentation createUser,
                                                          UserCreateDTO user) {

        String userFullName = Objects.equals("en", user.getLocale()) ? user.getFirstName() + " " + user.getLastName() : user.getLastName() + user.getFirstName();

        createUser.setId(user.getUserId());
        createUser.setUsername(user.getUserName());
        createUser.setEmail(user.getUserName());
        createUser.setFirstName(user.getFirstName());
        createUser.setLastName(user.getLastName());
        createUser.setEnabled(true);
        Map<String, List<String>> attributes = new HashedMap<>();
        attributes.put(UserOtherClaim.LOCALE, Arrays.asList(user.getLocale()));
//        attributes.put(UserOtherClaim.CREATE_ID, Arrays.asList(currentUser.getUserId()));
//        attributes.put(UserOtherClaim.RECEIVE_SMS, Arrays.asList(user.getReceiveSMS()? "Y": "N"));
//        attributes.put(UserOtherClaim.USER_MBL_TEL_CNTR_CD, Arrays.asList(user.getUserTelNoCtrCd()));
//        attributes.put(UserOtherClaim.USER_TEL_NO, Arrays.asList(user.getUserTelNo()));
        attributes.put(UserOtherClaim.USER_NAME, Arrays.asList(userFullName));
        createUser.setAttributes(attributes);

        return createUser;
    }

    public void withdraw(String userId) {

        if ( dconMasterUserId.equals(userId)) {
            throw new RuntimeExceptionBase(ResultCode.USER_NOT_AVAILABLE_EXCEPTION);
        }
        Keycloak keycloak = buildKeycloak();

        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();
        usersResource.delete(userId);
    }
}