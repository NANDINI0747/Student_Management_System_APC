package com.example.enrollmentservice.service;

import com.example.enrollmentservice.model.Student;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "student-service", url = "http://localhost:8081")//Marks this interface as a Feign client.
public interface StudentClient {
    @GetMapping("/students/roll/{rollNo}")
    Student getStudent(@PathVariable("rollNo") String rollNo);
}
//“Fetches a student by roll number from Student service.”