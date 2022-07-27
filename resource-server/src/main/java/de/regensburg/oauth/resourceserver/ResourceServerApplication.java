package de.regensburg.oauth.resourceserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ResourceServerApplication {

	/**
	* Start spring resource server
	*
	* @param  args  
	*/
	public static void main(String[] args) {
		SpringApplication.run(ResourceServerApplication.class, args);
	}

}
