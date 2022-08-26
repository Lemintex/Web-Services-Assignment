package com.event.event.service;

import com.event.event.model.Attend;
import com.event.event.model.User;
import com.event.event.repository.AttendRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class AttendServiceImpl implements AttendService {
    @Autowired
    private AttendRepository attendRepo;

    public Boolean doesUserExist(String userID) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://pmaier.eu.pythonanywhere.com/user/" + userID;
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return true;
        }
        catch (HttpStatusCodeException e) {
            return false;
        }
    }

    public ResponseEntity<Attend> getEventsRegisteredFor(String userID) {
        List<Attend> result = new ArrayList<Attend>();

        Iterable<Attend> allAttends = attendRepo.findAll();
        Iterator<Attend> iter = allAttends.iterator();
        while (iter.hasNext()) {
            Attend next = iter.next();
            if (next.getUserID().equals(userID)) result.add(next);
        }
        if (result.isEmpty()) return new ResponseEntity(result, HttpStatus.NOT_FOUND);
        return new ResponseEntity(result, HttpStatus.FOUND);
    }

    public ResponseEntity<Void> registerForEvent(String userID, int eventID) {
        Boolean userExists = doesUserExist(userID);
        if (!userExists) return new ResponseEntity(HttpStatus.NOT_FOUND);
        Iterable<Attend> allAttends = attendRepo.findAll();
        Iterator<Attend> iter = allAttends.iterator();
        while (iter.hasNext()) {
            Attend next = iter.next();
            if (next.getUserID().equals(userID) && next.getEventID() == eventID) return new ResponseEntity(HttpStatus.ALREADY_REPORTED);
        }
        Attend a = new Attend(eventID, userID);
        attendRepo.save(a);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    public ResponseEntity<Void> removeUserFromAllEvents(String userID) {
//        Boolean userExists = doesUserExist(userID);
//        if (!userExists) return new ResponseEntity(HttpStatus.BAD_REQUEST);
        ResponseEntity attending = getEventsRegisteredFor(userID);
        List<Attend> list = (List<Attend>) attending.getBody();
        for (int i = 0; i < list.size(); i++) {
            attendRepo.delete(list.get(i));
        }
        return new ResponseEntity(HttpStatus.OK);
    }
    public ResponseEntity<Void> removeUserFromEvent(String userID, int eventID) {
//        Boolean userExists = doesUserExist(userID);
//        if (!userExists) return new ResponseEntity(HttpStatus.NOT_FOUND);
        Iterable<Attend> allAttends = attendRepo.findAll();
        Iterator<Attend> iter = allAttends.iterator();
        Attend a = null;
        var exists = false;
        while (iter.hasNext()) {
            Attend next = iter.next();
            if (next.getEventID() == eventID && next.getUserID().equals(userID)) {
                a = next;
                exists = true;
                break;
            }
        }
        if (!exists) return new ResponseEntity(HttpStatus.NOT_FOUND);
        attendRepo.delete(a);
        return new ResponseEntity(HttpStatus.OK);
    }

    public ResponseEntity<Void> getAttendees(int eventID) {
        RestTemplate restTemplate = new RestTemplate();
        List<User> result = new ArrayList();
        Iterable<Attend> allAttends = attendRepo.findAll();
        Iterator<Attend> iter = allAttends.iterator();
        while (iter.hasNext()) {
            Attend next = iter.next();
            if (next.getEventID() == eventID) {
                String url = "https://pmaier.eu.pythonanywhere.com/user/" + next.getUserID();
                ResponseEntity<String> r;
                try {
                    r = restTemplate.getForEntity(url, String.class);
                    JSONObject jsonObject = new JSONObject(r.getBody());
                    JSONObject user = jsonObject.getJSONObject("user");
                    JSONArray interestsJSON = user.getJSONArray("interests");
                    ArrayList interests = new ArrayList();
                    for (int i = 0; i < interestsJSON.length(); i++) {
                        interests.add(interestsJSON.getString(i));
                    }
                    result.add(new User(interests, user.getString("name"), next.getUserID()));
                } catch (Exception e) {
                    ArrayList interests = new ArrayList();
                    User temp = new User(interests, "USER NOT IN LIST", next.getUserID());
                    result.add(temp);
                }
            }
        }
        if (result.isEmpty()) return new ResponseEntity(result, HttpStatus.NOT_FOUND);
        return new ResponseEntity(result, HttpStatus.FOUND);
    }

    public ResponseEntity<Void> validateAttendees(List<User> attendees) {
        RestTemplate restTemplate = new RestTemplate();
        List<String> results = new ArrayList();
        for (int i = 0; i < attendees.size(); i++) {
            String url = "https://pmaier.eu.pythonanywhere.com/user/" + attendees.get(i).getUid();

            try {
                ResponseEntity<String> r = restTemplate.getForEntity(url, String.class);
                results.add("VALIDATED USER: " + attendees.get(i).getUid());
            }
            catch (HttpStatusCodeException e) {
                removeUserFromAllEvents(attendees.get(i).getUid());
                results.add("REMOVED USER: " + attendees.get(i).getUid());
            }
        }
        return new ResponseEntity(results, HttpStatus.OK);
    }

}