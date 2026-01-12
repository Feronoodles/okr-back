package com.example.okr;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@OpenAPIDefinition(servers = {@io.swagger.v3.oas.annotations.servers.Server(url = "/", description = "Default Server URL"), @io.swagger.v3.oas.annotations.servers.Server(url = "/myapp", description = "OKR Server URL")})
public class OkrApplication {

	public static void main(String[] args) {
		SpringApplication.run(OkrApplication.class, args);
	}

}
