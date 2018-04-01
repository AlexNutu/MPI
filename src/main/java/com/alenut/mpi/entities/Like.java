package com.alenut.mpi.entities;

import javax.persistence.*;

@Entity
@Table(name = "like")
public class Like {

    @Id
    @Column(name = "id_like")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id_like;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;

    @ManyToOne
    @JoinColumn(name = "id_idea")
    private Idea idea;

    public Long getId_like() {
        return id_like;
    }

    public void setId_like(Long id_like) {
        this.id_like = id_like;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Idea getIdea() {
        return idea;
    }

    public void setIdea(Idea idea) {
        this.idea = idea;
    }
}
