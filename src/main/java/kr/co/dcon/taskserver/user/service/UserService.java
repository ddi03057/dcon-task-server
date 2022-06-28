package kr.co.dcon.taskserver.user.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.dcon.taskserver.auth.dto.UserDetailsDTO;
import kr.co.dcon.taskserver.auth.service.CurrentUserService;
import kr.co.dcon.taskserver.common.constants.CommonConstants;
import kr.co.dcon.taskserver.common.constants.ResultCode;
import kr.co.dcon.taskserver.common.constants.UserOtherClaim;
import kr.co.dcon.taskserver.common.dto.NoResultDTO;
import kr.co.dcon.taskserver.common.exception.RuntimeExceptionBase;
import kr.co.dcon.taskserver.common.exception.UserAttributeException;
import kr.co.dcon.taskserver.common.util.EncryptUtil;
import kr.co.dcon.taskserver.common.util.StringUtil;
import kr.co.dcon.taskserver.common.util.Utils;
import kr.co.dcon.taskserver.user.dto.UserChangeDTO;
import kr.co.dcon.taskserver.user.dto.UserChangePasswordDTO;
import kr.co.dcon.taskserver.user.dto.UserCreateDTO;
import kr.co.dcon.taskserver.user.dto.UserDTO;
import kr.co.dcon.taskserver.user.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.map.HashedMap;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

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

    @Value("${dcon.keycloak.dconmaster.userid}")
    private String dconMasterUserId;
    private CurrentUserService currentUserService;

    private UserMapper userMapper;

    private static final String RESULT_STRING = "result";

    public static String RESULT_CODE = "RESULT_CODE";
    public static String FRIST_NAME = "firstName";
    public static String LAST_NAME = "lastName";


    private static final String[] attributeArr = {UserOtherClaim.USER_NAME, UserOtherClaim.USER_LOCALE};
    private static final String[] ignoreProperties = {"userId", "userEmail", "email", "useYn", FRIST_NAME, LAST_NAME};

    public UserService(CurrentUserService currentUserService, UserMapper userMapper) {
        this.currentUserService = currentUserService;
        this.userMapper = userMapper;
    }

    @Value("${dcon.encrypt}")
    public String encryptKey;

    public UserDTO selectCurrentUser() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) authentication;
        @SuppressWarnings("unchecked")
        KeycloakPrincipal<KeycloakSecurityContext> keycloakPrincipal = (KeycloakPrincipal<KeycloakSecurityContext>) token.getPrincipal();
        KeycloakSecurityContext context = keycloakPrincipal.getKeycloakSecurityContext();
        String nowToken = context.getTokenString();

        log.info("nowToken :  {}", "Bearer " + nowToken);

        AccessToken accessToken = context.getToken();
        String userName = String.valueOf(accessToken.getOtherClaims().get(UserOtherClaim.USER_NAME));
        String userAuth = String.valueOf(accessToken.getOtherClaims().get(UserOtherClaim.AUTH));
        String userUseYn = String.valueOf(accessToken.getOtherClaims().get(UserOtherClaim.USE_YN));
        String userLocale = String.valueOf(accessToken.getOtherClaims().get(UserOtherClaim.USER_LOCALE));
        currentUserService.getCurrentUser().setUserName(userName);
        currentUserService.getCurrentUser().setLocale(userLocale);
        currentUserService.getCurrentUser().setAuth(userAuth);
        currentUserService.getCurrentUser().setUseYn(userUseYn);
        currentUserService.setRealmInfo();

        return new UserDTO(context);
    }

    public UserDetailsDTO selectUserDetail(String userId) throws Exception {
        if (StringUtil.isEmpty(userId)) {
            throw new IllegalArgumentException("selectUserByUserId() userId can not be empty");
        }

        UserResource userResource = getUserResource(userId);
        UserRepresentation user = userResource.toRepresentation();

        return buildUserDetail(user);
    }

    private UserResource getUserResource(String userId) throws Exception {
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

        Map<String, String> userDetailsMap = new HashMap<>();

        if (!ObjectUtils.isEmpty(attribute)) {
            attribute.forEach((k, v) -> {
                if (!ObjectUtils.isEmpty(v) && Arrays.asList(attributeArr).contains(k)) {
                    userDetailsMap.put(StringUtil.snakeToCamel(k), v.get(0));
                }
            });

            ObjectMapper mapper = new ObjectMapper();
            UserDetailsDTO userDetailsDTO = mapper.readValue(mapper.writeValueAsString(userDetailsMap), UserDetailsDTO.class);

            BeanUtils.copyProperties(userDetails, userDetailsDTO, ignoreProperties);
        }

    }

    public Map<String, Object> updateUserPassword(UserChangePasswordDTO changePasswordmodel) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();

        String userEmail = changePasswordmodel.getEmail();
        String userId = userMapper.selectKeyCloakUserId(userEmail);

        if (dconMasterUserId.equals(userId)) {
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

        } catch (BadRequestException e) {

            // credential setvalue시 전 암호와 같은 암호라면 400 에러 발생.
            resultMap.put(RESULT_STRING, CommonConstants.NO);
            return resultMap;
        } catch (Exception e) {

            resultMap.put(RESULT_STRING, "Server Error");
            return resultMap;
        }

        return resultMap;
    }


    @Transactional
    public Map<String, Object> updateUser(UserChangeDTO useChg) {
        Map<String, Object> resultMap = new HashMap<>();

        String userEmail = useChg.getUserEmail();
        String userId = userMapper.selectKeyCloakUserId(userEmail);

        if (!dconMasterUserId.equals(userId)) {
            resultMap.put(RESULT_CODE, ResultCode.USER_NOT_EXISTS_EXCEPTION);
            return resultMap;
        }

        try {
            Map<String, String> param = new HashMap<>();
//                param.put(UserOtherClaim.USER_TEL_NO, Utils.nvl(useChg.getTelNo(),""));
//                param.put(UserOtherClaim.USER_MBL_TEL_CNTR_CD, useChg.getUserTelNoCtrCd());
//                param.put(UserOtherClaim.USER_REGIST_COMPANY_NAME, useChg.getRegistCompanyName());
            param.put(UserOtherClaim.USER_LOCALE, useChg.getUserLocale());
            param.put(FRIST_NAME, useChg.getFirstName());
            param.put(LAST_NAME, useChg.getLastName());
            param.put(UserOtherClaim.ERROR_CNT, "0");

            resultMap = updateKeyCloakUser(userId, param);
        } catch (NotFoundException notFoundException) {
            resultMap.put(RESULT_CODE, ResultCode.USER_NOT_EXISTS_EXCEPTION);
        } catch (Exception e) {
            resultMap.put(RESULT_CODE, String.valueOf(ResultCode.ETC_ERROR));
        }

        return resultMap;
    }

    public Map<String, Object> updateKeyCloakUser(String userId, Map<String, String> param) throws Exception {

        Map<String, Object> result = new HashedMap<>();

        UserResource userResource = getUserResource(userId);
        UserRepresentation user = userResource.toRepresentation();

        if (param.containsKey(FRIST_NAME) && param.get(FRIST_NAME) != null) {
            user.setFirstName(param.get(FRIST_NAME));
        }
        if (param.containsKey(LAST_NAME) && param.get(LAST_NAME) != null) {
            user.setLastName(param.get(LAST_NAME));
        }

        Map<String, List<String>> attributes = user.getAttributes();
        for (Map.Entry<String, String> entry : param.entrySet()) {
            if (Objects.equals("fistName", entry.getKey())
                    || Objects.equals(LAST_NAME, entry.getKey())) {
                continue;
            }

            attributes.put(entry.getKey(), Arrays.asList(entry.getValue()));
        }
        if (this.currentUserService.getCurrentUser() != null) {
            attributes.put(UserOtherClaim.UPDATED_ID, Arrays.asList(this.currentUserService.getCurrentUser().getUserId()));
        }
        attributes.put(UserOtherClaim.UPDATED_DATE, Arrays.asList(String.valueOf(System.currentTimeMillis())));
        try {
            userResource.update(user);
            result.put(RESULT_STRING, CommonConstants.YES);
        } catch (Exception e) {
            result.put(RESULT_STRING, CommonConstants.NO);
        }

        return result;
    }

    @Transactional
    public Map<String, Object> createUser(UserCreateDTO user) {

        Map<String, Object> resultMap = new HashedMap<>();
        try {
            String password = Utils.getRandomString();
            insertKeycloakUserInfo(user, password, resultMap);

        } catch (Exception e) {
            log.info("Exception : {}", e);
        }
        return resultMap;
    }

    public void insertKeycloakUserInfo(UserCreateDTO user, String password, Map<String, Object> resultMap) {
        try {
            Keycloak keycloak = buildKeycloak();

            RealmResource realmResource = keycloak.realm(realm);
            UsersResource usersResource = realmResource.users();

            UserRepresentation createUser = new UserRepresentation();
            createUser = setInsertUserRepresentation(createUser, user);
            CredentialRepresentation credential = setCredential(password);
            createUser.setCredentials(Arrays.asList(credential));

            int userCount = userMapper.selectKeyCloakUserCount(user);

            if (userCount > 0) {
                resultMap.put(RESULT_STRING, ResultCode.USER_EXIST_ALREADY);
            } else {
                usersResource.create(createUser);
            }

        } catch (Exception e) {
            resultMap.put(RESULT_STRING, ResultCode.ETC_ERROR);
        }
    }


    public Keycloak buildKeycloak() throws Exception {
        return KeycloakBuilder.builder()
                .serverUrl(authServerUrl) //
                .realm(currentUserService.getRealmInfo().getRealmName())
                .clientId(currentUserService.getRealmInfo().getClientId())
                .clientSecret(currentUserService.getRealmInfo().getClientSecret())
//                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .grantType(OAuth2Constants.PASSWORD)
                .username(currentUserService.getRealmInfo().getUserName())
                // TODO : 패스워드 DB에 암호화해서 넣고 쓸 때는 암호화 풀어서 넣을 것 > 최초에 DB에 넣을 때 암호화 어떻게?
                .password(EncryptUtil.decrypt(currentUserService.getRealmInfo().getPassword(), encryptKey))
                .build();
    }

    public CredentialRepresentation setCredential(String password) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        return credential;
    }

    public UserRepresentation setInsertUserRepresentation(UserRepresentation createUser, UserCreateDTO user) {

        String userFullName = Objects.equals("en", user.getUserLocale()) ? user.getFirstName() + " " + user.getLastName() : user.getLastName() + user.getFirstName();

        createUser.setId(user.getUserId());
        createUser.setUsername(user.getUserName());
        createUser.setEmail(user.getUserEmail());
        createUser.setFirstName(user.getFirstName());
        createUser.setLastName(user.getLastName());
        createUser.setEnabled(true);
        Map<String, List<String>> attributes = new HashedMap<>();
        attributes.put(UserOtherClaim.USER_LOCALE, Arrays.asList(user.getUserLocale()));

        // 업데이트 할 때는 뺄 것
        attributes.put(UserOtherClaim.CREATE_ID, Arrays.asList(currentUserService.getCurrentUser().getUserId()));
        attributes.put(UserOtherClaim.CREATE_DATE, Arrays.asList("시간 : simpledateformat"));

        attributes.put(UserOtherClaim.UPDATED_ID, Arrays.asList(currentUserService.getCurrentUser().getUserId()));
        attributes.put(UserOtherClaim.UPDATED_DATE, Arrays.asList("시간 : simpledateformat"));

//        attributes.put(UserOtherClaim.USER_TEL_NO, Arrays.asList(user.getUserTelNo()));
        attributes.put(UserOtherClaim.USER_NAME, Arrays.asList(userFullName));
        createUser.setAttributes(attributes);

        return createUser;
    }

    public void withdraw(String userId) throws Exception {

        if (dconMasterUserId.equals(userId)) {
            throw new RuntimeExceptionBase(ResultCode.USER_NOT_AVAILABLE_EXCEPTION);
        }
        Keycloak keycloak = buildKeycloak();

        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();
        usersResource.delete(userId);
    }

    public List<UserRepresentation> selectUserList(String userId) throws Exception {
        Map<String, Object> resultMap = new HashedMap<>();
        Keycloak keycloak = buildKeycloak();
        List<UserRepresentation> list = keycloak.realm(realm).users().list();
        log.info("list.size()::{}", list.size());

        for(Iterator<UserRepresentation> it=list.iterator(); it.hasNext();){
            String str = String.valueOf(it.next());
            if(str.equals(dconMasterUserId)) it.remove();
        }
        for(UserRepresentation vo : list){
            log.info("vo.getId()::{}",vo.getId());
            if(vo.getId().equals(dconMasterUserId.trim())){
                log.info("vdconMasterUserId()::{}",dconMasterUserId);
                list.remove(vo.getId());
            }

        }

        return list;
    }
}
