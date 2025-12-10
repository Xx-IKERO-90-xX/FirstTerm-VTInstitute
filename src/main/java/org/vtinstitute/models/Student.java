package org.vtinstitute.models;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "students")
public class Student {
    @Id
    @SequenceGenerator(name = "students_id_gen", sequenceName = "scores_code_seq", allocationSize = 1)
    @Column(name = "idcard", nullable = false, length = 8)
    private String idcard;

    @Column(name = "firstname", nullable = false, length = 50)
    private String firstname;

    @Column(name = "lastname", nullable = false, length = 100)
    private String lastname;

    @Column(name = "phone", length = 12)
    private String phone;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "birthdate")
    private String birthdate;

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setBirthdate(String birthdate) {this.birthdate = birthdate;}

    public String getBirthdate() {return birthdate;}
}