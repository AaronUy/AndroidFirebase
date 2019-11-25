package com.example.android.androidfirebase;

import java.util.Date;

public class Post {
    private String id;
    private String title;
    private String category;
    private Date date;
    private String content;

    public Post(){

    }

    public Post(String id, String title, String category, Date date, String content){
        this.id = id;
        this.title = title;
        this.category = category;
        this.date = date;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setBlog(String blog) {
        this.content = blog;
    }
}

