package com.example.enrollmentservice.model;

public class Student {
    private String rollNo;
    private String name;
    private String email;

    public Student() {}//When JSON is sent in a request, Spring creates a Student object using this constructor, then fills in values using setters.

    public String getRollNo() { return rollNo; }
    public void setRollNo(String rollNo) { this.rollNo = rollNo; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
