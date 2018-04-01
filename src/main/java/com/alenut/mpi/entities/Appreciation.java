package com.alenut.mpi.entities;

import javax.persistence.*;

@Entity
@Table(name = "appreciation")
public class Appreciation {

    @Id
    @Column(name = "id_appreciation")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id_appreciation;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;

    @ManyToOne
    @JoinColumn(name = "id_idea")
    private Idea idea;

    public Long getId_appreciation() {
        return id_appreciation;
    }

    public void setId_appreciation(Long id_appreciation) {
        this.id_appreciation = id_appreciation;
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
