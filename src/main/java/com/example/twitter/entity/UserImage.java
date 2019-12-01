package com.example.twitter.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "files")
public class UserImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String fileName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    public UserImage () {
    }

    public String getUserName() {
        return user != null ? user.getUsername() : "";
    }

    public Long getUserId() {
        return user.getId();
    }

    public int getFollowingsCount () {
        return user.getFollowings().size();
    }
    public int getFollowersCount () {
        return user.getFollowers().size();
    }

    public UserImage (User user) {
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserImage userImage = (UserImage) o;
        return id.equals(userImage.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
