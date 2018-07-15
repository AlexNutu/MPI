package com.alenut.mpi.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Message {

    @Id
    @Column(name = "id_message")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "body")
    private String body;

    @Column(name = "send_date")
    private String send_date;

    @ManyToOne
    @JoinColumn(name = "id_sender")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "id_conversation")
    private Conversation conversation;

    @Column(name = "id_receiver")
    private Long id_receiver;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSend_date() {
        return send_date;
    }

    public void setSend_date(String send_date) {
        this.send_date = send_date;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    public Long getId_receiver() {
        return id_receiver;
    }

    public void setId_receiver(Long id_receiver) {
        this.id_receiver = id_receiver;
    }
}
