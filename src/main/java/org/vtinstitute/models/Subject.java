package org.vtinstitute.models;

public class Subject {
    // Atributos
    private int code; // PK (INTEGER - subjects_code_seq)
    private String name;
    private int year;
    private int hours;

    // Constructor Vacío
    public Subject() {
    }

    // Constructor Completo
    public Subject(int code, String name, int year, int hours) {
        this.code = code;
        this.name = name;
        this.year = year;
        this.hours = hours;
    }

    // Getters y Setters
    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
    public int getHours() { return hours; }
    public void setHours(int hours) { this.hours = hours; }

    @Override
    public String toString() {
        return "Subject{" +
                "code=" + code +
                ", name='" + name + '\'' +
                ", year=" + year +
                ", hours=" + hours +
                '}';
    }
}