package com.example.enrollmentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;//Marks this as a Spring Boot app,
import org.springframework.cloud.openfeign.EnableFeignClients;//Enables Spring Cloud OpenFeign

@SpringBootApplication
@EnableFeignClients
public class EnrollmentServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(EnrollmentServiceApplication.class, args);
    }
}
