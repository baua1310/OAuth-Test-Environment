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
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import de.uniregensburg.oauthclient.model.Token;
import de.uniregensburg.oauthclient.repository.TokenRepository;

@Service
public class AccessTokenReceiver {
	private static AccessTokenReceiver instance;

	@Value("${client_id}")
	private String clientId;
	@Value("${client_secret}")
	private String clientSecret;
    @Value("${token_endpoint}")
    private String tokenEndpoint;
    @Value("${redirect_uri}")
    private String redirectUri;
    @Autowired
    private TokenRepository tokenRepository;

	@PostConstruct
	public void init() {
		instance = this;
	}

	public static AccessTokenReceiver getInstance() {
		return instance;
	}

    public Token getAccessToken(String code, String codeVerifier) {
        Map<String, String> parameters = new HashMap<>();
		parameters.put("client_id", clientId);
		parameters.put("client_secret", clientSecret);
		parameters.put("code", code);
        parameters.put("redirect_uri", redirectUri);
        parameters.put("code_verifier", codeVerifier);
        parameters.put("grant_type", "authorization_code");


		String form = parameters.entrySet()
			.stream()
			.map(e -> e.getKey() + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
			.collect(Collectors.joining("&"));

		HttpClient client = HttpClient.newHttpClient();

		HttpRequest postRequest = HttpRequest.newBuilder()
			.uri(URI.create(tokenEndpoint))
			.headers("Content-Type", "application/x-www-form-urlencoded")
			.POST(HttpRequest.BodyPublishers.ofString(form))
			.build();

		HttpResponse<?> response = null;
		try {
			response = client.send(postRequest, HttpResponse.BodyHandlers.ofString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        String jsonString = response.body().toString();
        Token token = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            token = objectMapper.readValue(jsonString, Token.class);
            String username = TokenInspector.getInstance().getUsername(token);
            token.setUsername(username);
            tokenRepository.save(token);
        } catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return token;
    }

}
