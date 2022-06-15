package kr.co.dcon.taskserver.common.util;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class TokenUtil {

	public static KeycloakSecurityContext getContext() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) authentication;
		@SuppressWarnings("unchecked")
		KeycloakPrincipal<KeycloakSecurityContext> keycloakPrincipal = (KeycloakPrincipal<KeycloakSecurityContext>)token.getPrincipal();
        return keycloakPrincipal.getKeycloakSecurityContext();
	}
	
	public static AccessToken getAccessToken() {
		return getContext().getToken();
		
	}
	
}
