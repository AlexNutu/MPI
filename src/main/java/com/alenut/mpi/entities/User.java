package com.alenut.mpi.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "user")
public class User {

    @Id
    @Column(name = "id_user")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name")
    private String full_name;

    @NotNull
    @Column(name = "username", unique = true)
    private String username;

    @NotNull
    @Column(name = "email", unique = true)
    private String email;

    @NotNull
    @Column(name = "password")
    private String password;

    @Column(name = "phone_number")
    private String phone_number;

    @Column(name = "role")
    private int role;

    @Column(name = "reg_date")
    private Date reg_date;

    @Column(name = "occupation")
    private String occupation;

    @Column(name = "image_path")
    private String image;

    @OneToMany(mappedBy = "user")
    private List<Idea> ideas;

    @OneToMany(mappedBy = "sender")
    private List<Message> messages;

    @OneToMany(mappedBy = "user")
    private List<Comment> comments;

    @OneToMany(mappedBy = "user")
    private List<Appreciation> appreciations;

    @OneToMany(mappedBy = "user")
    private List<Conversation> conversations;

    @Transient
    private String newPassword;

    @Column(name = "confirmed")
    private int confirmed;

    @Column(name = "token")
    private String token;

    @Column(name = "alert")
    private int alert;

//    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//        @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
//    private List<Role> roles;

    public User() {
    }

    public User(String full_name, String username, String email, String password, String phone_number, int role, Date reg_date, String occupation, String image, List<Idea> ideas, List<Comment> comments, List<Appreciation> appreciations, List<Conversation> conversations, String newPassword) {
        this.full_name = full_name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.phone_number = phone_number;
        this.role = role;
        this.reg_date = reg_date;
        this.occupation = occupation;
        this.image = image;
        this.ideas = ideas;
        this.comments = comments;
        this.appreciations = appreciations;
        this.conversations = conversations;
        this.newPassword = newPassword;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public Date getReg_date() {
        return reg_date;
    }

    public void setReg_date(Date reg_date) {
        this.reg_date = reg_date;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Idea> getIdeas() {
        return ideas;
    }

    public void setIdeas(List<Idea> ideas) {
        this.ideas = ideas;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Appreciation> getAppreciations() {
        return appreciations;
    }

    public void setAppreciations(List<Appreciation> appreciations) {
        this.appreciations = appreciations;
    }

    public List<Conversation> getConversations() {
        return conversations;
    }

    public void setConversations(List<Conversation> conversations) {
        this.conversations = conversations;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public int getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(int confirmed) {
        this.confirmed = confirmed;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getAlert() {
        return alert;
    }

    public void setAlert(int alert) {
        this.alert = alert;
    }
}
