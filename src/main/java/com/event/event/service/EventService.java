package com.event.event.service;

import com.event.event.model.Event;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface EventService {

    public Optional<Event> getEvent(int eventID);

    public Boolean doesEventExist(int eventID);

    public ResponseEntity<Void> listEvents();

    public void changeAttending(int eventID, int number);
}