package com.springboot.Spring_B;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Application entry point. Enables transaction management for all database
 * operations.
 */
@EnableTransactionManagement
@SpringBootApplication
public class SpringBApplication {

    /**
     * Bootstraps the Spring application.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(SpringBApplication.class, args);
        System.out.println("Hello Niraj");
    }

}
