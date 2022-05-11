package de.uniregensburg.oauthclient.api;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import de.uniregensburg.oauthclient.model.State;
import de.uniregensburg.oauthclient.model.Token;
import de.uniregensburg.oauthclient.repository.StateRepository;
import de.uniregensburg.oauthclient.util.AccessTokenReceiver;
import de.uniregensburg.oauthclient.util.TokenInspector;

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
    public RedirectView redirect(HttpServletRequest request, HttpServletResponse response) {
        String error = request.getParameter("error");
        if (error != null) {
            return error(request);
        }
        String code = request.getParameter("code");
        if (code != null) {
            return code(request, response);
        }
        return new RedirectView("error");
    }

    private RedirectView code(HttpServletRequest request, HttpServletResponse response) {
        String code = request.getParameter("code");
        long stateId = Long.parseLong(request.getParameter("state"));

        State state = stateRepository.findById(stateId);
        stateRepository.delete(state);

        String codeVerifier = state.getCodeVerifier();

        Token token = AccessTokenReceiver.getInstance().getAccessToken(code, codeVerifier);

        String username = TokenInspector.getInstance().getUsername(token);
        Cookie cookie = new Cookie("username", username);
        response.addCookie(cookie);

        String redirectTo = state.getRedirectUri();
		
		return new RedirectView(redirectTo);
    }

    private RedirectView error(HttpServletRequest request) {
        String error = request.getParameter("error");
        System.out.println(error);
        String errorDescription = request.getParameter("error_description");
        if (errorDescription != null) {
            System.out.println(errorDescription);
        }
        return new RedirectView("error");
    }

}
