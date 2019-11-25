package com.example.android.androidfirebase;

import java.util.Date;

public class Category {
    private String id;
    private String Cname;
    private Date date;


    public Category(){

    }

    public Category(String id, String cname, Date date) {
        this.id = id;
        Cname = cname;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public String getCname() {
        return Cname;
    }

    public void setCname(String cname) {
        Cname = cname;
    }
}

