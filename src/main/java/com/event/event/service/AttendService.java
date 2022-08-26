package com.event.event.service;

import com.event.event.model.Attend;
import com.event.event.model.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AttendService {

    public Boolean doesUserExist(String userID);

    public ResponseEntity<Attend> getEventsRegisteredFor(String userID);

    public ResponseEntity<Void> getAttendees(int eventID);

    public ResponseEntity<Void> registerForEvent(String userID, int eventID);

    public ResponseEntity<Void> removeUserFromEvent(String userID, int eventID);

    public ResponseEntity<Void> removeUserFromAllEvents(String userID);

    public ResponseEntity<Void> validateAttendees(List<User> attendees);
}
