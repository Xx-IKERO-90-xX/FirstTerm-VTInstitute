package org.vtinstitute.models;

public class SubjectCourse {
    // Atributos
    private int code; // PK (INTEGER - subject_courses_code_seq)
    private int subjectId; // FK
    private int courseId; // FK

    // Constructor Vacío
    public SubjectCourse() {
    }

    // Constructor Completo
    public SubjectCourse(int code, int subjectId, int courseId) {
        this.code = code;
        this.subjectId = subjectId;
        this.courseId = courseId;
    }

    // Getters y Setters
    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; }
    public int getSubjectId() { return subjectId; }
    public void setSubjectId(int subjectId) { this.subjectId = subjectId; }
    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }

    @Override
    public String toString() {
        return "SubjectCourse{" +
                "code=" + code +
                ", subjectId=" + subjectId +
                ", courseId=" + courseId +
                '}';
    }
}