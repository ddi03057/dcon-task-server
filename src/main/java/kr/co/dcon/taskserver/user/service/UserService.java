package kr.co.dcon.taskserver.user.service;


import kr.co.dcon.taskserver.auth.dto.UserSimpleDTO;
import kr.co.dcon.taskserver.auth.service.CurrentUserService;
import kr.co.dcon.taskserver.common.constants.CommonConstants;
import kr.co.dcon.taskserver.common.constants.UserOtherClaim;
import kr.co.dcon.taskserver.common.util.Utils;
import kr.co.dcon.taskserver.user.dto.UserChangeDTO;
import kr.co.dcon.taskserver.user.dto.UserChangePasswordDTO;
import kr.co.dcon.taskserver.user.dto.UserCreateDTO;
import kr.co.dcon.taskserver.user.dto.UserDTO;
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
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
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

    //  @Autowired
    //  private AuthClient authClient;
    @Autowired
    private CurrentUserService currentUserService;

    private static final String RESULT_STRING = "result";


    public UserDTO selectCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) authentication;
        @SuppressWarnings("unchecked")
        KeycloakPrincipal<KeycloakSecurityContext> keycloakPrincipal = (KeycloakPrincipal<KeycloakSecurityContext>) token.getPrincipal();
        KeycloakSecurityContext context = keycloakPrincipal.getKeycloakSecurityContext();


        log.info("currentUserService.;::{}", currentUserService.getCurrentUser().toString());


//        UsersResource userResource = getKeycloakUserResource();
//        UserRepresentation user = new UserRepresentation();

        return new UserDTO(context);
    }

    public Map<String, String> updateUserPassword(UserChangePasswordDTO changePasswordmodel) {
        log.info("changePasswordmodel::{}",changePasswordmodel.toString());

        Map<String, String> resultMap = new HashMap<>();

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

        return resultMap;
    }

    public Keycloak buildKeycloak() {
        return KeycloakBuilder.builder().serverUrl("http://localhost:8081/auth/") //
                .realm("dcon") //
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId("dcon-task-backend") //
              //  .username("admin")
             //   .password("1q2w3e4r5t!!Q")
                .clientSecret("dnLpwknmu0eAmeBPqV1XDz1cxXSPFE5s").build();


    }
    public CredentialRepresentation setCredential(String password) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        return credential;
    }

    @Transactional
    public Map<String, String> updateUser(String userId, UserChangeDTO useChg) {
        Map<String, String> resultMap = new HashMap<>();
        String result = "Y";
        Keycloak keycloak = buildKeycloak();
        RealmResource realmResource = keycloak.realm("dcon");
        log.info("realmResource.users()::{}",realmResource.users());
        log.info("updateUser::");
        UserResource userResource = realmResource.users().get(currentUserService.getCurrentUser().getUserId());
        log.info("updateUser1::");
       // result = checkUser(useChg, result);
    //    if( "Y".equals(result)) {
            try {
                log.info("updateUser3::");
                Map<String, String> param = new HashMap<>();
//                param.put(UserOtherClaim.USER_TEL_NO, Utils.nvl(useChg.getTelNo(),""));
//                param.put(UserOtherClaim.USER_MBL_TEL_CNTR_CD, useChg.getUserTelNoCtrCd());
//                param.put(UserOtherClaim.USER_REGIST_COMPANY_NAME, useChg.getRegistCompanyName());
                param.put(UserOtherClaim.LOCALE, useChg.getLocale());
                param.put("firstName", useChg.getFirstName());
                param.put("lastName", useChg.getLastName());
                UserRepresentation user = userResource.toRepresentation();
                user.getAttributes().put("ko", Collections.singletonList(UserOtherClaim.LOCALE));
                user.setEmail("suseok.park");
                userResource.update(user);
            } catch (NotFoundException notFoundException) {
                result = "NotFoundException";
                resultMap.put(RESULT_STRING, result);
                log.info("updateUser4::");
            } catch (Exception e) {
                result = "DB ERROR";
                resultMap.put(RESULT_STRING, result);
                log.info("updateUser5::{}",e.getMessage());
            }
     //   }
        return resultMap;
    }

    @Transactional
    public void createUser( UserCreateDTO user) {
        Response response = null;
        UserSimpleDTO currentUser =  currentUserService.getCurrentUser();
        String password = Utils.getRandomString();
        user.setPassword(password);
        try {
            UserRepresentation createUser = new UserRepresentation();
            createUser.setId(user.getUserId());
            createUser.setUsername(user.getUserName());
            createUser.setEmail(user.getUserName());
            createUser.setFirstName(user.getFirstName());
            createUser.setLastName(user.getLastName());
            createUser.setEnabled(true);
            createUser.setEmailVerified(true);

            Map<String, List<String>> attributes = new HashedMap<>();
//            attributes.put(UserOtherClaim.LOCALE,  Arrays.asList(user.getLocale()));
//            attributes.put(UserOtherClaim.CREATE_ID, Arrays.asList(currentUser.getUserId()));

            createUser.setAttributes(attributes);
            CredentialRepresentation credential = new CredentialRepresentation();
            credential.setTemporary(false);
            credential.setType(CredentialRepresentation.PASSWORD);
            credential.setValue(user.getPassword());
            createUser.setCredentials(Arrays.asList(credential));

            Keycloak keycloak = buildKeycloak();
            RealmResource realmResource = keycloak.realm("dcon");
            UsersResource usersResource = realmResource.users();
//            log.info("realmResource.users()::{}",realmResource.users());
            log.info("updateUser::");
            UserResource userResource = (UserResource) keycloak.realm("dcon").users();
         //   UserResource userResource = realmResource.users().get(currentUserService.getCurrentUser().getUserId());
            log.info("updateUser1::");
            log.info("createUser: {}", createUser);
            response = usersResource.create(createUser);
            log.info("response: {}", response.toString());
        } catch (Exception e) {

        } finally {

            if (response != null) {
                response.close();
            }
        }
    }
}
