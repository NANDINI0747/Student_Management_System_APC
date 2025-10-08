package com.example.enrollmentservice.model;

public class Enrollment {//varibles to hold enrollment details
    private String studentRollNo;
    private String studentName;
    private String course;
    private int credit;

    public Enrollment() {}//No-args constructor.

    public Enrollment(String studentRollNo, String studentName, String course, int credit) {
        this.studentRollNo = studentRollNo;
        this.studentName = studentName;
        this.course = course;
        this.credit = credit;
    }//Parameterized constructor.Allows creating an Enrollment object with all fields initialized in one step:

    public String getStudentRollNo() { return studentRollNo; }
    public void setStudentRollNo(String studentRollNo) { this.studentRollNo = studentRollNo; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }

    public int getCredit() { return credit; }
    public void setCredit(int credit) { this.credit = credit; }
}
