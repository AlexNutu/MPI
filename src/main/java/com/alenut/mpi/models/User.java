package com.alenut.mpi.models;

import javax.persistence.*;

import com.alenut.mpi.models.info.UserInformation;

import java.util.Date;

@Entity
@Table(name = "user")
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "nr_ideas")
    private Integer nr_ideas;

    @Column(name = "ocupation")
    private String ocupation;

    @Column(name = "gender")
    private String gender;

    @Column(name = "phone_number")
    private String phone_number;

    @Column(name = "reg_date")
    private Date reg_date;

    @Column(name = "role")
    private int role;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getNr_ideas() {
        return nr_ideas;
    }

    public void setNr_ideas(Integer nr_ideas) {
        this.nr_ideas = nr_ideas;
    }

    public String getOcupation() {
        return ocupation;
    }

    public void setOcupation(String ocupation) {
        this.ocupation = ocupation;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public Date getReg_date() {
        return reg_date;
    }

    public void setReg_date(Date reg_date) {
        this.reg_date = reg_date;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public User() {
    }

    public User(UserInformation userInformation) {
        this.username = userInformation.getUsername();
        this.password = userInformation.getPassword();
        this.email = userInformation.getEmail();
        this.nr_ideas = userInformation.getNr_ideas();
        this.ocupation = userInformation.getOcupation();
        this.gender = userInformation.getGender();
        this.phone_number = userInformation.getPhone_number();
        this.reg_date = userInformation.getRegistration_date();
        this.role = userInformation.getRole();
    }
}

