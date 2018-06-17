package com.alenut.mpi.entities;

import javax.persistence.*;

@Entity
@Table(name = "following")
public class Following {

    @Id
    @Column(name = "id_following")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;

    @ManyToOne
    @JoinColumn(name = "id_following_user")
    private User followingUser;

    @Column(name = "date_following")
    private String date_following;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getFollowingUser() {
        return followingUser;
    }

    public void setFollowingUser(User followingUser) {
        this.followingUser = followingUser;
    }

    public String getDate_following() {
        return date_following;
    }

    public void setDate_following(String date_following) {
        this.date_following = date_following;
    }
}
