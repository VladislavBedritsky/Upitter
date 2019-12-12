package com.example.twitter.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String text;
    private String tag;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    private String filename;
    private String time;

    @ManyToMany
    @JoinTable(
            name="message_likes",
            joinColumns = {@JoinColumn(name="message_id")},
            inverseJoinColumns = {@JoinColumn(name="user_id")}
    )
    private Set<User> likes = new HashSet<>();

    public Message() {}

    public Message(Integer id) {
        this.id = id;
    }

    public Message(String text, String tag, User user,String time) {
        this.text = text;
        this.tag = tag;
        this.user = user;
        this.time = time;
    }

    public int getAmountLikes () {
        return likes.size();
    }

    public UserImage getImage () {
        for(UserImage temp:user.getUserImages()) {
            return temp;
        }
        return null;
    }

    public Long getUserId () {
        return user.getId();
    }

    public String getUserName () {
        return user != null ? user.getUsername() : "<none>";
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Message(String tag) {
        this.tag = tag;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Set<User> getLikes() {
        return likes;
    }

    public void setLikes(Set<User> likes) {
        this.likes = likes;
    }
}
