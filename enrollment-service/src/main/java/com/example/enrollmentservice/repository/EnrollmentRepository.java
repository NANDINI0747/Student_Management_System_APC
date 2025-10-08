package com.example.enrollmentservice.repository;

import com.example.enrollmentservice.model.Enrollment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;//RowMapper: Used to convert a row from SQL ResultSet → Enrollment object.
import org.springframework.stereotype.Repository;//Marks this class as a repository bean

import java.util.List;

@Repository
public class EnrollmentRepository {//Declares EnrollmentRepository as a Spring-managed bean that handles database operations for enrollments.

    private final JdbcTemplate jdbcTemplate;

    public EnrollmentRepository(JdbcTemplate jdbcTemplate) {//Constructor Injection
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Enrollment> rowMapper = (rs, rowNum) ->
            new Enrollment(
                    rs.getString("student_roll_no"),
                    rs.getString("student_name"),
                    rs.getString("course"),
                    rs.getInt("credit")
            );

    public int enrollStudent(Enrollment e) {
        return jdbcTemplate.update(
                "INSERT INTO enrollments (student_roll_no, student_name, course, credit) VALUES (?, ?, ?, ?)",
                e.getStudentRollNo(), e.getStudentName(), e.getCourse(), e.getCredit()
        );
    }

    public List<Enrollment> getAllEnrollments() {//Uses rowMapper to convert each row into an Enrollment.Returns a List<Enrollment>.
        return jdbcTemplate.query("SELECT * FROM enrollments", rowMapper);
    }

    public List<Enrollment> getByStudentRollNo(String rollNo) {
        return jdbcTemplate.query(
                "SELECT * FROM enrollments WHERE student_roll_no = ?",
                rowMapper, rollNo
        );
    }

    public int unenrollStudent(String rollNo, String course) {
        return jdbcTemplate.update(
                "DELETE FROM enrollments WHERE student_roll_no = ? AND course = ?",
                rollNo, course
        );
    }

    public boolean existsByRollNoAndCourse(String rollNo, String course) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM enrollments WHERE student_roll_no = ? AND course = ?",
                Integer.class, rollNo, course
        );
        return count != null && count > 0;
    }

    public int updateEnrollment(String rollNo, String oldCourse, String newCourse, int newCredit) {
        return jdbcTemplate.update(
                "UPDATE enrollments SET course=?, credit=? WHERE student_roll_no=? AND course=?",
                newCourse, newCredit, rollNo, oldCourse
        );
    }

    public int deleteEnrollmentsByRollNo(String rollNo) {
        return jdbcTemplate.update(
                "DELETE FROM enrollments WHERE student_roll_no = ?",
                rollNo
        );
    }
}
