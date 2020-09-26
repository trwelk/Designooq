package com.Tregaki.designooq;

public class Post {
    public String image;

    public Post(String image, String description, String user) {
        this.image = image;
        this.description = description;
        this.user = user;
    }

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
