package com.example.restApi;

import org.springframework.boot.SpringApplication;

public class TestRestApiApplication {

	public static void main(String[] args) {
		SpringApplication.from(RestApiApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
