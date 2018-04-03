package com.alenut.mpi.entities;

import javax.persistence.*;

@Entity
public class Matching {
    @Id
    @Column(name = "id_match")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_idea")
    private Idea idea;

    @ManyToOne
    @JoinColumn(name = "id_match_idea")
    private Idea ideaMatch;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Idea getIdea() {
        return idea;
    }

    public void setIdea(Idea idea) {
        this.idea = idea;
    }

    public Idea getIdeaMatch() {
        return ideaMatch;
    }

    public void setIdeaMatch(Idea ideaMatch) {
        this.ideaMatch = ideaMatch;
    }
}
