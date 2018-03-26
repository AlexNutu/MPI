package com.alenut.mpi.entities.info;

import java.util.Date;

public class UserInformation {

    private Integer id;
    private String fullName;
    private String username;
    private String password;
    private String email;
    private Integer likes;
    private String ocupation;
    private String gender;
    private String phone_number;
    private Date registration_date;
    private int role; // 1 - admin, 0 - normal user

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
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

    public Date getRegistration_date() {
        return registration_date;
    }

    public void setRegistration_date(Date registration_date) {
        this.registration_date = registration_date;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
