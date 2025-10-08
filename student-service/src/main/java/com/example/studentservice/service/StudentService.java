package com.example.studentservice.service;

import com.example.studentservice.exception.ExceptionConfig;
import com.example.studentservice.model.Student;
import com.example.studentservice.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service //Marks this class as a Spring service bean (part of service layer).
public class StudentService {

    private final StudentRepository repository;
    private final EnrollmentClient enrollmentClient;

    public StudentService(StudentRepository repository, EnrollmentClient enrollmentClient) {
        this.repository = repository;
        this.enrollmentClient = enrollmentClient;
    }

    public String addStudent(Student student) {
        if (repository.existsByRollNo(student.getRollNo())) {
            throw new ExceptionConfig.ConflictException(
                    "Student with roll number " + student.getRollNo() + " already exists.");
        }
        if (repository.existsByEmail(student.getEmail())) {
            throw new ExceptionConfig.ConflictException(
                    "Email " + student.getEmail() + " already in use.");
        }

        repository.addStudent(student);
        return "Student added successfully!";
    }

    public List<Student> getAllStudents() {
        return repository.getAllStudents();
    }

    public Student getStudentByRollNo(String rollNo) {
        Student s = repository.getStudentByRollNo(rollNo);
        if (s == null) {
            throw new ExceptionConfig.NotFoundException(
                    "Student not found with roll number " + rollNo);
        }
        return s;
    }

    public String updateStudent(String rollNo, Student updated) {
        if (!repository.existsByRollNo(rollNo)) {
            throw new ExceptionConfig.NotFoundException(
                    "Student not found with roll number " + rollNo);
        }

        String newEmail = updated.getEmail();
        if (newEmail != null && !newEmail.trim().isEmpty()) {
            Student byEmail = repository.getStudentByEmail(newEmail);
            if (byEmail != null && !byEmail.getRollNo().equals(rollNo)) {
                throw new ExceptionConfig.ConflictException(
                        "Email " + newEmail + " already exists for another student.");
            }
        }

        updated.setRollNo(rollNo);
        int updatedRows = repository.updateStudent(updated);
        if (updatedRows == 0) {
            throw new ExceptionConfig.NotFoundException(
                    "Student not found with roll number " + rollNo);
        }
        return "Student updated successfully!";
    }

    @Transactional  //The @Transactional ensures that if any part fails (DB delete or enrollment delete), the whole operation is rolled back.
    public String deleteStudent(String rollNo) {
        if (!repository.existsByRollNo(rollNo)) {
            throw new ExceptionConfig.NotFoundException(
                    "Student not found with roll number " + rollNo);
        }

        String enrollmentMsg;
        try {
            enrollmentMsg = enrollmentClient.deleteEnrollmentsByRollNo(rollNo);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to delete enrollments for student " + rollNo + ": " + ex.getMessage());
        }

        int deleted = repository.deleteStudent(rollNo);
        if (deleted == 0) {
            throw new ExceptionConfig.NotFoundException(
                    "Student not found with roll number " + rollNo);
        }

        return "Student deleted successfully! " + enrollmentMsg;
    }

    public boolean existsByEmail(String email) { return repository.existsByEmail(email); }
    public boolean existsByRollNo(String rollNo) { return repository.existsByRollNo(rollNo); }
}
