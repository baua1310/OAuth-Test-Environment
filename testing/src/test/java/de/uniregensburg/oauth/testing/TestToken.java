package de.uniregensburg.oauth.testing;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestToken {

    @Test
    public void test19() throws IOException, InterruptedException {
        String demoResourceUrl = "http://resource-server:8090/demo";

        HttpResponse response = HttpUtil.getResource(demoResourceUrl);

        int actual = response.statusCode();
        int expected = 401;

        Assertions.assertEquals(expected, actual);
    }

    // Schlägt fehl. Token ist laut Token Introspection nicht mehr aktiv, kann aber trotzdem noch verwendet werden
    @Test
    public void test20() throws IOException, InterruptedException {
        String clientId = "demo-client-credentials";
        String clientSecret = "secret2";
        String scope = "demo";
        String authTokenUrl = "http://auth-server:9000/oauth2/token";
        String tokenRevocationUrl = "http://auth-server:9000/oauth2/revoke";
        String demoResourceUrl = "http://resource-server:8090/demo";

        HttpResponse response = HttpUtil.doClientCredentialsGrant(authTokenUrl, clientId, clientSecret, scope);
        String jsonString = response.body().toString();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonString);

        String accessToken = jsonNode.get("access_token").textValue();

        response = HttpUtil.revokeAccessToken(tokenRevocationUrl, accessToken, clientId, clientSecret);

        response = HttpUtil.getResource(demoResourceUrl, accessToken);
        
        Map headers = response.headers().map();
        String actual = headers.get("www-authenticate").toString();
        int start = actual.indexOf("\"");
        int end =  actual.indexOf("\"", start + 1);
        actual = actual.substring(start + 1, end);
        String expected = "invalid_token";

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void test22() throws IOException, InterruptedException {
        String clientId = "demo-client-credentials";
        String clientSecret = "secret2";
        String scope = "demo";
        String authTokenUrl = "http://auth-server:9000/oauth2/token";
        String demoResourceUrl = "http://resource-server:8090/demo";

        HttpResponse response = HttpUtil.doClientCredentialsGrant(authTokenUrl, clientId, clientSecret, scope);
        String jsonString = response.body().toString();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonString);

        String accessToken = jsonNode.get("access_token").textValue();

        response = HttpUtil.getResource(demoResourceUrl, accessToken);

        String actual = response.body().toString();
        String expected = "Demo works!";

        Assertions.assertEquals(expected, actual);
    }
}
