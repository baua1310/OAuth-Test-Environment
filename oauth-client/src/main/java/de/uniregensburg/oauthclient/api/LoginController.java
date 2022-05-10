package de.uniregensburg.oauthclient.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import de.uniregensburg.oauthclient.model.State;
import de.uniregensburg.oauthclient.repository.StateRepository;
import de.uniregensburg.oauthclient.util.CodeChallengeMethod;
import de.uniregensburg.oauthclient.util.PKCE;
import de.uniregensburg.oauthclient.util.PKCEException;
import de.uniregensburg.oauthclient.util.ResponseType;

@RestController
public class LoginController {

	@Value("${client_id}")
	private String clientId;
	@Value("${client_secret}")
	private String clientSecret;
	@Value("${authorization_endpoint}")
	private String authorizationEndpoint;
    @Value("${redirect_uri}")
    private String redirectUri;
    @Autowired
    private StateRepository stateRepository;

    @GetMapping("/webapp/login")
    public RedirectView login(RedirectAttributes attributes) {
        String responseType = ResponseType.CODE.toString(); // TODO
        String codeChallengeMethod = CodeChallengeMethod.S256.toString();
        State state = new State("http://localhost:8080/login"); // TODO 
        String scope = "photos"; // TODO

        String codeChallenge = null;
        try {
            String codeVerifier = PKCE.getCodeVerifier();
            state.setCodeVerifier(codeVerifier);
            stateRepository.save(state);
            codeChallenge = PKCE.generateCodeChallenge(codeVerifier);
        } catch (PKCEException e) {
            // TODO Redirect to error page
            e.printStackTrace();
        }

        attributes.addAttribute("response_type", responseType);
        attributes.addAttribute("client_id", clientId);
        attributes.addAttribute("redirect_uri", redirectUri);
        attributes.addAttribute("scope", scope);
        attributes.addAttribute("state", state.getId().toString());
        attributes.addAttribute("code_challenge", codeChallenge);
        attributes.addAttribute("code_challenge_method", codeChallengeMethod);
        return new RedirectView(authorizationEndpoint);

    }

}
