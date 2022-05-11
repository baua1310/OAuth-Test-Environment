package de.uniregensburg.oauthclient.util;

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

import javax.annotation.PostConstruct;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import de.uniregensburg.oauthclient.model.Token;

@Service
public class TokenInspector {
	private static TokenInspector instance;

	@Value("${client_id}")
	private String clientId;
	@Value("${client_secret}")
	private String clientSecret;
	@Value("${introspection_endpoint}")
	private String introspectionEndpoint;

	@PostConstruct
	public void init() {
		instance = this;
	}

	public static TokenInspector getInstance() {
		return instance;
	}

    public String inspectAccessToken(String accessToken) {
        Map<String, String> parameters = new HashMap<>();
		parameters.put("client_id", clientId);
		parameters.put("client_secret", clientSecret);
		parameters.put("token", accessToken);

		String form = parameters.entrySet()
			.stream()
			.map(e -> e.getKey() + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
			.collect(Collectors.joining("&"));

		HttpClient client = HttpClient.newHttpClient();

		HttpRequest request = HttpRequest.newBuilder()
			.uri(URI.create(introspectionEndpoint))
			.headers("Content-Type", "application/x-www-form-urlencoded")
			.POST(HttpRequest.BodyPublishers.ofString(form))
			.build();

		HttpResponse<?> response = null;
		try {
			response = client.send(request, HttpResponse.BodyHandlers.ofString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        return response.body().toString();
    }
    
    public String getUsername(Token token) {

        String accessToken = token.getAccessToken();
        String jsonString = inspectAccessToken(accessToken);

        String username = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonString);
            username = jsonNode.get("username").textValue();
        } catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		
		return username;
    }

}
