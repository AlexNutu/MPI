package com.alenut.mpi.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Comment {

    @Id
    @Column(name = "id_comment")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id_comment;

    @Column(name = "body")
    private String body;

    @Column(name = "posted_date")
    private Date posted_date;

//    @Column(name = "id_idea")
//    private Long id_idea;

    @ManyToOne
    @JoinColumn(name = "id_idea")
    private Idea idea;

    public Long getId_comment() {
        return id_comment;
    }

    public void setId_comment(Long id_comment) {
        this.id_comment = id_comment;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getPosted_date() {
        return posted_date;
    }

    public void setPosted_date(Date posted_date) {
        this.posted_date = posted_date;
    }

    public Idea getIdea() {
        return idea;
    }

    public void setIdea(Idea idea) {
        this.idea = idea;
    }
}
