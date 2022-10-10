package com.etz.authorisationserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication
@EnableJpaAuditing
public class AuthorisationServerApplication  {

	public static void main(String[] args) {

		SpringApplication.run(AuthorisationServerApplication.class, args);
	}


}
