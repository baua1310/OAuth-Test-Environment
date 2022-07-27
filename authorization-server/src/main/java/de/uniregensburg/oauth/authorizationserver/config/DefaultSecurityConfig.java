package de.uniregensburg.oauth.authorizationserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebSecurity
public class DefaultSecurityConfig {

    /**
     * Apply deault security filter chain: any request must be authenticated
     * 
     * Source: https://github.com/Baeldung/spring-security-oauth/blob/master/oauth-authorization-server/spring-authorization-server/src/main/java/com/baeldung/config/DefaultSecurityConfig.java
     *
     * @param   http
     * @return  default security filter chain
     */
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests(authorizeRequests ->
            authorizeRequests.anyRequest().authenticated()
        )
        .formLogin(withDefaults());
        return http.build();
    }

    /**
     * Add user
     *
     * Source: https://github.com/Baeldung/spring-security-oauth/blob/master/oauth-authorization-server/spring-authorization-server/src/main/java/com/baeldung/config/DefaultSecurityConfig.java
     * Source: https://docs.spring.io/spring-authorization-server/docs/current/reference/html/getting-started.html
     *
     * Modified: username
     *
     * @return  user details service with oauth user
     */
    @Bean
    UserDetailsService users() {
        UserDetails user = User.withDefaultPasswordEncoder()
            .username("user")
            .password("password")
            .roles("USER")
            .build();
        return new InMemoryUserDetailsManager(user);
    }
}
