package com.alenut.mpi.entities;

import javax.persistence.*;
import java.util.List;

@Entity
public class Category {

    @Id
    @Column(name = "id_category")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "body")
    private String body;

    @OneToMany(mappedBy = "category")
    @OrderBy("posted_date DESC")
    private List<Idea> ideasFromCategory;

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

    public List<Idea> getIdeasFromCategory() {
        return ideasFromCategory;
    }

    public void setIdeasFromCategory(List<Idea> ideasFromCategory) {
        this.ideasFromCategory = ideasFromCategory;
    }
}
