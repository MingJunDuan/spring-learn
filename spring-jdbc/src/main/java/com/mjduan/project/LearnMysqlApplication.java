package com.mjduan.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource(locations = {"classpath:jdbcConfig/jdbc.xml"})
public class LearnMysqlApplication {

	public static void main(String[] args) {
		SpringApplication.run(LearnMysqlApplication.class, args);
	}
}
