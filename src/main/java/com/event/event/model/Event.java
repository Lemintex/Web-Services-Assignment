package com.event.event.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String description;

    private String location;

    private String eventDate;

    private String eventTime;

    private String eventDuration;

    private int capacity;

    private int attending;

    public Event(String description, String location, String eventDate, String eventTime, String eventDuration, int capacity) {
        this.description = description;
        this.location = location;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.eventDuration = eventDuration;
        this.capacity = capacity;
        this.attending = 0;
    }

    public Event() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getEventDuration() {
        return eventDuration;
    }

    public void setEventDuration(String eventDuration) {
        this.eventDuration = eventDuration;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getAttending() {
        return attending;
    }

    public void setAttending(int attending) {
        this.attending = attending;
    }
}
