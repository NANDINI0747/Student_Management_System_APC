package com.example.studentservice.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "enrollment-service", url = "http://localhost:8082")
public interface EnrollmentClient {

    @DeleteMapping("/enrollments/student/{rollNo}")
    String deleteEnrollmentsByRollNo(@PathVariable("rollNo") String rollNo);
}
