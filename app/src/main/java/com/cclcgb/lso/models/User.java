package com.cclcgb.lso.models;

public class User {
    private int mId;
    private String mName;

    public User(int id, String name) {
        this.mId = id;
        this.mName = name;
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }
}
