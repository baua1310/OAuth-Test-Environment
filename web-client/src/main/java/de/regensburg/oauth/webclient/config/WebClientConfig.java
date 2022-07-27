package de.regensburg.oauth.webclient.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    /** 
    * Initialize web client with authorized client manager
    * 
    * Source: https://github.com/Baeldung/spring-security-oauth/blob/master/oauth-authorization-server/client-server/src/main/java/com/baeldung/config/WebClientConfig.java
    * Source: https://github.com/spring-projects/spring-authorization-server/blob/main/samples/messages-client/src/main/java/sample/config/WebClientConfig.java
    * 
    * @param    OAuth2AuthorizedClientManager
    * @return   web client
    */
    @Bean
    WebClient webClient(OAuth2AuthorizedClientManager authorizedClientManager) {
        ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2Client =
            new ServletOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);
        return WebClient.builder()
            .apply(oauth2Client.oauth2Configuration())
            .build();
    }

    /** 
    * Initialize authorized client manager: use authorization code and refresh token
    * 
    * Source: https://github.com/Baeldung/spring-security-oauth/blob/master/oauth-authorization-server/client-server/src/main/java/com/baeldung/config/WebClientConfig.java
    * Source: https://github.com/spring-projects/spring-authorization-server/blob/main/samples/messages-client/src/main/java/sample/config/WebClientConfig.java
    * 
    * @param    ClientRegistrationRepository
    * @param    OAuth2AuthorizedClientRepository
    * @return   authorized client manager
    */
    @Bean
    OAuth2AuthorizedClientManager authorizedClientManager(ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientRepository authorizedClientRepository) {

        OAuth2AuthorizedClientProvider authorizedClientProvider =
            OAuth2AuthorizedClientProviderBuilder.builder()
                .authorizationCode()
                .refreshToken()
                .build();
        DefaultOAuth2AuthorizedClientManager authorizedClientManager = new DefaultOAuth2AuthorizedClientManager(
                clientRegistrationRepository, authorizedClientRepository);
        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);

        return authorizedClientManager;
    }
}