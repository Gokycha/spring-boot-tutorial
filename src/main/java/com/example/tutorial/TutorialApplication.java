package com.example.tutorial;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.tutorial"})
public class TutorialApplication {
	public static void main(String[] args) {
		SpringApplication.run(TutorialApplication.class, args);
	}
}
