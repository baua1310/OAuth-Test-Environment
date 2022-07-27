package de.uniregensburg.oauth.authorizationserver.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.ClientSettings;
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;
import org.springframework.security.web.SecurityFilterChain;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

@Configuration
public class AuthorizationServerConfig {

    /**
     * apply default authorization server security filter chain 
     *
     * Source: https://github.com/Baeldung/spring-security-oauth/blob/master/oauth-authorization-server/spring-authorization-server/src/main/java/com/baeldung/config/AuthorizationServerConfig.java
     *
     * @param   http
     * @return  authorization server security filter chain
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authServerSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        return http.formLogin(Customizer.withDefaults()).build();
    }

    /**
     * Register clients 
     *
     * Source: https://github.com/Baeldung/spring-security-oauth/blob/master/oauth-authorization-server/spring-authorization-server/src/main/java/com/baeldung/config/AuthorizationServerConfig.java
     * Source: https://docs.spring.io/spring-authorization-server/docs/current/reference/html/getting-started.html
     * 
     * Modified: client id, scope, redirect uri
     * Added: second client
     * 
     * @return  registered client repository with oauth clients
     */
    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
            .clientId("demo-client")
            .clientSecret("{noop}secret")
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
            .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
            .redirectUri("http://web-client:8080/login/oauth2/code/demo-client-oidc")
            .redirectUri("http://web-client:8080/authorized")
            .scope(OidcScopes.OPENID)
            .scope("demo")
            .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
            .build();

        RegisteredClient registeredClient2 = RegisteredClient.withId(UUID.randomUUID().toString())
            .clientId("demo-client-credentials")
            .clientSecret("{noop}secret2")
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
            .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
            .scope("demo")
            .build();

        return new InMemoryRegisteredClientRepository(registeredClient, registeredClient2);
    }

    /**
     * 
     * 
     * Source: https://github.com/Baeldung/spring-security-oauth/blob/master/oauth-authorization-server/spring-authorization-server/src/main/java/com/baeldung/config/AuthorizationServerConfig.java
     * 
     * @return  json web key source
     */
    @Bean
	public JWKSource<SecurityContext> jwkSource() {
        RSAKey rsaKey = generateRsa();
        JWKSet jwkSet = new JWKSet(rsaKey);
		return new ImmutableJWKSet<>(jwkSet);
	}

    /**
     *
     * 
     * Source: https://github.com/Baeldung/spring-security-oauth/blob/master/oauth-authorization-server/spring-authorization-server/src/main/java/com/baeldung/config/AuthorizationServerConfig.java
     * 
     * @return  rsa key
     */
    private static RSAKey generateRsa() {
        KeyPair keyPair = generateRsaKey();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        return new RSAKey.Builder(publicKey)
            .privateKey(privateKey)
            .keyID(UUID.randomUUID().toString())
            .build();
    }

    /**
     *
     * 
     * Source: https://github.com/Baeldung/spring-security-oauth/blob/master/oauth-authorization-server/spring-authorization-server/src/main/java/com/baeldung/config/AuthorizationServerConfig.java
     * Source: https://docs.spring.io/spring-authorization-server/docs/current/reference/html/getting-started.html
     * 
     * @return  rsa key pair
     */
    private static KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }

    /**
     *
     * 
     * Source: https://github.com/Baeldung/spring-security-oauth/blob/master/oauth-authorization-server/spring-authorization-server/src/main/java/com/baeldung/config/AuthorizationServerConfig.java
     * 
     * @return  provider settings including one oauth resource server
     */
    @Bean
	public ProviderSettings providerSettings() {
		return ProviderSettings.builder()
        .issuer("http://auth-server:9000")
        .build();
	}
}
