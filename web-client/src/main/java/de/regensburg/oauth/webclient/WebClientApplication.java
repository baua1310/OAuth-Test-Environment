package de.regensburg.oauth.webclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebClientApplication {

    /** 
    * Start spring web client
    * 
    * @param    args
    */
	public static void main(String[] args) {
		SpringApplication.run(WebClientApplication.class, args);
	}

}
