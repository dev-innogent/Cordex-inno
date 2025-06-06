package com.springboot.Spring_B;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement//to enable transaction management so that every transaction will complete fully not half.
@SpringBootApplication
public class SpringBApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBApplication.class, args);
		System.out.println("Hello Niraj");
	}

}
