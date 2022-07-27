package de.uniregensburg.oauth.testing;

import java.io.IOException;
import java.net.http.HttpResponse;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestClientCredentialsGrant {

    /**
     * Test 11
     * Client fordert beim Authorization Server Access Token mit seinen Client Credentials an.
     * Client frägt Daten ohne Benutzerkontext unter Angabe des Access Tokens beim Resource Server an.
     * 
     * @result  Authorization Server stellt dem Client einen gültigen Access Token aus. Der Client erhält die Daten vom Resource Server.
     */
    @Test
    public void test11() throws IOException, InterruptedException {
        // initialize variables
        String clientId = "demo-client-credentials";
        String clientSecret = "secret2";
        String scope = "demo";
        String authTokenUrl = "http://auth-server:9000/oauth2/token";
        String demoResourceUrl = "http://resource-server:8090/demo";

        // get access token using clients credentials grant
        HttpResponse response = HttpUtil.doClientCredentialsGrant(authTokenUrl, clientId, clientSecret, scope);

        // extract access token from response
        String jsonString = response.body().toString();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonString);
        String accessToken = jsonNode.get("access_token").textValue();

        // get resource using access token
        response = HttpUtil.getResource(demoResourceUrl, accessToken);

        // get response text
        String actual = response.body().toString();
        // expected text
        String expected = "Demo works!";
        // validate
        Assertions.assertEquals(expected, actual);
    }

    /**
     * Test 12
     * Client fordert beim Authorization Server Access Token mit seinen Client Credentials an.
     * Die Client Credentials sind falsch.
     * 
     * @result  Authorization Server lehnt Anfrage ab und gibt dem Client die Fehlermeldung invalid_client zurück.
     */
    @Test
    public void test12() throws IOException, InterruptedException {
        // initialize variables
        String clientId = "demo-client-credentials";
        String clientSecret = "wrong-secret";
        String scope = "demo";
        String authTokenUrl = "http://auth-server:9000/oauth2/token";

        // get access token using clients credentials grant
        HttpResponse response = HttpUtil.doClientCredentialsGrant(authTokenUrl, clientId, clientSecret, scope);

        // extract error message from response
        String jsonString = response.body().toString();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonString);
        String actual = jsonNode.get("error").textValue();
        
        // expected error message
        String expected = "invalid_client";
        // validate
        Assertions.assertEquals(expected, actual);
    }

}
