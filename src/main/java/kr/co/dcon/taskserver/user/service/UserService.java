package kr.co.dcon.taskserver.user.service;


import kr.co.dcon.taskserver.auth.service.CurrentUserService;
import kr.co.dcon.taskserver.common.constants.CommonConstants;
import kr.co.dcon.taskserver.common.constants.ResultCode;
import kr.co.dcon.taskserver.common.constants.UserOtherClaim;
import kr.co.dcon.taskserver.common.util.Utils;
import kr.co.dcon.taskserver.user.dto.UserChangeDTO;
import kr.co.dcon.taskserver.user.dto.UserChangePasswordDTO;
import kr.co.dcon.taskserver.user.dto.UserCreateDTO;
import kr.co.dcon.taskserver.user.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.map.HashedMap;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.util.*;

@Service
@Slf4j
//@AllArgsConstructor
public class UserService {

    @Value("${keycloak.auth-server-url}")
    public String authServerUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.resource}")
    private String clientId;

    @Value("${dcon.keycloak.rest.clientSecret}")
    private String clientSecret;
    @Autowired
    private CurrentUserService currentUserService;

    // @Autowired
    ResteasyClient resteasyClient;
    private static final String RESULT_STRING = "result";


    public UserDTO selectCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) authentication;
        @SuppressWarnings("unchecked")
        KeycloakPrincipal<KeycloakSecurityContext> keycloakPrincipal = (KeycloakPrincipal<KeycloakSecurityContext>) token.getPrincipal();
        KeycloakSecurityContext context = keycloakPrincipal.getKeycloakSecurityContext();


        log.info("currentUserService.;::{}", currentUserService.getCurrentUser().toString());
        String nowToken = context.getTokenString();

        log.info("nowToken:::{}", "Bearer " + nowToken);
        currentUserService.getCurrentUser().setToken(nowToken);
        log.info("currentUserService.;::{}", currentUserService.getCurrentUser().toString());

