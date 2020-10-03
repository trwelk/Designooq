package com.Tregaki.designooq;

import java.util.HashMap;
import java.util.List;

public class Post {
    public String image;


    public HashMap<String,HashMap<String,String>> favourite;

    public HashMap<String, HashMap<String, String>> getFavourite() {
        return favourite;
    }

    public void setFavourite(HashMap<String, HashMap<String, String>> favourite) {
        this.favourite = favourite;
    }

    public Post() {
    }

    public Post(String image, String description, String user) {
        this.image = image;
        this.description = description;
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String title;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String description;
    public String user;
}
