package com.example.studentservice.model;

public class Student {
    private String rollNo;   // primary key
    private String name;
    private String email;

    public Student(String rollNo, String name, String email) {
        this.rollNo = rollNo;
        this.name = name;
        this.email = email;
    }

    // Default constructor
    public Student() {}

    // Getters & Setters
    public String getRollNo() { return rollNo; }
    public void setRollNo(String rollNo) { this.rollNo = rollNo; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
