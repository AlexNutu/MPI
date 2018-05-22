package com.alenut.mpi.entities;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Idea {

    @Id
    @Column(name = "id_idea")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "body")
    private String body;

    @Column(name = "posted_date")
    private String posted_date;

    @Column(name = "image_path")
    private String image_path;

    @OneToOne
    @JoinColumn(name = "id_category")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;

    @OneToMany(mappedBy = "idea")
    @OrderBy("posted_date DESC")
    private List<Comment> comments;

    @OneToMany(mappedBy = "idea")
    private List<Appreciation> appreciations;

    @OneToMany(mappedBy = "idea")
    private List<Tag> tags;

    @OneToMany(mappedBy = "idea")
    private List<Matching> matchings;

    @Column(name = "likenumber")
    private Integer likenumber;

    @Column(name = "comnumber")
    private Integer comnumber;

    @Column(name = "simnumber")
    private Integer simnumber;

    @Column(name = "semantic")
    private String semantic;

    @Column(name = "sintactic")
    private String sintactic;

    @Transient
    private long liked;

    public Idea() {
    }

    public Idea(Idea idea2){
        this.title = idea2.title;
        this.body = idea2.body;
        this.posted_date = idea2.posted_date;
        this.image_path = idea2.image_path;
        this.category = idea2.category;
        this.user = idea2.user;
        this.comments = idea2.comments;
        this.appreciations = idea2.appreciations;
        this.tags = idea2.tags;
        this.matchings = idea2.matchings;
        this.semantic = idea2.semantic;
        this.sintactic = idea2.sintactic;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getPosted_date() {
        return posted_date;
    }

    public void setPosted_date(String posted_date) {
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

    public List<Appreciation> getAppreciations() {
        return appreciations;
    }

    public void setAppreciations(List<Appreciation> appreciations) {
        this.appreciations = appreciations;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public List<Matching> getMatchings() {
        return matchings;
    }

    public void setMatchings(List<Matching> matchings) {
        this.matchings = matchings;
    }

    public String getSemantic() {
        return semantic;
    }

    public void setSemantic(String semantic) {
        this.semantic = semantic;
    }

    public String getSintactic() {
        return sintactic;
    }

    public void setSintactic(String sintactic) {
        this.sintactic = sintactic;
    }

    public long getLiked() {
        return liked;
    }

    public void setLiked(long liked) {
        this.liked = liked;
    }

    public Integer getLikenumber() {
        return likenumber;
    }

    public void setLikenumber(Integer likenumber) {
        this.likenumber = likenumber;
    }

    public Integer getComnumber() {
        return comnumber;
    }

    public void setComnumber(Integer comnumber) {
        this.comnumber = comnumber;
    }

    public Integer getSimnumber() {
        return simnumber;
    }

    public void setSimnumber(Integer simnumber) {
        this.simnumber = simnumber;
    }
}
