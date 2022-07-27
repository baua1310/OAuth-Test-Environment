package de.uniregensburg.oauth.testing;

import java.io.IOException;
import java.net.http.HttpResponse;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestTokenIntrospection {

    /**
     * Test 24
     * Resource Server überprüft validen und noch nicht abgelaufenen Access Token beim Token Introspection Endpoint.
     * 
     * @result  Introspection Endpoint antwortet Token ist aktiv.
     */
    @Test
    public void test24() throws IOException, InterruptedException {
        // initialize variables
        String clientId = "demo-client-credentials";
        String clientSecret = "secret2";
        String scope = "demo";
        String authTokenUrl = "http://auth-server:9000/oauth2/token";
        String tokenIntrospectionUrl = "http://auth-server:9000/oauth2/introspect";

        // get access token using clients credentials grant
        HttpResponse response = HttpUtil.doClientCredentialsGrant(authTokenUrl, clientId, clientSecret, scope);

        // extract access token from response
        String jsonString = response.body().toString();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonString);
        String accessToken = jsonNode.get("access_token").textValue();

        // inspect access token
        response = HttpUtil.inspectToken(tokenIntrospectionUrl, accessToken, clientId, clientSecret);

        // get active variable
        jsonString = response.body().toString();
        objectMapper = new ObjectMapper();
        jsonNode = objectMapper.readTree(jsonString);
        boolean actual = jsonNode.get("active").booleanValue();
        // expected active variable
        boolean expected = true;
        // validate
        Assertions.assertEquals(expected, actual);
    }

    /**
     * Test 25
     * Resource Server überprüft invaliden und noch nicht abgelaufenen Access Token beim Token Introspection Endpoint.
     * 
     * @result  Introspection Endpoint antwortet Token ist nicht aktiv.
     */
    @Test
    public void test25() throws IOException, InterruptedException {
        // initialize variables
        String clientId = "demo-client-credentials";
        String clientSecret = "secret2";
        String scope = "demo";
        String authTokenUrl = "http://auth-server:9000/oauth2/token";
        String tokenRevocationUrl = "http://auth-server:9000/oauth2/revoke";
        String tokenIntrospectionUrl = "http://auth-server:9000/oauth2/introspect";

        // get access token using clients credentials grant
        HttpResponse response = HttpUtil.doClientCredentialsGrant(authTokenUrl, clientId, clientSecret, scope);

        // extract access token from response
        String jsonString = response.body().toString();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonString);
        String accessToken = jsonNode.get("access_token").textValue();

        // revoke access token
        response = HttpUtil.revokeAccessToken(tokenRevocationUrl, accessToken, clientId, clientSecret);

        // inspect access token
        response = HttpUtil.inspectToken(tokenIntrospectionUrl, accessToken, clientId, clientSecret);

        // get active variable
        jsonString = response.body().toString();
        objectMapper = new ObjectMapper();
        jsonNode = objectMapper.readTree(jsonString);
        boolean actual = jsonNode.get("active").booleanValue();
        // expected active variable
        boolean expected = false;
        // validate
        Assertions.assertEquals(expected, actual);
    }

    /**
     * Test 26
     * Resource Server überprüft invaliden und abgelaufenen Access Token beim Token Introspection Endpoint.
     *
     * @result  Introspection Endpoint antwortet Token ist nicht aktiv.
     */
    @Test
    public void test26() throws IOException, InterruptedException {
        // initialize variables
        String clientId = "demo-client-credentials";
        String clientSecret = "secret2";
        String scope = "demo";
        String authTokenUrl = "http://auth-server:9000/oauth2/token";
        String tokenRevocationUrl = "http://auth-server:9000/oauth2/revoke";
        String tokenIntrospectionUrl = "http://auth-server:9000/oauth2/introspect";

        // get access token using clients credentials grant
        HttpResponse response = HttpUtil.doClientCredentialsGrant(authTokenUrl, clientId, clientSecret, scope);

        // extract access token from response
        String jsonString = response.body().toString();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonString);
        String accessToken = jsonNode.get("access_token").textValue();

        // revoke access token
        response = HttpUtil.revokeAccessToken(tokenRevocationUrl, accessToken, clientId, clientSecret);

        // let access token expire (Token lifetime is 5 minutes)
        // sleep 5 minutes and 3 seconds
        Thread.sleep((5 * 60 * 1000) + 3 * 1000);

        // inspect access token
        response = HttpUtil.inspectToken(tokenIntrospectionUrl, accessToken, clientId, clientSecret);

        // get active variable
        jsonString = response.body().toString();
        objectMapper = new ObjectMapper();
        jsonNode = objectMapper.readTree(jsonString);
        boolean actual = jsonNode.get("active").booleanValue();
        // expected active variable
        boolean expected = false;
        // validate
        Assertions.assertEquals(expected, actual);
    }

    /**
     * Test 27
     * Resource Server überprüft validen und abgelaufenen Access Token beim Token Introspection Endpoint.
     * 
     * @result  Introspection Endpoint antwortet Token ist nicht aktiv.
     */
    @Test
    public void test27() throws IOException, InterruptedException {
        // initialize variables
        String clientId = "demo-client-credentials";
        String clientSecret = "secret2";
        String scope = "demo";
        String authTokenUrl = "http://auth-server:9000/oauth2/token";
        String tokenIntrospectionUrl = "http://auth-server:9000/oauth2/introspect";

        // get access token using clients credentials grant
        HttpResponse response = HttpUtil.doClientCredentialsGrant(authTokenUrl, clientId, clientSecret, scope);

        // extract access token from response
        String jsonString = response.body().toString();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonString);
        String accessToken = jsonNode.get("access_token").textValue();

        // let access token expire (Token lifetime is 5 minutes)
        // sleep 5 minutes and 3 seconds
        Thread.sleep((5 * 60 * 1000) + 3 * 1000);

        // inspect access token
        response = HttpUtil.inspectToken(tokenIntrospectionUrl, accessToken, clientId, clientSecret);

        // get active variable
        jsonString = response.body().toString();
        objectMapper = new ObjectMapper();
        jsonNode = objectMapper.readTree(jsonString);
        boolean actual = jsonNode.get("active").booleanValue();
        // expected active variable
        boolean expected = false;
        // validate
        Assertions.assertEquals(expected, actual);
    }

}
