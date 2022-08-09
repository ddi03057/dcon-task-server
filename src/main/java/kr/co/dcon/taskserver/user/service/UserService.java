package kr.co.dcon.taskserver.user.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.dcon.taskserver.auth.dto.TokenDTO;
import kr.co.dcon.taskserver.auth.dto.UserDetailsDTO;
import kr.co.dcon.taskserver.auth.service.CurrentUserService;
import kr.co.dcon.taskserver.common.constants.CommonConstants;
import kr.co.dcon.taskserver.common.constants.ResultCode;
import kr.co.dcon.taskserver.common.constants.UserOtherClaim;
import kr.co.dcon.taskserver.common.dto.SendEmailDTO;
import kr.co.dcon.taskserver.common.exception.UserAttributeException;
import kr.co.dcon.taskserver.common.service.MailSendService;
import kr.co.dcon.taskserver.common.util.EncryptUtil;
import kr.co.dcon.taskserver.common.util.StringUtil;
import kr.co.dcon.taskserver.common.util.Utils;
import kr.co.dcon.taskserver.user.dto.*;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

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

    @Value("${dcon.realmName}")
    private String realmName;

    @Value("${dcon.keycloak.dconmaster.userid}")
    private String dconMasterUserId;
    private CurrentUserService currentUserService;

    private MailSendService mailSendService;

    private RestTemplate restTemplate;

    private UserMapper userMapper;

    private static final String RESULT_STRING = "result";

    public static String RESULT_CODE = "RESULT_CODE";
    public static String FRIST_NAME = "firstName";
    public static String LAST_NAME = "lastName";

    @Value("${front.env}")
    private String env;

    private static final String[] attributeArr = {UserOtherClaim.USER_NAME, UserOtherClaim.USER_LOCALE, UserOtherClaim.ERROR_CNT
                                                , UserOtherClaim.UPDATED_ID, UserOtherClaim.UPDATED_DATE, UserOtherClaim.AUTH
                                                , UserOtherClaim.USER_EMAIL, UserOtherClaim.USE_YN};
    private static final String[] ignoreProperties = {"userId", "email", "firstName", "lastName", "userFullName"};

    public UserService(CurrentUserService currentUserService, UserMapper userMapper, RestTemplate restTemplate, MailSendService mailSendService) {
        this.currentUserService = currentUserService;
        this.userMapper = userMapper;
        this.restTemplate = restTemplate;
        this.mailSendService = mailSendService;
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

        if(CommonConstants.local.equals(env) || CommonConstants.dev.equals(env)){
            log.info("nowToken :  {}", "Bearer " + nowToken);
        }


        AccessToken accessToken = context.getToken();
        String userName = String.valueOf(accessToken.getOtherClaims().get(UserOtherClaim.USER_NAME));
        String userAuth = String.valueOf(accessToken.getOtherClaims().get(UserOtherClaim.AUTH));
        String userUseYn = String.valueOf(accessToken.getOtherClaims().get(UserOtherClaim.USE_YN));
        String userLocale = String.valueOf(accessToken.getOtherClaims().get(UserOtherClaim.USER_LOCALE));
        String passwordInitYn = String.valueOf(accessToken.getOtherClaims().get(UserOtherClaim.PASSWORD_INIT_YN));

        currentUserService.getCurrentUser().setUserName(userName);
        currentUserService.getCurrentUser().setLocale(userLocale);
        currentUserService.getCurrentUser().setAuth(userAuth);
        currentUserService.getCurrentUser().setUseYn(userUseYn);
        currentUserService.getCurrentUser().setPasswordInitYn(passwordInitYn);
        currentUserService.setRealmInfo();

        return new UserDTO(context);
    }

    public UserDetailsDTO selectUserDetail(String userId) throws Exception {
        if (StringUtil.isEmpty(userId)) {
            throw new IllegalArgumentException("selectUserByUserId() userId can not be empty");
        }

        UserResource userResource = getUserResource(userId);
        UserRepresentation user = userResource.toRepresentation();

        String userLocale = userMapper.selectUserLocale(userId);
        if ( StringUtil.isEmpty(userLocale) ) {
            userLocale = "ko";
        }
        return buildUserDetail(user, userLocale);
    }

    private UserResource getUserResource(String userId) throws Exception {
        Keycloak keycloak = buildKeycloak();
        RealmResource realmResource = keycloak.realm(realm);
        UserResource userResource = realmResource.users().get(userId);
        return userResource;
    }

    private UserDetailsDTO buildUserDetail(UserRepresentation user, String userLocale) throws IOException {

        UserDetailsDTO userDetails = new UserDetailsDTO();
        Map<String, List<String>> attribute = user.getAttributes();

        if (attribute == null) {
            throw new UserAttributeException(user.getId());
        }
        String userFullName = Objects.equals("en", userLocale) ? user.getFirstName() + " " + user.getLastName() : user.getLastName() + user.getFirstName();

        userDetails.setUserId(user.getId());
        userDetails.setUserEmail(user.getEmail());
        userDetails.setEmail(user.getEmail());
        userDetails.setUseYn(Boolean.TRUE.equals(user.isEnabled()) ? CommonConstants.YES : CommonConstants.NO);
        userDetails.setFirstName(user.getFirstName());
        userDetails.setLastName(user.getLastName());
        userDetails.setUserFullName(userFullName);
        log.info("userDetails : {}", userDetails.toString());
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

            BeanUtils.copyProperties(userDetailsDTO, userDetails , ignoreProperties);
        }
    }

    public ResultCode updateUserPassword(UserChangePasswordDTO changePasswordmodel) {

        if (!changePasswordmodel.getNewCredential().equals(changePasswordmodel.getNewCredentialConfirm())) {
            return ResultCode.BAD_REQUEST;
        }
        if (changePasswordmodel.getEmail().equals(changePasswordmodel.getNewCredential())) {
            return ResultCode.BAD_REQUEST;
        }

        String userEmail = changePasswordmodel.getEmail();
        String userAuth = currentUserService.getCurrentUser().getAuth();

        log.info("userAuth : {}", userAuth);
        Map<String, Object> paramMap = new HashedMap<>();
        paramMap.put("userEmail", userEmail);
        paramMap.put("userAuth", userAuth);
        String userId = userMapper.selectKeyCloakUserId(paramMap);

        if (dconMasterUserId.equals(userId)) {
            return ResultCode.USER_NOT_AVAILABLE_EXCEPTION;
        }

        try {
            UserResource userResource = getUserResource(userId);
            CredentialRepresentation credential = setCredential(changePasswordmodel.getNewCredential());

            userResource.resetPassword(credential);

            UserRepresentation user = userResource.toRepresentation();
            user.getAttributes().put(UserOtherClaim.ERROR_CNT, Arrays.asList(CommonConstants.ZERO));
            user.getAttributes().put(UserOtherClaim.UPDATED_DATE, Arrays.asList(Utils.getCurrentDateYYMMDD()));
            user.getAttributes().put(UserOtherClaim.UPDATED_ID, Arrays.asList(currentUserService.getCurrentUser().getUserId()));
            user.getAttributes().put(UserOtherClaim.PASSWORD_INIT_YN, Arrays.asList(CommonConstants.PASSWORD_INIT_N));
//            user.getAttributes().put(UserOtherClaim.PWD_INIT_YN,Arrays.asList(CommonConstants.YES));
//            user.getAttributes().put(UserOtherClaim.PWD_CHG_DT, Arrays.asList(String.valueOf(System.currentTimeMillis())));
//            user.getAttributes().put(UserOtherClaim.SKIP_PWD_CHG_DT, Arrays.asList(String.valueOf(System.currentTimeMillis())));
            userResource.update(user);

            SendEmailDTO sendEmailDTO = new SendEmailDTO();
            try {
                // TODO : 이메일 전송
                sendEmailDTO.setSendYn("Y");
            } catch (Exception e) {
                sendEmailDTO.setSendYn("N");
            }

            sendEmailDTO.setUserName(user.getUsername());
            sendEmailDTO.setUserEmail(user.getEmail());
            sendEmailDTO.setSendType(CommonConstants.EMAIL_PASSWORD_RESET);
            sendEmailDTO.setCreateDate(Utils.getCurrentDateYYMMDD());
            sendEmailDTO.setSendDate(Utils.getCurrentDateYYMMDD());

            mailSendService.insertMailSend(sendEmailDTO);

        } catch (BadRequestException e) {

            // credential setvalue시 전 암호와 같은 암호라면 400 에러 발생.
            return ResultCode.BAD_REQUEST;
        } catch (Exception e) {
            return ResultCode.ETC_ERROR;
        }

        return ResultCode.OK;
    }


    @Transactional
    public ResultCode updateUser(UserChangeDTO useChg) {
        ResultCode resultCode = ResultCode.OK;

        String userEmail = useChg.getUserEmail();
        String userAuth = currentUserService.getCurrentUser().getAuth();

        log.info("userAuth : {}", userAuth);

        Map<String, Object> paramMap = new HashedMap<>();
        paramMap.put("userEmail", userEmail);
        paramMap.put("userAuth", userAuth);

        String userId = userMapper.selectKeyCloakUserId(paramMap);

        if (dconMasterUserId.equals(userId)) {
            return ResultCode.USER_NOT_EXISTS_EXCEPTION;
        }

        try {
            Map<String, String> param = new HashMap<>();
//                param.put(UserOtherClaim.USER_TEL_NO, Utils.nvl(useChg.getTelNo(),""));
//                param.put(UserOtherClaim.USER_MBL_TEL_CNTR_CD, useChg.getUserTelNoCtrCd());
//                param.put(UserOtherClaim.USER_REGIST_COMPANY_NAME, useChg.getRegistCompanyName());
//            param.put(LAST_NAME, useChg.getLastName());
//            param.put(FRIST_NAME, useChg.getFirstName());
            param.put(UserOtherClaim.USER_LOCALE, useChg.getUserLocale());
            param.put(UserOtherClaim.AUTH, useChg.getUserAuth());
            param.put(UserOtherClaim.USE_YN, useChg.getUseYn());
            param.put(UserOtherClaim.ERROR_CNT, "0");
            param.put(UserOtherClaim.PASSWORD_INIT_YN, CommonConstants.PASSWORD_INIT_N);

            resultCode = updateKeyCloakUser(userId, param);
        } catch (NotFoundException notFoundException) {
            resultCode = ResultCode.USER_NOT_EXISTS_EXCEPTION;
        } catch (Exception e) {
            resultCode = ResultCode.ETC_ERROR;
        }

        return resultCode;
    }

    public ResultCode updateKeyCloakUser(String userId, Map<String, String> param) throws Exception {

        ResultCode resultCode = ResultCode.OK;

        UserResource userResource = getUserResource(userId);
        UserRepresentation user = userResource.toRepresentation();
// lastName, firstName 은 정책상 수정불가
//        if (param.containsKey(FRIST_NAME) && param.get(FRIST_NAME) != null) {
//            user.setFirstName(param.get(FRIST_NAME));
//        }
//        if (param.containsKey(LAST_NAME) && param.get(LAST_NAME) != null) {
//            user.setLastName(param.get(LAST_NAME));
//        }

        Map<String, List<String>> attributes = user.getAttributes();
        for (Map.Entry<String, String> entry : param.entrySet()) {
//            if (Objects.equals("firstName", entry.getKey())
//                    || Objects.equals(LAST_NAME, entry.getKey())) {
//                continue;
//            }

            attributes.put(entry.getKey(), Arrays.asList(entry.getValue()));
        }
        if (this.currentUserService.getCurrentUser() != null) {
            attributes.put(UserOtherClaim.UPDATED_ID, Arrays.asList(this.currentUserService.getCurrentUser().getUserId()));
        }
        attributes.put(UserOtherClaim.UPDATED_DATE, Arrays.asList(String.valueOf(System.currentTimeMillis())));
        try {
            user.setRequiredActions(null);
            userResource.update(user);
        } catch (Exception e) {
            resultCode = ResultCode.ETC_ERROR;
        }

        return resultCode;
    }

    @Transactional
    public ResultCode createUser(UserCreateDTO user) {

//        String password = Utils.getRandomString();

        try {
            Keycloak keycloak = buildKeycloak();

            RealmResource realmResource = keycloak.realm(realm);
            UsersResource usersResource = realmResource.users();

            UserRepresentation createUser = new UserRepresentation();
            createUser = setInsertUserRepresentation(createUser, user);

            if (StringUtils.isEmpty(user.getPassword())) {
                return ResultCode.INVALID_PARAMETER_PASSWORD;
            }

//            CredentialRepresentation credential = setCredential(password);
            CredentialRepresentation credential = setCredential(user.getPassword());
            createUser.setCredentials(Arrays.asList(credential));

            int userCount = userMapper.selectKeyCloakUserCount(user);

            if (userCount > 0) {
                return ResultCode.USER_EXIST_ALREADY;
            } else {

                usersResource.create(createUser);
                // TODO : 임의로 생성된 패스워드 공지 이메일을 보낸다.?

                // 사용자에게 임의로 생성된 패스워드 이메일 공지 후 히스토리 저장??
                // 사용자에게 패스워드 받는 것으로 수정..
//                    SendEmailDTO sendEmailDTO = new SendEmailDTO();
//                    sendEmailDTO.setSendYn("Y");
//                    sendEmailDTO.setUserName(createUser.getUsername());
//                    sendEmailDTO.setUserEmail(createUser.getEmail());
//                    sendEmailDTO.setSendType(CommonConstants.EMAIL_PASSWORD_INIT);
//                    sendEmailDTO.setCreateDate(Utils.getCurrentDateYYMMDD());
//                    sendEmailDTO.setSendDate(Utils.getCurrentDateYYMMDD());
//                    mailSendService.insertMailSend(sendEmailDTO);

                return ResultCode.OK;
            }

        } catch (Exception e) {
            return ResultCode.ETC_ERROR;
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
        createUser.setUsername(userFullName);
        createUser.setEmail(user.getUserEmail());
        createUser.setFirstName(user.getFirstName());
        createUser.setLastName(user.getLastName());
        createUser.setEnabled(true);

        createUser.setEmailVerified(false);
        createUser.setRequiredActions(null);

        Map<String, List<String>> attributes = new HashedMap<>();
        attributes.put(UserOtherClaim.USER_LOCALE, Arrays.asList(user.getUserLocale()));

        // 업데이트 할 때는 뺄 것
        attributes.put(UserOtherClaim.CREATE_ID, Arrays.asList(currentUserService.getCurrentUser().getUserId()));
        attributes.put(UserOtherClaim.CREATE_DATE, Arrays.asList(Utils.getCurrentDateYYMMDD()));

        attributes.put(UserOtherClaim.UPDATED_ID, Arrays.asList(currentUserService.getCurrentUser().getUserId()));
        attributes.put(UserOtherClaim.UPDATED_DATE, Arrays.asList(Utils.getCurrentDateYYMMDD()));

        attributes.put(UserOtherClaim.USER_NAME, Arrays.asList(userFullName));
        attributes.put(UserOtherClaim.ERROR_CNT, Arrays.asList(String.valueOf(user.getErrorCnt())));
        attributes.put(UserOtherClaim.AUTH, Arrays.asList(user.getUserAuth()));
        attributes.put(UserOtherClaim.USE_YN, Arrays.asList(user.getUseYn()));
        attributes.put(UserOtherClaim.USER_EMAIL, Arrays.asList(user.getUserEmail()));
        attributes.put(UserOtherClaim.PASSWORD_INIT_YN, Arrays.asList(CommonConstants.PASSWORD_INIT_Y) );

        createUser.setAttributes(attributes);

        return createUser;
    }

    public ResultCode withdraw(String userId) throws Exception {

        try {
            if (dconMasterUserId.equals(userId)) {
                return ResultCode.USER_NOT_AVAILABLE_EXCEPTION;
            }
            Keycloak keycloak = buildKeycloak();

            RealmResource realmResource = keycloak.realm(realm);
            UsersResource usersResource = realmResource.users();
            usersResource.delete(userId);
        } catch (Exception e) {
            return ResultCode.ETC_ERROR;
        }
        return ResultCode.OK;
    }

    public List<UserRepresentation> selectUserList(String userId) throws Exception {
        Keycloak keycloak = buildKeycloak();
        List<UserRepresentation> list = keycloak.realm(realm).users().list();
        return list;
    }

    public List<UserListDTO> selectProjectUserList(UserListProjectReqDTO reqDTO) {
        reqDTO.setProjectRealm(realmName);
        reqDTO.setRealmMasterUseUserId(dconMasterUserId);
        List<UserListDTO> userList = new ArrayList<>();
        List<UserListDTO> list = userMapper.selectProjectUserList(reqDTO);

        String userLocal = currentUserService.getCurrentUser().getLocale();
        for(UserListDTO vo : list){
            if(CommonConstants.KO.equals(userLocal)){
                vo.setUserName(vo.getLastName().concat(vo.getFirstName()));
            }else{
                vo.setUserName(vo.getFirstName().concat(vo.getLastName()));
            }
            userList.add(vo);
        }
        return userList;
    }

    public UserListDTO selectProjectUserDetail(UserListProjectReqDTO reqDTO) {

        String userLocal = currentUserService.getCurrentUser().getLocale();
        reqDTO.setProjectRealm(realmName);
        UserListDTO dto = userMapper.selectProjectUserDetail(reqDTO);
        if(CommonConstants.KO.equals(userLocal)){
            dto.setUserName(dto.getLastName().concat(dto.getFirstName()));
        }else{
            dto.setUserName(dto.getFirstName().concat(dto.getLastName()));
        }
        return dto;
    }
    public boolean validUserPassword(String userEmail, String password) {

        if (StringUtil.isEmpty(userEmail)) {
            throw new IllegalArgumentException("validUserPassword() userName can not be empty");
        }
        if (StringUtil.isEmpty(password)) {
            throw new IllegalArgumentException("validUserPassword() password can not be empty");
        }
        HttpHeaders headers = buildHeader();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.add("client_id", currentUserService.getRealmInfo().getClientId());
        params.add("client_secret", currentUserService.getRealmInfo().getClientSecret());
        params.add("username", userEmail);
        params.add("password", password);
        params.add("grant_type", "password");
        String tokenUrl = String.format("%s/realms/%s/protocol/openid-connect/token", this.authServerUrl, currentUserService.getRealmInfo().getRealmName());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        try {
            this.restTemplate.postForEntity(tokenUrl, request, TokenDTO.class, params);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    private HttpHeaders buildHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }
}
