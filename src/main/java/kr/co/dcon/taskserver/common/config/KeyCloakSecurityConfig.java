package kr.co.dcon.taskserver.common.config;

import lombok.extern.slf4j.Slf4j;
import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Slf4j
@KeycloakConfiguration
@EnableSwagger2
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class KeyCloakSecurityConfig extends KeycloakWebSecurityConfigurerAdapter {


    @Autowired
    private RestTemplateBuilder restBuilder;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) {
        KeycloakAuthenticationProvider keycloakAuthenticationProvider = keycloakAuthenticationProvider();
        keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
        auth.authenticationProvider(keycloakAuthenticationProvider);
        //auth.authenticationProvider(keycloakAuthenticationProvider());
    }

    @Bean
    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
       // return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
      //return new KeycloakRemoteInvalidUserSessionManager(restBuilder);
          return new NullAuthenticatedSessionStrategy();
    }

    private static final String[] AUTH_WHITELIST = {
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v2/api-docs",
            "/webjars/**",
            "/api/v1/whitelabel/*",
            "/saml/**",
            "/heath"
    };

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(AUTH_WHITELIST);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        super.configure(http);
        http.headers().frameOptions().disable();
        http.cors().and().csrf().disable().authorizeRequests()
                //http.cors().and().authorizeRequests()
                .antMatchers("/*").permitAll()
                .and().authorizeRequests().antMatchers("/api/v1/**").permitAll()
                .and().authorizeRequests().antMatchers("/*/api/v1/**").permitAll()
                .anyRequest().authenticated();

    }

  @Bean
  public KeycloakConfigResolver KeycloakConfigResolver() {
    return new KeycloakSpringBootConfigResolver();
  }
   /* @Value("${front.env}")
    String strServerType;

    @Bean
    public KeycloakConfigResolver keycloakConfigResolver() {
        return new KeycloakConfigResolver() {
            private KeycloakDeployment keycloakDeployment;

            @Override
            public KeycloakDeployment resolve(HttpFacade.Request facade) {
                if (keycloakDeployment != null) {
                    return keycloakDeployment;
                }

                String strKeyCloackJson = "";
                if ("PROD".equals(strServerType)) {
                    strKeyCloackJson = "/keycloak_prod.json";
                } else if ("STAGE".equals(strServerType)) {
                    strKeyCloackJson = "/keycloak_stg.json";
                } else if ("DEV".equals(strServerType)) {
                    strKeyCloackJson = "/keycloak_dev.json";
                } else {
                    strKeyCloackJson = "/keycloak.json";
                }


                // log.info("[strKeyCloackJson] strServerType = " + strServerType);
                // log.info("[strKeyCloackJson] strKeyCloackJson = " + strKeyCloackJson);

                InputStream configInputStream = getClass().getResourceAsStream(strKeyCloackJson);
                return KeycloakDeploymentBuilder.build(configInputStream);
            }
        };
    }*/
}
