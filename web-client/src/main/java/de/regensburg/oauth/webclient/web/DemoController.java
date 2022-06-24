package de.regensburg.oauth.webclient.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;

/*
 * Source: https://github.com/Baeldung/spring-security-oauth/tree/master/oauth-authorization-server
 */

@RestController
public class DemoController {

    @Autowired
    private WebClient webClient;

    @GetMapping(value = "/demo")
    public String getDemo(@RegisteredOAuth2AuthorizedClient("demo-client-authorization-code") OAuth2AuthorizedClient authorizedClient) {
        return this.webClient
            .get()
            .uri("http://resource-server:8090/demo")
            .attributes(oauth2AuthorizedClient(authorizedClient))
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }
}