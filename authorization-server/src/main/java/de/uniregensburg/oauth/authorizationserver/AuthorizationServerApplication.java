package de.uniregensburg.oauth.authorizationserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AuthorizationServerApplication {

	/**
	* Start Spring Authorization Server
	*
	* @param  args  
	*/
	public static void main(String[] args) {
		SpringApplication.run(AuthorizationServerApplication.class, args);
	}

}
