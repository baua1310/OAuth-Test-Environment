package de.uniregensburg.oauth.testing;

import java.io.IOException;
import java.net.http.HttpResponse;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestClientCredentialsGrant {

    @Test
    public void test11() throws IOException, InterruptedException {
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

    @Test
    public void test12() throws IOException, InterruptedException {
        String clientId = "demo-client-credentials";
        String clientSecret = "wrong-secret";
        String scope = "demo";
        String authTokenUrl = "http://auth-server:9000/oauth2/token";

        HttpResponse response = HttpUtil.doClientCredentialsGrant(authTokenUrl, clientId, clientSecret, scope);
        String jsonString = response.body().toString();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonString);
        String actual = jsonNode.get("error").textValue();

        String expected = "invalid_client";

        Assertions.assertEquals(expected, actual);
    }

}
