package de.uniregensburg.oauth.testing;

import java.io.IOException;
import java.net.http.HttpResponse;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestTokenIntrospection {

    @Test
    public void test24() throws IOException, InterruptedException {
        String clientId = "demo-client-credentials";
        String clientSecret = "secret2";
        String scope = "demo";
        String authTokenUrl = "http://auth-server:9000/oauth2/token";
        String tokenIntrospectionUrl = "http://auth-server:9000/oauth2/introspect";

        HttpResponse response = HttpUtil.doClientCredentialsGrant(authTokenUrl, clientId, clientSecret, scope);
        String jsonString = response.body().toString();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonString);

        String accessToken = jsonNode.get("access_token").textValue();

        response = HttpUtil.inspectToken(tokenIntrospectionUrl, accessToken, clientId, clientSecret);

        jsonString = response.body().toString();
        objectMapper = new ObjectMapper();
        jsonNode = objectMapper.readTree(jsonString);
        boolean actual = jsonNode.get("active").booleanValue();
        boolean expected = true;

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void test25() throws IOException, InterruptedException {
        String clientId = "demo-client-credentials";
        String clientSecret = "secret2";
        String scope = "demo";
        String authTokenUrl = "http://auth-server:9000/oauth2/token";
        String tokenRevocationUrl = "http://auth-server:9000/oauth2/revoke";
        String tokenIntrospectionUrl = "http://auth-server:9000/oauth2/introspect";

        HttpResponse response = HttpUtil.doClientCredentialsGrant(authTokenUrl, clientId, clientSecret, scope);
        String jsonString = response.body().toString();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonString);

        String accessToken = jsonNode.get("access_token").textValue();

        response = HttpUtil.revokeAccessToken(tokenRevocationUrl, accessToken, clientId, clientSecret);

        response = HttpUtil.inspectToken(tokenIntrospectionUrl, accessToken, clientId, clientSecret);

        jsonString = response.body().toString();
        objectMapper = new ObjectMapper();
        jsonNode = objectMapper.readTree(jsonString);
        boolean actual = jsonNode.get("active").booleanValue();
        boolean expected = false;

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void test26() throws IOException, InterruptedException {
        String clientId = "demo-client-credentials";
        String clientSecret = "secret2";
        String scope = "demo";
        String authTokenUrl = "http://auth-server:9000/oauth2/token";
        String tokenRevocationUrl = "http://auth-server:9000/oauth2/revoke";
        String tokenIntrospectionUrl = "http://auth-server:9000/oauth2/introspect";

        HttpResponse response = HttpUtil.doClientCredentialsGrant(authTokenUrl, clientId, clientSecret, scope);
        String jsonString = response.body().toString();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonString);

        String accessToken = jsonNode.get("access_token").textValue();

        response = HttpUtil.revokeAccessToken(tokenRevocationUrl, accessToken, clientId, clientSecret);

        Thread.sleep((5 * 60 * 1000) + 3 * 1000); // Token Lifetime: 5 Minuten + 3 Sekunden puffer
        response = HttpUtil.inspectToken(tokenIntrospectionUrl, accessToken, clientId, clientSecret);

        jsonString = response.body().toString();
        objectMapper = new ObjectMapper();
        jsonNode = objectMapper.readTree(jsonString);
        boolean actual = jsonNode.get("active").booleanValue();
        boolean expected = false;

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void test27() throws IOException, InterruptedException {
        String clientId = "demo-client-credentials";
        String clientSecret = "secret2";
        String scope = "demo";
        String authTokenUrl = "http://auth-server:9000/oauth2/token";
        String tokenIntrospectionUrl = "http://auth-server:9000/oauth2/introspect";

        HttpResponse response = HttpUtil.doClientCredentialsGrant(authTokenUrl, clientId, clientSecret, scope);
        String jsonString = response.body().toString();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonString);

        String accessToken = jsonNode.get("access_token").textValue();

        Thread.sleep((5 * 60 * 1000) + 3 * 1000); // Token Lifetime: 5 Minuten + 3 Sekunden puffer
        response = HttpUtil.inspectToken(tokenIntrospectionUrl, accessToken, clientId, clientSecret);

        jsonString = response.body().toString();
        objectMapper = new ObjectMapper();
        jsonNode = objectMapper.readTree(jsonString);
        boolean actual = jsonNode.get("active").booleanValue();
        boolean expected = false;

        Assertions.assertEquals(expected, actual);
    }

}
