package com.example.enrollmentservice.service;

import com.example.enrollmentservice.exception.ExceptionConfig;
import com.example.enrollmentservice.model.Enrollment;
import com.example.enrollmentservice.model.Student;
import com.example.enrollmentservice.repository.EnrollmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service//Marks this as a Spring service
public class EnrollmentService {

    private final EnrollmentRepository repository;//Accesses the database for enrollments
    private final StudentClient studentClient;//Calls another microservice or client to fetch student details.

    public EnrollmentService(EnrollmentRepository repository, StudentClient studentClient) {
        this.repository = repository;
        this.studentClient = studentClient;//Injects dependencies via constructor (constructor injection).
    }

    public String enrollStudent(Enrollment e) {
        Student student;
        try {
            student = studentClient.getStudent(e.getStudentRollNo());
        } catch (Exception ex) {
            throw new ExceptionConfig.NotFoundException(
                    "No student exists with roll number " + e.getStudentRollNo());
        }

        if (student == null || student.getRollNo() == null) {
            throw new ExceptionConfig.NotFoundException(
                    "No student exists with roll number " + e.getStudentRollNo());
        }

        // Validate course name
        if (!e.getCourse().matches("^[A-Za-z ]+$")) {
            throw new ExceptionConfig.ConflictException("Course name must not contain numbers!");
        }

        if (repository.existsByRollNoAndCourse(e.getStudentRollNo(), e.getCourse())) {
            throw new ExceptionConfig.ConflictException(
                    "Enrollment already exists for student in course " + e.getCourse());
        }

        e.setStudentName(student.getName());
        repository.enrollStudent(e);
        return "Student enrolled successfully!";
    }

    public List<Enrollment> getAllEnrollments() {
        return repository.getAllEnrollments();
    }

    public List<Enrollment> getEnrollmentsByStudent(String rollNo) {
        List<Enrollment> enrollments = repository.getByStudentRollNo(rollNo);
        if (enrollments.isEmpty()) {
            throw new ExceptionConfig.NotFoundException(
                    "No enrollment exists for student with roll number " + rollNo);
        }
        return enrollments;
    }

    public String unenrollStudent(String rollNo, String course) {
        int deleted = repository.unenrollStudent(rollNo, course);
        if (deleted == 0) {
            throw new ExceptionConfig.NotFoundException(
                    "Enrollment not found for roll number " + rollNo + " in course " + course);
        }
        return "Student unenrolled from course!";
    }

    public String updateEnrollment(String rollNo, String oldCourse, String newCourse, int newCredit) {
        // Validate course name
        if (!newCourse.matches("^[A-Za-z ]+$")) {
            throw new ExceptionConfig.ConflictException("Course name must not contain numbers!");
        }

        int updated = repository.updateEnrollment(rollNo, oldCourse, newCourse, newCredit);
        if (updated == 0) {
            throw new ExceptionConfig.NotFoundException(
                    "Enrollment not found for update (rollNo=" + rollNo + ", course=" + oldCourse + ")");
        }
        return "Enrollment updated successfully!";
    }

    public String deleteEnrollmentsByRollNo(String rollNo) {
        List<Enrollment> enrollments = repository.getByStudentRollNo(rollNo);
        if (enrollments.isEmpty()) {
            return "No enrollments found for student " + rollNo + ".";
        }
        repository.deleteEnrollmentsByRollNo(rollNo);
        return enrollments.size() + " enrollment(s) deleted for student " + rollNo;
    }
}
