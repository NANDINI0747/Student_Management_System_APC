package com.example.enrollmentservice.controller;

import com.example.enrollmentservice.model.Enrollment;
import com.example.enrollmentservice.service.EnrollmentService;
import org.springframework.http.ResponseEntity;//ResponseEntity lets you control the HTTP response fully (status code, headers, body).
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController  //It combines @Controller and @ResponseBody
@RequestMapping("/enrollments")//sets the base URL path for all endpoints in this class.
public class EnrollmentController {

    private final EnrollmentService service;//Declares a field service to use the business logic methods from EnrollmentService.

    public EnrollmentController(EnrollmentService service){//Constructor Injection
        this.service = service;
    }
     //ResponseEntity<T> is a class in Spring Framework that represents the whole HTTP response.
     //If you return an object (like Enrollment), Spring will serialize it as JSON and always send 200 OK
    @PostMapping("/enroll")
    public ResponseEntity<String> enrollStudent(@RequestBody Enrollment e) {//Spring converts incoming JSON into an Enrollment object.
        return ResponseEntity.status(201).body(service.enrollStudent(e));//Calls the service method to handle the business logic
    }//Returns HTTP 201 Created

    @GetMapping("/all")
    public ResponseEntity<List<Enrollment>> getAllEnrollments() {
        return ResponseEntity.ok(service.getAllEnrollments());//Fetches all enrollments.
    }

    @GetMapping("/student/{rollNo}")
    public ResponseEntity<List<Enrollment>> getEnrollmentsByStudent(@PathVariable String rollNo) {////Extracts {rollNo} from the URL.
        return ResponseEntity.ok(service.getEnrollmentsByStudent(rollNo));//Returns JSON list of enrollments.
    }

    @PutMapping("/update/{rollNo}")
    public ResponseEntity<String> updateEnrollment(@PathVariable String rollNo,//extracts query parameters from URL
                                                   @RequestParam String oldCourse,
                                                   @RequestParam String newCourse,
                                                   @RequestParam int credit) {
        return ResponseEntity.ok(service.updateEnrollment(rollNo, oldCourse, newCourse, credit));
    }

    @DeleteMapping("/unenroll/{rollNo}")
    public ResponseEntity<String> unenrollStudent(@PathVariable String rollNo,
                                                  @RequestParam String course) {
        return ResponseEntity.ok(service.unenrollStudent(rollNo, course));
    }

    @DeleteMapping("/student/{rollNo}")
    public ResponseEntity<String> deleteEnrollmentsByRollNo(@PathVariable String rollNo) {
        return ResponseEntity.ok(service.deleteEnrollmentsByRollNo(rollNo));
    }
}


