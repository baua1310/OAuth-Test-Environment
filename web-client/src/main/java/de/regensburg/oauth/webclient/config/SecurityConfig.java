package de.regensburg.oauth.webclient.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebSecurity
public class SecurityConfig {

    /** 
    * Security filter chain any request. Set redirect to login page.
    * 
    * Source: https://github.com/Baeldung/spring-security-oauth/blob/master/oauth-authorization-server/client-server/src/main/java/com/baeldung/config/SecurityConfig.java
    * Source: https://github.com/spring-projects/spring-authorization-server/blob/main/samples/messages-client/src/main/java/sample/config/SecurityConfig.java
    *
    * Modified: redirect uri
    * 
    * @param    http
    * @return   security filter chain
    */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeRequests(authorizeRequests ->
                authorizeRequests.anyRequest().authenticated()
            )
            .oauth2Login(oauth2Login ->
                oauth2Login.loginPage("/oauth2/authorization/demo-client-oidc"))
            .oauth2Client(withDefaults());
        return http.build();
    }
}