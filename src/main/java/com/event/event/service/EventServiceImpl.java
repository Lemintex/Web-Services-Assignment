
package com.event.event.service;

import com.event.event.model.Attend;
import com.event.event.model.Event;
import com.event.event.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class EventServiceImpl implements EventService {
    @Autowired
    private EventRepository eventRepo;

    public Optional<Event> getEvent(int eventID) {
       if (doesEventExist(eventID)) return eventRepo.findById(eventID);
        return null;
    }

    public Boolean doesEventExist(int eventID) {
        return eventRepo.existsById(eventID);
    }

    public ResponseEntity<Void> listEvents() {
        List<Event> result = new ArrayList<>();
        if (eventRepo.count() == 0) return new ResponseEntity(result, HttpStatus.NOT_FOUND);
        Iterable allAttends = eventRepo.findAll();
        Iterator iter = allAttends.iterator();
        while (iter.hasNext()) {
            result.add((Event) iter.next());
        }
        return new ResponseEntity(result, HttpStatus.FOUND);
    }

    public void changeAttending(int eventID, int number) {
        Event event = eventRepo.findById(eventID).get();
        event.setAttending(event.getAttending() + number);
        eventRepo.save(event);
    }
}
