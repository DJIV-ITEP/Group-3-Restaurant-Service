package com.itep.restaurant_service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.JdbcConnectionDetails;
import org.springframework.context.annotation.Bean;

import static org.yaml.snakeyaml.nodes.Tag.STR;

@SpringBootApplication
public class RestaurantServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestaurantServiceApplication.class, args);
	}

//	@Bean
//	CommandLineRunner commandLineRunner(JdbcConnectionDetails jdbc){
//		return args -> {
//
//			System.out.println("Class:"+jdbc.getClass().getName());
//			System.out.println("JDBC URL:"+jdbc.getJdbcUrl());
//			System.out.println("username URL:"+jdbc.getUsername());
//			System.out.println("password URL:"+jdbc.getPassword());
//		};
//	}

}
