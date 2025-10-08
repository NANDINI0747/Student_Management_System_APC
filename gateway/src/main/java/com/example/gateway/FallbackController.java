package com.example.gateway;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class FallbackController {

    @GetMapping("/studentServiceFallback")
    public String studentServiceFallback() {
        return "Student Service is currently unavailable. Please try again later!";
    }

    @GetMapping("/enrollmentServiceFallback")
    public String enrollmentServiceFallback() {
        return "Enrollment Service is currently unavailable. Please try again later!";
    }
}

