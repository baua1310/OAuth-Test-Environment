package de.uniregensburg.oauthclient.app;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebAppController {

	@GetMapping("/webapp")
	public String webapp() {
		return "webapp";
	}

}
