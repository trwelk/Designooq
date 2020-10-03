package com.Tregaki.designooq;

public class User {
    public User() {
    }
    public String username;

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public boolean online;
    public String email;
    public String image;
    public String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public User(String username, String email, String image,String type,boolean online) {
        this.username = username;
        this.email = email;
        this.image = image;
        this.type = type;
        this.online = online;
    }

}
