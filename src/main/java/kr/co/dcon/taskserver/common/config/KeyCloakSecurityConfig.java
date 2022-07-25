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


    private RestTemplateBuilder restBuilder;

    public KeyCloakSecurityConfig(RestTemplateBuilder restBuilder){
        this.restBuilder = restBuilder;
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) {
        KeycloakAuthenticationProvider keycloakAuthenticationProvider = keycloakAuthenticationProvider();
        keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
        auth.authenticationProvider(keycloakAuthenticationProvider);
    }

    @Bean
    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
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
                .and().authorizeRequests().antMatchers("/api/v1/mail/**").permitAll()
                .and().authorizeRequests().antMatchers("/api/v1/front-profile").permitAll()
                .and().authorizeRequests().antMatchers("/api/v1/pay/payment").permitAll()
                .and().authorizeRequests().antMatchers("/api/v1/pay/paymentResult").permitAll()
                .and().authorizeRequests().antMatchers("/api/v1/bill/billPdf/**").permitAll()
                .and().authorizeRequests().antMatchers("/api/v1/user/cardAuth").permitAll() // check manually in the UserDetailContrller
                .and().authorizeRequests().antMatchers("/api/v1/user/cardAuthResult").permitAll() // check manually in the UserDetailContrller
                .and().authorizeRequests().antMatchers("/api/v1/product/downloadProductFile/**").permitAll() // check manually in the ProductContrller
                .and().authorizeRequests().antMatchers("/favicon.ico").permitAll()
                .and().authorizeRequests().antMatchers("/css/*").permitAll()
                .and().authorizeRequests().antMatchers("/image/*").permitAll()
                .and().authorizeRequests().antMatchers("/js/*").permitAll()
                .and().authorizeRequests().antMatchers("/").permitAll()
                .and().authorizeRequests().antMatchers("/health").permitAll()
//                .and().authorizeRequests().antMatchers("/api/v1/**").permitAll()
//                .and().authorizeRequests().antMatchers("/*/api/v1/**").permitAll()
                .anyRequest().authenticated();

    }

  @Bean
  public KeycloakConfigResolver KeycloakConfigResolver() {
    return new KeycloakSpringBootConfigResolver();
  }
}
