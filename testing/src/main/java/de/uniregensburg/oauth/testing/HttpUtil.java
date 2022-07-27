package de.uniregensburg.oauth.testing;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Utility to perform web calls with http client
 */
public class HttpUtil {

    /**
     * Get resource based at resource url and authenticate with bearer token
     *
     * @param   resourceUrl
     * @param   accessToken
     * @return  http response
     */
    public static HttpResponse getResource(String resourceUrl, String accessToken) throws IOException, InterruptedException {
        HttpRequest request = getGetRequest(resourceUrl, "Authorization", "Bearer " + accessToken);
        return doHttpRequest(request);
    }

    /**
     * Get resource based at resource url and without authenticating
     *
     * @param   resourceUrl
     * @return  http response
     */
    public static HttpResponse getResource(String resourceUrl) throws IOException, InterruptedException {
        HttpRequest request = getGetRequest(resourceUrl);
        return doHttpRequest(request);
    }

    /**
     * Perform client credential grant to get access token with definied scope with auth token url using client id and client secret
     *
     * @param   authTokenUrl
     * @param   clientId
     * @param   clientSecret
     * @param   scope
     * @return  http response
     */
    public static HttpResponse doClientCredentialsGrant(String authTokenUrl, String clientId, String clientSecret, String scope) throws IOException, InterruptedException {
        Map<String, String> parameters = new HashMap<>();
		parameters.put("client_id", clientId);
		parameters.put("client_secret", clientSecret);
        parameters.put("grant_type", "client_credentials");
        parameters.put("scope", scope);

        HttpRequest request = getPostRequest(authTokenUrl, parameters);
        return doHttpRequest(request);
    }

    /**
     * Revoke access token at token revocation url using client id and client secret
     *
     * @param   tokenRevocationUrl
     * @param   accessToken
     * @param   clientId
     * @param   clientSecret
     * @return  http response
     */
    public static HttpResponse revokeAccessToken(String tokenRevocationUrl, String accessToken, String clientId, String clientSecret) throws IOException, InterruptedException {
        Map<String, String> parameters = new HashMap<>();
		parameters.put("client_id", clientId);
		parameters.put("client_secret", clientSecret);
        parameters.put("token", accessToken);

        HttpRequest request = getPostRequest(tokenRevocationUrl, parameters);
        return doHttpRequest(request);
    }

    /**
     * Inspect access token at token introspection url using client id and client secret
     *
     * @param   tokenIntrospectionUrl
     * @param   accessToken
     * @param   clientId
     * @param   clientSecret
     * @return  http response
     */
    public static HttpResponse inspectToken(String tokenIntrospectionUrl, String accessToken, String clientId, String clientSecret) throws IOException, InterruptedException {
        Map<String, String> parameters = new HashMap<>();
		parameters.put("client_id", clientId);
		parameters.put("client_secret", clientSecret);
        parameters.put("token", accessToken);

        HttpRequest request = getPostRequest(tokenIntrospectionUrl, parameters);
        return doHttpRequest(request);
    }

    /**
     * Create post request using url and form data
     *
     * @param   url
     * @param   form
     * @return  post request
     */
    private static HttpRequest getPostRequest(String url, Map<String, String> form) {

        return HttpRequest.newBuilder()
			.uri(URI.create(url))
			.headers("Content-Type", "application/x-www-form-urlencoded")
			.POST(HttpRequest.BodyPublishers.ofString(convertHashMapFormToString(form)))
			.build();
    }

    /**
     * Create get request using url
     *
     * @param   url
     * @return  get request
     */
    private static HttpRequest getGetRequest(String url) {

        return HttpRequest.newBuilder()
			.uri(URI.create(url))
			.GET()
			.build();
    }

    /**
     * Create get request using url and additional header information
     *
     * @param   url
     * @param   header
     * @return  get request
     */
    private static HttpRequest getGetRequest(String url, String... header) {

        return HttpRequest.newBuilder()
			.uri(URI.create(url))
			.headers(header)
			.GET()
			.build();
    }

    /**
     * Convert hash map to form urlencoded string
     *
     * @param   form
     * @return  form urlencoded string
     */
    private static String convertHashMapFormToString(Map<String, String> form) {
        return form.entrySet()
			.stream()
			.map(e -> e.getKey() + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
			.collect(Collectors.joining("&"));
    }

    /**
     * Perform http request, handle body as string
     *
     * @param   request
     * @return  http response
     */
    private static HttpResponse doHttpRequest(HttpRequest request) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
	    return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

}
