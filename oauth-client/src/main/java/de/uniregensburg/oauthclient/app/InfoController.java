package de.uniregensburg.oauthclient.app;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import de.uniregensburg.oauthclient.model.Token;
import de.uniregensburg.oauthclient.repository.TokenRepository;
import de.uniregensburg.oauthclient.util.UserInfo;

@Controller
public class InfoController {
    @Autowired
    private TokenRepository tokenRepository;

	@GetMapping("/webapp/info")
	public String info(Model model, @CookieValue(value = "username", defaultValue = "") String username) {

		if (username.isBlank()) {
			return notLoggedIn(model);
		} else {
			model.addAttribute("loggedIn", true);
		}

		List<Token> tokens = tokenRepository.findByUsername(username);
		String accessToken = null;
		for (Token token : tokens) {
			if (!token.isExpired() && token.getScope().contains("profile")) {
				accessToken = token.getAccessToken();
			}
		}

		// TODO: no access token -> refresh possible?
		if (accessToken == null) {
			return notLoggedIn(model);
		}

		String name = UserInfo.getInstance().getName(accessToken);

        model.addAttribute("name", name);

		return "info";
	}

	private String notLoggedIn(Model model) {
		model.addAttribute("loggedIn", false);
		return "info";
	}

}
