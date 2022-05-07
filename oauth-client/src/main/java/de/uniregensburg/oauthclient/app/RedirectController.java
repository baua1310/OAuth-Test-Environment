package de.uniregensburg.oauthclient.app;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedirectController {

    @GetMapping("/webapp/redirect")
    public String redirect() {
        return "Redirect endpoint reached";
    }

}
