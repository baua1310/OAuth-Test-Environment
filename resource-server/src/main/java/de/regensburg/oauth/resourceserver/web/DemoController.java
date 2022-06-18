package de.regensburg.oauth.resourceserver.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * Source: https://github.com/Baeldung/spring-security-oauth/tree/master/oauth-authorization-server
 */

@RestController
public class DemoController {

    @GetMapping("/demo")
    public String getDemo() {
        return "Demo works!";
    }
}