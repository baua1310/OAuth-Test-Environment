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

public class HttpUtil {
    protected static HttpResponse getResource(String demoResourceUrl, String accessToken) throws IOException, InterruptedException {
        HttpRequest request = getGetRequest(demoResourceUrl, "Authorization", "Bearer " + accessToken);
        return doHttpRequest(request);
    }


    protected static HttpResponse getResource(String demoResourceUrl) throws IOException, InterruptedException {
        HttpRequest request = getGetRequest(demoResourceUrl);
        return doHttpRequest(request);
    }

    protected static HttpResponse doClientCredentialsGrant(String authTokenUrl, String clientId, String clientSecret, String scope) throws IOException, InterruptedException {
        Map<String, String> parameters = new HashMap<>();
		parameters.put("client_id", clientId);
		parameters.put("client_secret", clientSecret);
        parameters.put("grant_type", "client_credentials");
        parameters.put("scope", scope);

        HttpRequest request = getPostRequest(authTokenUrl, parameters);
        return doHttpRequest(request);
    }

    public static HttpResponse revokeAccessToken(String tokenRevocationUrl, String accessToken, String clientId, String clientSecret) throws IOException, InterruptedException {
        Map<String, String> parameters = new HashMap<>();
		parameters.put("client_id", clientId);
		parameters.put("client_secret", clientSecret);
        parameters.put("token", accessToken);

        HttpRequest request = getPostRequest(tokenRevocationUrl, parameters);
        return doHttpRequest(request);
    }

    public static HttpResponse inspectToken(String tokenIntrospectionUrl, String accessToken, String clientId, String clientSecret) throws IOException, InterruptedException {
        Map<String, String> parameters = new HashMap<>();
		parameters.put("client_id", clientId);
		parameters.put("client_secret", clientSecret);
        parameters.put("token", accessToken);

        HttpRequest request = getPostRequest(tokenIntrospectionUrl, parameters);
        return doHttpRequest(request);
    }

    private static HttpRequest getPostRequest(String url, Map<String, String> form) {

        return HttpRequest.newBuilder()
			.uri(URI.create(url))
			.headers("Content-Type", "application/x-www-form-urlencoded")
			.POST(HttpRequest.BodyPublishers.ofString(convertHashMapFormToString(form)))
			.build();
    }

    private static HttpRequest getGetRequest(String url) {

        return HttpRequest.newBuilder()
			.uri(URI.create(url))
			.GET()
			.build();
    }

    private static HttpRequest getGetRequest(String url, String... header) {

        return HttpRequest.newBuilder()
			.uri(URI.create(url))
			.headers(header)
			.GET()
			.build();
    }

    private static String convertHashMapFormToString(Map<String, String> form) {
        return form.entrySet()
			.stream()
			.map(e -> e.getKey() + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
			.collect(Collectors.joining("&"));
    }

    private static HttpResponse doHttpRequest(HttpRequest request) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
	    return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

}
