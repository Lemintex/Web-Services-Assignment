package com.event.event;

import com.event.event.model.Attend;
import com.event.event.model.Event;
import com.event.event.repository.AttendRepository;
import com.event.event.repository.EventRepository;
import netscape.javascript.JSObject;
import org.json.JSONObject;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class EventApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(AttendRepository attendRepository, EventRepository eventRepository) {
        return args -> {
            attendRepository.deleteAll();
            eventRepository.deleteAll();
//            RestTemplate restTemplate = new RestTemplate();
//            String url = "https://pmaier.eu.pythonanywhere.com/random-user";
//            for (int i = 0; i < 8; i++) {
//                try {
//                    ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
//                    JSONObject jsonObject = new JSONObject(response.getBody());
//                    JSONObject user = jsonObject.getJSONObject("user");
//                    String userID = user.getString("uid");
//                    System.out.println(jsonObject);
//                    attendRepository.save(new Attend((int) Math.ceil(Math.random()*3)+10, userID));
//                } catch (HttpStatusCodeException e) {
//                    System.out.println(e);
//                }
//            }
            Event e3 = new Event("alpha", "location", "date", "time", "duration", 1);
            e3.setAttending(1);
            attendRepository.save(new Attend(3, "imposter"));
            Event e4 = new Event("bravo", "location", "date", "time", "duration", 10);
            e4.setAttending(1);
            attendRepository.save(new Attend(4, "imposter"));

            eventRepository.save(e3);
            eventRepository.save(e4);
            eventRepository.save(new Event("charlie", "location", "date", "time", "duration", 10));
            eventRepository.save(new Event("delta", "location", "date", "time", "duration", 1));
        };
    }

}
