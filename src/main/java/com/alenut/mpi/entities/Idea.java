package com.alenut.mpi.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Idea {

    @Id
    @Column(name = "id_idea")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id_idea;

    @Column(name = "title")
    private String title;

    @Column(name = "body")
    private String body;

    @Column(name = "postedDate")
    private Date postedDate;

    @Column(name = "id_creator")
    private Long idCreator;

    public Idea(){
    }

    public Long getId() {
        return id_idea;
    }

    public void setId(Long id) {
        this.id_idea = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(Date posted_date) {
        this.postedDate = posted_date;
    }

    public Long getCreator() {
        return idCreator;
    }

    public void setCreator(Long idCreator) {
        this.idCreator = idCreator;
    }

}
