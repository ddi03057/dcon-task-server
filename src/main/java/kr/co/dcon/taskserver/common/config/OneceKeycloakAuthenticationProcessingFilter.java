package kr.co.dcon.taskserver.common.config;

import org.keycloak.adapters.springsecurity.filter.KeycloakAuthenticationProcessingFilter;
import org.springframework.security.authentication.AuthenticationManager;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

public class OneceKeycloakAuthenticationProcessingFilter
    extends KeycloakAuthenticationProcessingFilter {
  private static final String FILTER_APPLIED =
      "__spring_security_keycloak_processfilter_filterApplied";


  public OneceKeycloakAuthenticationProcessingFilter(AuthenticationManager authenticationManager) {
    super(authenticationManager);
  }


  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
      throws IOException, ServletException {

    if (req.getAttribute(FILTER_APPLIED) != null) {
      chain.doFilter(req, res);
      return;
    }

    req.setAttribute(FILTER_APPLIED, true);
    super.doFilter(req, res, chain);

  }


}
