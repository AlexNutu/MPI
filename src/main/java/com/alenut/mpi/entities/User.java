package com.alenut.mpi.entities;

import com.alenut.mpi.entities.info.UserInformation;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "user")
public class User {

    @Id
    @Column(name = "id_user")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phone_number;

    @Column(name = "role")
    private int role;

    @Column(name = "reg_date")
    private Date reg_date;

//    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//        @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
//    private List<Role> roles;

    User() {
    }

    public User(UserInformation userInformation) {
        this.username = userInformation.getUsername();
        this.password = userInformation.getPassword();
        this.email = userInformation.getEmail();
        this.phone_number = userInformation.getPhone_number();
        this.reg_date = userInformation.getRegistration_date();
        this.role = userInformation.getRole();
    }

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

}
