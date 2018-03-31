package com.alenut.mpi.entities;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

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

    @Column(name = "likes_number")
    private Integer likes_number;

    @Column(name = "posted_date")
    private Date posted_date;

    @Column(name = "image_path")
    private String image_path;

    @OneToOne
    @JoinColumn(name = "id_category")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;

    @OneToMany(mappedBy = "idea")
    private List<Comment> comments;

    public Idea() {
    }

    public Long getId_idea() {
        return id_idea;
    }

    public void setId_idea(Long id_idea) {
        this.id_idea = id_idea;
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

    public Integer getLikes_number() {
        return likes_number;
    }

    public void setLikes_number(Integer likes_number) {
        this.likes_number = likes_number;
    }

    public Date getPosted_date() {
        return posted_date;
    }

    public void setPosted_date(Date posted_date) {
        this.posted_date = posted_date;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
