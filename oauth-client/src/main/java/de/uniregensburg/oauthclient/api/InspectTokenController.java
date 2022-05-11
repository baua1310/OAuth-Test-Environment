package de.uniregensburg.oauthclient.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import de.uniregensburg.oauthclient.util.TokenInspector;

@RestController
public class InspectTokenController {

	@Value("${client_id}")
	private String clientId;
	@Value("${client_secret}")
	private String clientSecret;
	@Value("${introspection_endpoint}")
	private String introspectionEndpoint;

    @GetMapping(value = "/api/v1/inspect/{token}", produces = "application/json")
	public String inspect(@PathVariable String token) {

		return TokenInspector.getInstance().inspectAccessToken(token);

	}
}
