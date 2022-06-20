package kr.co.dcon.taskserver.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.dcon.taskserver.auth.dto.UserDetailsDTO;
import kr.co.dcon.taskserver.common.constants.CommonConstants;
import kr.co.dcon.taskserver.common.constants.UserOtherClaim;
import kr.co.dcon.taskserver.common.exception.UserAttributeException;
import kr.co.dcon.taskserver.user.dto.UserCreateDTO;
import org.apache.commons.collections4.map.HashedMap;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.util.*;

public class KeycloakUtil {

    @Value("${keycloak.auth-server-url}")
    private static String authServerUrl;

    @Value("${keycloak.realm}")
    private static String realm;

    private static final String[] attributeArr = {UserOtherClaim.USER_NAME, UserOtherClaim.LOCALE};
    private static final String[] ignoreProperties = {"userId", "userEmail", "email", "useYn", "firstName", "lastName"};

    public static KeycloakSecurityContext getContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) authentication;
        @SuppressWarnings("unchecked")
        KeycloakPrincipal<KeycloakSecurityContext> keycloakPrincipal = (KeycloakPrincipal<KeycloakSecurityContext>) token.getPrincipal();
        return keycloakPrincipal.getKeycloakSecurityContext();
    }
    public static AccessToken getAccessToken() {
        KeycloakSecurityContext context = getContext();
        return context.getToken();
    }

    public static String getAccessTokenAttribute(AccessToken accessToken, String attributeName) {
        return String.valueOf(accessToken.getOtherClaims().get(attributeName));
    }


    public static Keycloak buildKeycloak() {
        return KeycloakBuilder.builder()
                .serverUrl(authServerUrl) //
                .realm("master")
                .username("admin")
                .password("1q2w3e4r5t!!Q")
                .clientId("dcon-master")
                .build();
    }
    public static UserResource getUserResource(String userId) {

        Keycloak keycloak = buildKeycloak();
        RealmResource realmResource = keycloak.realm(realm);
        return realmResource.users().get(userId);
    }

    public static UserDetailsDTO buildUserDetail(UserRepresentation user) throws IOException {
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

    private static void fetchUserAttribute(UserDetailsDTO userDetails, Map<String, List<String>> attribute) throws IOException {

        // TODO : attribute 와 키값과 userDetails의 필드명이 같은지 확인할 것
        // attribute 의 키값(snake)을 카멜로 변환하였을 때 userDetails 의 필드명과 일치해야 한다!!
        Map<String,String> userDetailsMap = new HashMap<>();

        if (!ObjectUtils.isEmpty(attribute)) {
            attribute.forEach((k,v) ->{
                if (!ObjectUtils.isEmpty(v)) {
                    if (Arrays.asList(attributeArr).contains(k)) {
                        userDetailsMap.put(StringUtil.snakeToCamel(k), v.get(0));
                    }
                }
            });

            ObjectMapper mapper = new ObjectMapper();
            UserDetailsDTO userDetailsDTO = mapper.readValue(mapper.writeValueAsString(userDetailsMap), UserDetailsDTO.class);

            BeanUtils.copyProperties(userDetails, userDetailsDTO, ignoreProperties);
        }
    }

    public static UsersResource getUsersResource() {
        Keycloak keycloak = buildKeycloak();
        RealmResource realmResource = keycloak.realm(realm);
        return realmResource.users();
    }

    public static CredentialRepresentation setCredential(String password) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        return credential;
    }

    public static UserRepresentation setInsertUserRepresentation(UserRepresentation createUser, UserCreateDTO user) {
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
}
