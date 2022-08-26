package com.event.event.controller;

import com.event.event.model.Attend;
import com.event.event.model.Event;
import com.event.event.model.User;
import com.event.event.service.AttendService;
import com.event.event.service.EventService;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
public class Controller {

    @Autowired
    private EventService eventService;
    @Autowired
    private AttendService attendService;

    private String clientApiKey = "jLSyHjLzWPSvRHpxZTLTJTGbQdMTajgAYJApfVCUuxawjDQaLbhxwVcBCfibFQeh";
    private String adminApiKey = "cLQVwVYupLbBYQzUcxnXqNifQWrMAJpNAwrpYypRZDbfXkwmmZjGxfQhZFnChjct";

    //BOTH
    //------------------------------------------------------------------------------------------------

    @GetMapping("/list")
    public ResponseEntity<Void> getEvents(@RequestHeader("Authorization") String key) {
        ResponseEntity<Void> response;
        if (!key.equals(adminApiKey) && !key.equals(clientApiKey)) response = new ResponseEntity(HttpStatus.UNAUTHORIZED);
        else {response = eventService.listEvents();}
        System.out.println("GET /list => " + response.getStatusCode());
        return response;
    }

    //CLIENT
    //-----------------------------------------------------------------------------------------------
        @GetMapping("/client/list/{userID}")
        public ResponseEntity<Attend> getEventsRegisteredFor(@RequestHeader("Authorization") String clientKey, @PathVariable String userID) {
            ResponseEntity<Attend> response;
            if (!clientKey.equals(clientApiKey)) response = new ResponseEntity(HttpStatus.UNAUTHORIZED);
            else {response = attendService.getEventsRegisteredFor(userID);}
            System.out.println("GET /list => " + response.getStatusCode());
            return response;
        }

    @PostMapping("/client/add/client={userID}&event={eventID}")
    public ResponseEntity<Void> addUserToEvent(@RequestHeader("Authorization") String clientKey, @PathVariable String userID, @PathVariable int eventID) {
        ResponseEntity<Void> response;
        if (!clientKey.equals(clientApiKey)) response = new ResponseEntity(HttpStatus.UNAUTHORIZED);
        else if (!eventService.doesEventExist(eventID)) response = new ResponseEntity(HttpStatus.NOT_FOUND);
        else if (eventService.getEvent(eventID).get().getCapacity() <= eventService.getEvent(eventID).get().getAttending()) response = new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
        else {
            response = attendService.registerForEvent(userID, eventID);
            if (response.getStatusCode() != HttpStatus.ALREADY_REPORTED) eventService.changeAttending(eventID, 1);
        }
        System.out.println("POST /client/add/client=" + userID +"&event=" + eventID + " => " + response.getStatusCode());
        return response;
    }

    @DeleteMapping("/client/remove/client={userID}&event={eventID}")
    public ResponseEntity<Void> removeUserFromEvent(@RequestHeader("Authorization") String clientKey, @PathVariable String userID, @PathVariable int eventID) {
        ResponseEntity<Void> response;
        if (!clientKey.equals(clientApiKey)) response = new ResponseEntity(HttpStatus.UNAUTHORIZED);
        else if (!eventService.doesEventExist(eventID)) response = new ResponseEntity(HttpStatus.NOT_FOUND);
        else {
            response = attendService.removeUserFromEvent(userID, eventID);
            if (response.getStatusCode() != HttpStatus.NOT_FOUND) eventService.changeAttending(eventID, -1);
        }
        System.out.println("DELETE /client/remove/client=" + userID + "&event=" + eventID + " => " + response.getStatusCode());
        return response;
    }

    //ADMIN
    //-------------------------------------------------------------------------------------------------------------

    @GetMapping("/admin/list/id={eventID}")
    public ResponseEntity<Void> getAttendees(@RequestHeader("Authorization") String adminKey, @PathVariable int eventID) {
        ResponseEntity<Void> response;
        if (!adminKey.equals(adminApiKey)) response = new ResponseEntity(HttpStatus.UNAUTHORIZED);
        else if (!eventService.doesEventExist(eventID)) response = new ResponseEntity(HttpStatus.BAD_REQUEST);
        else {response = attendService.getAttendees(eventID);}
        System.out.println("GET /admin/list/id=" + eventID + " => " + response.getStatusCode());
        return response;
    }

    @DeleteMapping("/admin/validate/id={eventID}")
    public ResponseEntity<Void> validateAttendees(@RequestHeader("Authorization") String adminKey, @PathVariable int eventID) throws JSONException {
        ResponseEntity<Void> response;
        if (!adminKey.equals(adminApiKey)) response = new ResponseEntity(HttpStatus.UNAUTHORIZED);
        else if (!eventService.doesEventExist(eventID)) response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        ResponseEntity exist = attendService.getAttendees(eventID);
        if (exist.getStatusCode() != HttpStatus.FOUND) {
            System.out.println("DELETE /admin/validate/id=" + eventID + " => " + exist.getStatusCode());
            return exist;
        }
        ArrayList<User> attendees = (ArrayList<User>) exist.getBody();
        response = attendService.validateAttendees(attendees);
        System.out.println("DELETE /admin/validate/id=" + eventID + " => " + response.getStatusCode());
        return response;
    }
}