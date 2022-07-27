package de.regensburg.oauth.resourceserver.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    /** 
    * Get demo resource
    * 
    * Source: https://github.com/Baeldung/spring-security-oauth/blob/master/oauth-authorization-server/resource-server/src/main/java/com/baeldung/web/ArticlesController.java
    * Source: https://github.com/spring-projects/spring-authorization-server/blob/main/samples/messages-resource/src/main/java/sample/web/MessagesController.java
    *
    * Modified: path, return type, returned content
    * 
    * @param    http
    * @return   security filter chain for demo path
    */
    @GetMapping("/demo")
    public String getDemo() {
        return "Demo works!";
    }
}