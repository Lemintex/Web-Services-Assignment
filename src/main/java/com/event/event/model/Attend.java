package com.event.event.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Attend {
    @Id
    @GeneratedValue
    private int key;
    private int eventID;
    private String userID;

    public Attend(int eventID, String userID) {
        this.eventID = eventID;
        this.userID = userID;
    }

    public Attend() {

    }

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}