package de.uniregensburg.oauthclient.api;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedirectController {

    @GetMapping("/webapp/redirect")
    public String redirect(HttpServletRequest request) {
        String state = request.getParameter("state");
        String error = request.getParameter("error");
        if (error != null) {
            System.out.println(error);
            String errorDescription = request.getParameter("error_description");
            if (errorDescription != null) {
                System.out.println(errorDescription);
            }
            return "Error";
        }
        String code = request.getParameter("code");
        System.out.println(code);
        return "code: " + code + " state: " + state;
    }

}