//        UsersResource userResource = getKeycloakUserResource();
//        UserRepresentation user = new UserRepresentation();

        return new UserDTO(context);
    }

    public Map<String, String> updateUserPassword(UserChangePasswordDTO changePasswordmodel) {
        log.info("changePasswordmodel::{}", changePasswordmodel.toString());

        Map<String, String> resultMap = new HashMap<>();

        Keycloak keycloak = buildKeycloak();

        RealmResource realmResource = keycloak.realm(realm);
        String userId = currentUserService.getCurrentUser().getUserId();
        UserResource usersResource = realmResource.users().get(userId);
        CredentialRepresentation credential = setCredential(changePasswordmodel.getNewCredential());
        try {
            usersResource.resetPassword(credential);
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
  /*
        Keycloak keycloak = buildKeycloak();
        RealmResource realmResource = keycloak.realm("dcon");
        log.info("realmResource.users()::{}",realmResource.users());
        log.info("updateUserPassword::");
        UserResource userResource = realmResource.users().get(currentUserService.getCurrentUser().getUserId());
        log.info("updateUserPassword2::");
        CredentialRepresentation credential = setCredential(changePasswordmodel.getNewCredential());
        log.info("updateUserPassword3::");
        try {
            userResource.resetPassword(credential);
            log.info("try::");
        } catch (BadRequestException e) {
            log.info("BadRequestException::");
            // credential setvalue시 전 암호와 같은 암호라면 400 에러 발생.
            resultMap.put(RESULT_STRING, CommonConstants.NO);
            return resultMap;
        } catch (Exception e) {
            log.info("Exception::{}",e.toString());
            resultMap.put(RESULT_STRING, "Server Error");
            return resultMap;
        }
*/
        return resultMap;
    }


    @Transactional
    public Map<String, String> updateUser(String userId, UserChangeDTO useChg) {
        Map<String, String> resultMap = new HashMap<>();

        log.info("useChg.toString()::{}", useChg.toString());
        Keycloak keycloak = buildKeycloak();

        RealmResource realmResource = keycloak.realm(realm);
        //  String userId = currentUserService.getCurrentUser().getUserId();

        UserResource usersResource = realmResource.users().get(userId);


        try {
            log.info("updateUser3::");
            Map<String, String> param = new HashMap<>();
//                param.put(UserOtherClaim.USER_TEL_NO, Utils.nvl(useChg.getTelNo(),""));
//                param.put(UserOtherClaim.USER_MBL_TEL_CNTR_CD, useChg.getUserTelNoCtrCd());
//                param.put(UserOtherClaim.USER_REGIST_COMPANY_NAME, useChg.getRegistCompanyName());
            param.put(UserOtherClaim.LOCALE, useChg.getLocale());
            param.put("firstName", useChg.getFirstName());
            param.put("lastName", useChg.getLastName());
            UserRepresentation updateUser = usersResource.toRepresentation();

            updateUser.getAttributes().put("ko", Collections.singletonList(UserOtherClaim.LOCALE));
            updateUser.setEmail("suseok.park");
            usersResource.update(updateUser);
        } catch (NotFoundException notFoundException) {
            //   result = "NotFoundException";
            //    resultMap.put(RESULT_STRING, result);
            log.info("updateUser4::{}", notFoundException.getMessage());
        } catch (Exception e) {
            //   result = "DB ERROR";
            //   resultMap.put(RESULT_STRING, result);
            //  log.info("updateUser5::{}",e.getMessage());
        }


        //  updateUser = setUserRepresentation(updateUser, useChg);


        return resultMap;
    }

    @Transactional
    public int createUser(UserCreateDTO user) {
        log.info("createUser:::");

        int insertResultCnt = 0;

        try {
            // step 1: insert in Keycloak
            String password = Utils.getRandomString();

            insertResultCnt = insertKeycloakUserInfo(user, password);

            // step 02: insert in DB & send email
          //  String keycloakId = CreatedResponseUtil.getCreatedId(response);
          //  user.setKeycloakId(keycloakId);

        } catch (Exception e) {
            log.info("Exception getMessage::{}", e.getMessage());
            log.info("Exception toString::{}", e.toString());

            //  resultMap.put(RESULT_STRING, CommonConstants.NO);
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

            // 1-2. Define user in keycloak
            UserRepresentation createUser = new UserRepresentation();
            createUser = setInsertUserRepresentation(createUser, user);
            log.info("5555");
            // 1-3. Set password
            CredentialRepresentation credential = setCredential(password);
            log.info("6666");
            createUser.setCredentials(Arrays.asList(credential));
            log.info("7777");
            // 1-4. Create User in Keycloak
            try {
                String userId = currentUserService.getCurrentUser().getUserId();
                List<UserRepresentation> search = realmResource.users().search(userId); //
                log.info("search.size()::{}",search.size());
                if(search.size() > 0){
                    cnt = search.size();
                }else{
                    usersResource.create(createUser);
                }

            } catch (Exception e) {
                cnt = 2 ;
            }
            log.info("createUser: {}", createUser);
        } catch (Exception e) {
            cnt = 2 ;
            log.info("wwwww::{}", e.toString());
            log.info("wwwww getMessage::{}", e.getMessage());
        }
        return cnt;
    }

    public Keycloak buildKeycloak() {
        return KeycloakBuilder.builder()
                .serverUrl(authServerUrl) //
//                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
//                .realm("master")
//                .clientId("dcon-master-client")
//                .clientSecret("2CN2cTFvlq6LPseOo5gV0i5bM5gq4SzY")
//                .resteasyClient(resteasyClient)
//                .build();
                .realm("master")
                .username("admin")
                .password("1q2w3e4r5t!!Q")
                .clientId("admin-cli")
                .build();
//                .realm(realm) //
//                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
//                .clientId(clientId) //
//                .clientSecret(clientSecret).build();
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
        // createUser.setEmailVerified(true);
        Map<String, List<String>> attributes = new HashedMap<>();
        log.info("11");
        attributes.put(UserOtherClaim.LOCALE, Arrays.asList(user.getLocale()));
        log.info("22");
//        attributes.put(UserOtherClaim.CREATE_ID, Arrays.asList(currentUser.getUserId()));
//        attributes.put(UserOtherClaim.RECEIVE_SMS, Arrays.asList(user.getReceiveSMS()? "Y": "N"));
//        attributes.put(UserOtherClaim.USER_MBL_TEL_CNTR_CD, Arrays.asList(user.getUserTelNoCtrCd()));
//        attributes.put(UserOtherClaim.USER_TEL_NO, Arrays.asList(user.getUserTelNo()));
        attributes.put(UserOtherClaim.USER_NAME, Arrays.asList(userFullName));
        log.info("33");
        createUser.setAttributes(attributes);
        log.info("44");

        return createUser;
    }
}
