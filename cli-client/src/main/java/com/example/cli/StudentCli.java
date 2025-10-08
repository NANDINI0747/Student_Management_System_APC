package com.example.cli;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.fluent.Content;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.client5.http.HttpResponseException;
import org.apache.hc.core5.http.ContentType;

import java.util.*;

public class StudentCli {
    private static final String BASE_URL = "http://localhost:8080/api";
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n==== STUDENT & ENROLLMENT CLI ====");
            System.out.println("1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. View Specific Student");
            System.out.println("4. Update Student Details");
            System.out.println("5. Delete Student");
            System.out.println("6. Enroll Student in a Course");
            System.out.println("7. View All Enrollments");
            System.out.println("8. View Specific Student Enrollments");
            System.out.println("9. Remove Enrollment");
            System.out.println("10. Update Enrollment");
            System.out.println("11. Exit");
            System.out.print("Choice: ");
            int ch = sc.nextInt(); sc.nextLine();

            try {
                switch (ch) {
                    case 1 -> addStudent();
                    case 2 -> viewAllStudents();
                    case 3 -> viewStudent();
                    case 4 -> updateStudent();
                    case 5 -> deleteStudent();
                    case 6 -> enrollStudent();
                    case 7 -> viewAllEnrollments();
                    case 8 -> viewStudentEnrollments();
                    case 9 -> removeEnrollment();
                    case 10 -> updateEnrollment();
                    case 11 -> { System.out.println("Exiting..."); return; }
                    default -> System.out.println("Invalid choice");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    // ---------------- STUDENT METHODS ----------------
    private static void addStudent() {
        try {
            System.out.print("RollNo: ");
            String roll = sc.nextLine();
            System.out.print("Name: ");
            String name = sc.nextLine();
            System.out.print("Email: ");
            String email = sc.nextLine();

            Map<String, Object> student = new HashMap<>();
            student.put("rollNo", roll);
            student.put("name", name);
            student.put("email", email);

            String json = mapper.writeValueAsString(student);
            Content res = Request.post(BASE_URL + "/students/add")
                    .bodyString(json, ContentType.APPLICATION_JSON)
                    .execute().returnContent();
            System.out.println(res.asString());
        } catch (HttpResponseException e) {
            if (e.getStatusCode() == 409) {
                System.out.println("Error: Student with this roll number or email already exists!");
            } else {
                System.out.println("Error adding student: " + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("Error adding student: " + e.getMessage());
        }
    }

    private static void viewAllStudents() {
        try {
            Content res = Request.get(BASE_URL + "/students/all").execute().returnContent();
            List<Map<String, Object>> students = mapper.readValue(res.asString(), new TypeReference<>() {});
            if (students.isEmpty()) {
                System.out.println("No students found!");
            } else {
                printStudentsTable(students);
            }
        } catch (Exception e) {
            System.out.println("Error fetching students: " + e.getMessage());
        }
    }

    private static void viewStudent() {
        try {
            System.out.print("RollNo: ");
            String roll = sc.nextLine();
            Content res = Request.get(BASE_URL + "/students/roll/" + roll).execute().returnContent();
            System.out.println(res.asString());
        } catch (HttpResponseException e) {
            if (e.getStatusCode() == 404) {
                System.out.println("Error: Student with this roll number does not exist!");
            } else {
                System.out.println("Error fetching student: " + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("Error fetching student: " + e.getMessage());
        }
    }

    private static void updateStudent() {
        try {
            System.out.print("RollNo to update: ");
            String roll = sc.nextLine();
            System.out.print("New Name: ");
            String name = sc.nextLine();
            System.out.print("New Email: ");
            String email = sc.nextLine();

            Map<String, Object> updatedStudent = new HashMap<>();
            updatedStudent.put("name", name);
            updatedStudent.put("email", email);

            String json = mapper.writeValueAsString(updatedStudent);
            Content res = Request.put(BASE_URL + "/students/update/" + roll)
                    .bodyString(json, ContentType.APPLICATION_JSON)
                    .execute().returnContent();
            System.out.println(res.asString());
        } catch (HttpResponseException e) {
            if (e.getStatusCode() == 404) {
                System.out.println("Error: Student with this roll number does not exist!");
            } else if (e.getStatusCode() == 409) {
                System.out.println("Error: Email already exists for another student!");
            } else {
                System.out.println("Error updating student: " + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("Error updating student: " + e.getMessage());
        }
    }

    private static void deleteStudent() {
        try {
            System.out.print("RollNo to delete: ");
            String roll = sc.nextLine();
            Content res = Request.delete(BASE_URL + "/students/delete/" + roll)
                    .execute().returnContent();
            System.out.println(res.asString());
        } catch (HttpResponseException e) {
            if (e.getStatusCode() == 404) {
                System.out.println("Error: Student with this roll number does not exist!");
            } else {
                System.out.println("Error deleting student: " + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("Error deleting student: " + e.getMessage());
        }
    }

    // ---------------- ENROLLMENT METHODS ----------------
    private static void enrollStudent() {
        try {
            System.out.print("Student RollNo: ");
            String roll = sc.nextLine();

            if (!studentExists(roll)) {
                System.out.println("Error: Student does not exist!");
                return;
            }

            System.out.print("Course: ");
            String course = sc.nextLine();

            // Validation: Reject numeric course names
            if (!course.matches("^[A-Za-z ]+$")) {
                System.out.println("Error: Course name must not contain numbers!");
                return;
            }

            System.out.print("Credit: ");
            int credit = sc.nextInt(); sc.nextLine();

            Map<String, Object> enrollment = new HashMap<>();
            enrollment.put("studentRollNo", roll);
            enrollment.put("course", course);
            enrollment.put("credit", credit);

            String json = mapper.writeValueAsString(enrollment);
            Content res = Request.post(BASE_URL + "/enrollments/enroll")
                    .bodyString(json, ContentType.APPLICATION_JSON)
                    .execute().returnContent();
            System.out.println(res.asString());
        } catch (HttpResponseException e) {
            if (e.getStatusCode() == 409) {
                System.out.println("Error: Student already enrolled in this course!");
            } else {
                System.out.println("Error enrolling student: " + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("Error enrolling student: " + e.getMessage());
        }
    }

    private static void viewAllEnrollments() {
        try {
            Content res = Request.get(BASE_URL + "/enrollments/all").execute().returnContent();
            List<Map<String, Object>> enrollments = mapper.readValue(res.asString(), new TypeReference<>() {});
            if (enrollments.isEmpty()) {
                System.out.println("No enrollments found!");
            } else {
                printEnrollmentsTable(enrollments);
            }
        } catch (Exception e) {
            System.out.println("Error fetching enrollments: " + e.getMessage());
        }
    }

    private static void viewStudentEnrollments() {
        try {
            System.out.print("Student RollNo: ");
            String roll = sc.nextLine();

            if (!studentExists(roll)) {
                System.out.println("Error: Student does not exist!");
                return;
            }

            Content res = Request.get(BASE_URL + "/enrollments/student/" + roll)
                    .execute().returnContent();
            List<Map<String, Object>> enrollments = mapper.readValue(res.asString(), new TypeReference<>() {});
            if (enrollments.isEmpty()) {
                System.out.println("No enrollments exist for this student!");
            } else {
                printEnrollmentsTable(enrollments);
            }
        } catch (HttpResponseException e) {
            if (e.getStatusCode() == 404) {
                System.out.println("Error: Student does not exist!");
            } else {
                System.out.println("Error fetching enrollments: " + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("Error fetching enrollments: " + e.getMessage());
        }
    }

    private static void removeEnrollment() {
        try {
            System.out.print("Student RollNo: ");
            String roll = sc.nextLine();

            if (!studentExists(roll)) {
                System.out.println("Error: Student does not exist!");
                return;
            }

            System.out.print("Course to unenroll: ");
            String course = sc.nextLine();

            String url = String.format("%s/enrollments/unenroll/%s?course=%s", BASE_URL, roll, course);
            Content res = Request.delete(url).execute().returnContent();
            System.out.println(res.asString());
        } catch (HttpResponseException e) {
            if (e.getStatusCode() == 404) {
                System.out.println("Error: Enrollment not found!");
            } else {
                System.out.println("Error removing enrollment: " + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("Error removing enrollment: " + e.getMessage());
        }
    }

    private static void updateEnrollment() {
        try {
            System.out.print("Student RollNo: ");
            String roll = sc.nextLine();

            if (!studentExists(roll)) {
                System.out.println("Error: Student does not exist!");
                return;
            }

            System.out.print("Old Course: ");
            String oldCourse = sc.nextLine();
            System.out.print("New Course: ");
            String newCourse = sc.nextLine();

            // Validation: Reject numeric course names
            if (!newCourse.matches("^[A-Za-z ]+$")) {
                System.out.println("Error: Course name must not contain numbers!");
                return;
            }

            System.out.print("New Credit: ");
            int credit = sc.nextInt(); sc.nextLine();

            String url = String.format(
                    "%s/enrollments/update/%s?oldCourse=%s&newCourse=%s&credit=%d",
                    BASE_URL, roll, oldCourse, newCourse, credit
            );

            Content res = Request.put(url).execute().returnContent();
            System.out.println(res.asString());
        } catch (HttpResponseException e) {
            if (e.getStatusCode() == 404) {
                System.out.println("Error: Enrollment not found for update!");
            } else if (e.getStatusCode() == 409) {
                System.out.println("Error: Duplicate enrollment!");
            } else {
                System.out.println("Error updating enrollment: " + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("Error updating enrollment: " + e.getMessage());
        }
    }

    // ---------------- HELPER METHODS ----------------
    private static boolean studentExists(String roll) {
        try {
            Request.get(BASE_URL + "/students/roll/" + roll).execute().returnContent();
            return true;
        } catch (HttpResponseException e) {
            if (e.getStatusCode() == 404) return false;
        } catch (Exception ignored) {}
        return false;
    }

    // ---------------- PRINT UTILITIES ----------------
    private static void printStudentsTable(List<Map<String, Object>> students) {
        System.out.printf("%-10s %-20s %-25s\n", "RollNo", "Name", "Email");
        System.out.println("-----------------------------------------------");
        for (Map<String, Object> s : students) {
            System.out.printf("%-10s %-20s %-25s\n", s.get("rollNo"), s.get("name"), s.get("email"));
        }
    }

    private static void printEnrollmentsTable(List<Map<String, Object>> enrollments) {
        System.out.printf("%-10s %-20s %-10s\n", "RollNo", "Course", "Credit");
        System.out.println("-----------------------------------");
        for (Map<String, Object> e : enrollments) {
            System.out.printf("%-10s %-20s %-10s\n", e.get("studentRollNo"), e.get("course"), e.get("credit"));
        }
    }
}
