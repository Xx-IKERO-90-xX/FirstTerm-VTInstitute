package org.vtinstitute.models;

public class Student {
    // Atributos
    private String idCard; // PK (VARCHAR(8))
    private String firstName; // NOT NULL
    private String lastName; // NOT NULL
    private String phone;
    private String email;

    // Constructor Vacío
    public Student() {
    }

    // Constructor Completo
    public Student(String idCard, String firstName, String lastName, String phone, String email) {
        this.idCard = idCard;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
    }

    // Getters y Setters
    public String getIdCard() { return idCard; }
    public void setIdCard(String idCard) { this.idCard = idCard; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return "Student{" +
                "idCard='" + idCard + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}