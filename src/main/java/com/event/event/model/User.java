package com.event.event.model;

import java.util.ArrayList;

public class User {
    private ArrayList<String> interests;
    private String name;
    private String uid;

    public User(ArrayList<String> interests, String name, String uid) {
        this.interests = interests;
        this.name = name;
        this.uid = uid;
    }

    public ArrayList<String> getInterests() {
        return interests;
    }

    public void setInterests(ArrayList<String> interests) {
        this.interests = interests;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
