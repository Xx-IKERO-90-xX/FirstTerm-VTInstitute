package org.vtinstitute.models;

public class Enrollment {
    // Atributos
    private int code; // PK (INTEGER - inscriptions_code_seq)
    private String studentId; // FK
    private int courseId; // FK
    private int year; // NOT NULL

    // Constructor Vacío
    public Enrollment() {
    }

    // Constructor Completo
    public Enrollment(int code, String studentId, int courseId, int year) {
        this.code = code;
        this.studentId = studentId;
        this.courseId = courseId;
        this.year = year;
    }

    // Getters y Setters
    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; }
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    @Override
    public String toString() {
        return "Enrollment{" +
                "code=" + code +
                ", studentId='" + studentId + '\'' +
                ", courseId=" + courseId +
                ", year=" + year +
                '}';
    }
}
