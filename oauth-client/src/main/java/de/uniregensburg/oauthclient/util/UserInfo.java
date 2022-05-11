package de.uniregensburg.oauthclient.util;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import javax.annotation.PostConstruct;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UserInfo {
	private static UserInfo instance;

	@Value("${client_id}")
	private String clientId;
	@Value("${client_secret}")
	private String clientSecret;
	@Value("${introspection_endpoint}")
	private String introspectionEndpoint;
    String userInfoUrl = "https://dev-69056236.okta.com/oauth2/default/v1/userinfo";

	@PostConstruct
	public void init() {
		instance = this;
	}

	public static UserInfo getInstance() {
		return instance;
	}
        
    public String getUserInfo(String accesstoken) {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(userInfoUrl))
            .headers("Authorization", "Bearer " + accesstoken)
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

    public String getName(String accesstoken) {
        String jsonString = getUserInfo(accesstoken);
        
        String name = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonString);
            name = jsonNode.get("name").textValue();
        } catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return name;
    }
}
