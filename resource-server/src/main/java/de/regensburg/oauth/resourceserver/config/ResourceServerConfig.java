package de.regensburg.oauth.resourceserver.config;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/*
 * Source: https://github.com/Baeldung/spring-security-oauth/tree/master/oauth-authorization-server
 */

@EnableWebSecurity
public class ResourceServerConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.mvcMatcher("/demo/**")
          .authorizeRequests()
          .mvcMatchers("/demo/**")
          .access("hasAuthority('SCOPE_demo')")
          .and()
          .oauth2ResourceServer()
          .jwt();
        return http.build();
    }
}