package org.vtinstitute.models;

public class Course {
    // Atributos
    private int code; // PK (INTEGER - courses_code_seq)
    private String name; // NOT NULL

    // Constructor Vacío
    public Course() {
    }

    // Constructor Completo
    public Course(int code, String name) {
        this.code = code;
        this.name = name;
    }

    // Getters y Setters
    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @Override
    public String toString() {
        return "Course{" +
                "code=" + code +
                ", name='" + name + '\'' +
                '}';
    }
}
