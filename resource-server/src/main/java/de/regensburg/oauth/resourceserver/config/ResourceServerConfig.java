package de.regensburg.oauth.resourceserver.config;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class ResourceServerConfig {

    /** 
    * 
    * 
    * Source: https://github.com/Baeldung/spring-security-oauth/blob/master/oauth-authorization-server/resource-server/src/main/java/com/baeldung/config/ResourceServerConfig.java
    * Source: https://github.com/spring-projects/spring-authorization-server/blob/main/samples/messages-resource/src/main/java/sample/config/ResourceServerConfig.java
    *
    * Modified: path and scope
    * 
    * @param    http
    * @return   security filter chain for demo path
    */
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