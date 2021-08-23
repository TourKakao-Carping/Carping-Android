package com.tourkakao.carping.Home.EcoDataClass;

public class EcoReview {
    private int today_count;
    private String id;
    private String user;
    private String image;
    private String title;
    private String text;
    private String[] tags;
    private String created_at;

    public EcoReview(){}

    public EcoReview(int today_count, String id, String user, String image, String title, String text, String[] tags, String created_at) {
        this.today_count = today_count;
        this.id = id;
        this.user = user;
        this.image = image;
        this.title = title;
        this.text = text;
        this.tags = tags;
        this.created_at = created_at;
    }

    public String getId() {
        return id;
    }

    public String getUser() {
        return user;
    }

    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String[] getTags() {
        return tags;
    }

    public String getCreated_at() {
        return created_at;
    }

    public int getToday_count() {
        return today_count;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}