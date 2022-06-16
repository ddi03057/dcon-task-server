package kr.co.dcon.taskserver.common.config;

import lombok.Data;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.spi.UserSessionManagement;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.List;


@Component
@Scope
public class KeycloakRemoteInvalidUserSessionManager implements UserSessionManagement, SessionAuthenticationStrategy{

	private  RestTemplate restTemplate;

	public KeycloakRemoteInvalidUserSessionManager(RestTemplateBuilder restTemplateBuilder) {
		RestTemplate restTemplate = restTemplateBuilder.setConnectTimeout(Duration.ofMillis(2000))
				.setReadTimeout(Duration.ofMillis(1000)).build();
		this.restTemplate = restTemplate;
	}



	@Value("${keycloak.auth-server-url}")
	private String authServerUrl;

	private final String sessionCheckUrl = "/realms/%s/opsops/valid-session/%s";

	@Override
	public void logoutAll() {
		// not support  logoutAll
	}

	@Override
	public void logoutHttpSessions(List<String> ids) {
		// not support logoutHttpsessions
		// keycloak set the invalid session id to  redis
	}

	@Override
	public void onAuthentication(Authentication authentication, HttpServletRequest request,
			HttpServletResponse response) throws SessionAuthenticationException {
		KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) authentication;
		@SuppressWarnings("unchecked")
		KeycloakPrincipal<KeycloakSecurityContext> keycloakPrincipal = (KeycloakPrincipal<KeycloakSecurityContext>)token.getPrincipal();
        KeycloakSecurityContext context = keycloakPrincipal.getKeycloakSecurityContext();
        String realm = context.getRealm();
        String sessionId = context.getToken().getSessionState();

        String requestUrl =String.format(new StringBuilder()
        		.append(authServerUrl)
        		.append(sessionCheckUrl).toString(),
        			realm,
        			sessionId
        		);
        SessionState sessionState = restTemplate.getForEntity(requestUrl, SessionState.class).getBody();

        if (sessionState != null && sessionState.inactive) {
        	throw new SessionAuthenticationException("force logout session sessionId: [" + sessionId + "]");
		}
	}

}

@Data
class  SessionState {
	boolean inactive;
}
