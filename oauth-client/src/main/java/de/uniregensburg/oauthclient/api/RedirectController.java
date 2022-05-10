package de.uniregensburg.oauthclient.api;

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

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import de.uniregensburg.oauthclient.model.State;
import de.uniregensburg.oauthclient.repository.StateRepository;

@RestController
public class RedirectController {

    @Value("${client_id}")
    private String clientId;
    @Value("${client_secret}")
    private String clientSecret;
    @Value("${token_endpoint}")
    private String tokenEndpoint;
    @Value("${redirect_uri}")
    private String redirectUri;
    @Autowired
    private StateRepository stateRepository;

    @GetMapping("/webapp/redirect")
    public String redirect(HttpServletRequest request) {
        String state = request.getParameter("state");
        String error = request.getParameter("error");
        if (error != null) {
            return error(request);
        }
        String code = request.getParameter("code");
        if (code != null) {
            return code(request);
        }
        return "";
    }

    private String code(HttpServletRequest request) {
        String code = request.getParameter("code");
        long stateId = Long.parseLong(request.getParameter("state"));

        State state = stateRepository.findById(stateId);
        stateRepository.delete(state);

        // TODO: get code_verifier
        String codeVerifier = state.getCodeVerifier();

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
        
        if (response == null) {
			return "Response Error";
		}
		
		return response.body().toString();
        
    }

    private String error(HttpServletRequest request) {
        String error = request.getParameter("error");
        System.out.println(error);
        String errorDescription = request.getParameter("error_description");
        if (errorDescription != null) {
            System.out.println(errorDescription);
        }
        return "Error";
    }

}
