package de.uniregensburg.oauth.testing;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestToken {

    /**
     * Test 19
     * Client verwendet keinen Access Token um geschützte Daten beim Resource Server anzufragen.
     * 
     * @result  Resource Server lehnt Anfrage ab und gibt dem Client den HTTP Status 401 zurück.
     */
    @Test
    public void test19() throws IOException, InterruptedException {
        // initialize variables
        String demoResourceUrl = "http://resource-server:8090/demo";

        // get resource without using access token
        HttpResponse response = HttpUtil.getResource(demoResourceUrl);

        // get response status
        int actual = response.statusCode();
        // expected response status
        int expected = 401;
        // validate
        Assertions.assertEquals(expected, actual);
    }

    /**
     * Test 20
     * Resource Owner entzieht Client Zugriff beim Authorization Server.
     * Client frägt Daten beim Resource Owners beim Resource Server an.
     *
     * Test currently fails: according to token introspection is access token no longer active but can still be used to access resources.
     * 
     * @result  Resource Server lehnt Anfrage ab und gibt dem Client die Fehlermeldung invalid_token zurück.
     */
    @Test
    public void test20() throws IOException, InterruptedException {
        // initialize variables
        String clientId = "demo-client-credentials";
        String clientSecret = "secret2";
        String scope = "demo";
        String authTokenUrl = "http://auth-server:9000/oauth2/token";
        String tokenRevocationUrl = "http://auth-server:9000/oauth2/revoke";
        String demoResourceUrl = "http://resource-server:8090/demo";

        // get access token using clients credentials grant
        HttpResponse response = HttpUtil.doClientCredentialsGrant(authTokenUrl, clientId, clientSecret, scope);
        
        // extract access token from response
        String jsonString = response.body().toString();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonString);
        String accessToken = jsonNode.get("access_token").textValue();

        // revoke access token
        response = HttpUtil.revokeAccessToken(tokenRevocationUrl, accessToken, clientId, clientSecret);

        // get resource using access token
        response = HttpUtil.getResource(demoResourceUrl, accessToken);
        
        // get error message
        Map headers = response.headers().map();
        String actual = headers.get("www-authenticate").toString();
        int start = actual.indexOf("\"");
        int end =  actual.indexOf("\"", start + 1);
        actual = actual.substring(start + 1, end);
        // expected error message
        String expected = "invalid_token";
        // validate
        Assertions.assertEquals(expected, actual);
    }

    /**
     * Test 22
     * Client verwendet validen und noch nicht abgelaufenen Access Token um Daten beim Resource Servers anzufordern.
     * Der benötigte Scope für die Daten ist vorhanden.
     * 
     * @result  Client erhält die Daten vom Resource Server.
     */
    @Test
    public void test22() throws IOException, InterruptedException {
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
}
