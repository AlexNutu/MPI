package com.alenut.mpi.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Message {

    @Id
    @Column(name = "id_message")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id_message;

    @Column(name = "body")
    private String body;

    @Column(name = "send_date")
    private Date send_date;

    @Column(name = "id_sender")
    private Long id_sender;

//    @Column(name = "id_user")
//    private Long id_user;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;

    public Long getId_message() {
        return id_message;
    }

    public void setId_message(Long id_message) {
        this.id_message = id_message;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getSend_date() {
        return send_date;
    }

    public void setSend_date(Date send_date) {
        this.send_date = send_date;
    }

    public Long getId_sender() {
        return id_sender;
    }

    public void setId_sender(Long id_sender) {
        this.id_sender = id_sender;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
