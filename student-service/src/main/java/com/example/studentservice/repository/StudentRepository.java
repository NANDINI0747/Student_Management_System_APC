package com.example.studentservice.repository;

import com.example.studentservice.model.Student;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StudentRepository {

    private final JdbcTemplate jdbcTemplate;

    public StudentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private RowMapper<Student> rowMapper = (rs, rowNum) ->
            new Student(
                    rs.getString("roll_no"),
                    rs.getString("name"),
                    rs.getString("email")
            );

    // Check if email already exists
    public boolean existsByEmail(String email) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM students WHERE email = ?",
                Integer.class, email);
        return count != null && count > 0;
    }

    // Check if roll number already exists
    public boolean existsByRollNo(String rollNo) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM students WHERE roll_no = ?",
                Integer.class, rollNo);
        return count != null && count > 0;
    }

    // Insert new student
    public int addStudent(Student student) {
        return jdbcTemplate.update(
                "INSERT INTO students (roll_no, name, email) VALUES (?,?,?)",
                student.getRollNo(), student.getName(), student.getEmail()
        );
    }

    // Get student by roll number (returns null if not found)
    public Student getStudentByRollNo(String rollNo) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT * FROM students WHERE roll_no=?",
                    rowMapper, rollNo);
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    // Get all students
    public List<Student> getAllStudents() {
        return jdbcTemplate.query("SELECT * FROM students", rowMapper);
    }

    // Update student details using roll_no
    public int updateStudent(Student student) {
        return jdbcTemplate.update(
                "UPDATE students SET name=?, email=? WHERE roll_no=?",
                student.getName(), student.getEmail(), student.getRollNo()
        );
    }

    // Delete student by roll number
    public int deleteStudent(String rollNo) {
        return jdbcTemplate.update("DELETE FROM students WHERE roll_no=?", rollNo);
    }

    // Get student by email (returns null if not found)
    public Student getStudentByEmail(String email) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT * FROM students WHERE email = ?",
                    rowMapper, email);
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }
}
