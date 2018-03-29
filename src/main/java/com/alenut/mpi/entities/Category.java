package com.alenut.mpi.entities;

import javax.persistence.*;

@Entity
public class Category {

    @Id
    @Column(name = "id_category")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id_category;

    @Column(name = "body")
    private String body;

    public Long getId_category() {
        return id_category;
    }

    public void setId_category(Long id_category) {
        this.id_category = id_category;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
