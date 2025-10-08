package com.example.studentservice.controller;

import com.example.studentservice.model.Student;
import com.example.studentservice.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController //@RestController = @Controller + @ResponseBody — it tells Spring to return method results as HTTP response bodies (JSON/text).
@RequestMapping("/students") //@RequestMapping("/student")//sets the base URL path for all endpoints in this class.
public class StudentController {

    private final StudentService service; //declares a dependency on the service layer. final indicates it’s assigned once

    public StudentController(StudentService service) {  //constructor injection — Spring injects StudentService when creating this controller.
        this.service = service;
    }

    @PostMapping("/add") //maps HTTP POST requests to /students/add to this method.
    public ResponseEntity<String> addStudent(@RequestBody Student student) {  // @RequestBody Spring converts incoming JSON body into a Student object.
        return ResponseEntity.status(201).body(service.addStudent(student));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(service.getAllStudents());
    }

    @GetMapping("/roll/{rollNo}")
    public ResponseEntity<Student> getStudentByRoll(@PathVariable String rollNo) {  //@PathVariable String rollNo — extracts {rollNo} from the URL and passes it to the method.
        //The service returns a single Student which is returned as HTTP 200.
        //Used to fetch one student by their roll number from the service.
        return ResponseEntity.ok(service.getStudentByRollNo(rollNo));
    }

    @PutMapping("/update/{rollNo}")
    public ResponseEntity<String> updateStudent(@PathVariable String rollNo,
                                                @RequestBody Student updated) {
        return ResponseEntity.ok(service.updateStudent(rollNo, updated));
    }

    @DeleteMapping("/delete/{rollNo}")
    public ResponseEntity<String> deleteStudent(@PathVariable String rollNo) {
        return ResponseEntity.ok(service.deleteStudent(rollNo));
    }
}
//Key Idea: JPA allows you to work with Java objects (entities) instead of writing raw SQL queries. It handles the mapping between Java classes and database tables automatically.