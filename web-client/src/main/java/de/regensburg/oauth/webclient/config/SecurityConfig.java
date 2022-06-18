package de.regensburg.oauth.webclient.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

/*
 * Source: https://github.com/Baeldung/spring-security-oauth/tree/master/oauth-authorization-server
 */

@EnableWebSecurity
public class SecurityConfig {

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