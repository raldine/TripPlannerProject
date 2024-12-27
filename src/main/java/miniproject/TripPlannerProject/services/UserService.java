package miniproject.TripPlannerProject.services;



import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import miniproject.TripPlannerProject.models.UsernamePassword;
import miniproject.TripPlannerProject.repositories.UserDetailsRepo;

@Service
public class UserService {

    @Autowired
    private UserDetailsRepo userrepo;

    // to replace with insertion later
    private final String checkCreateURL = "http://localhost:8080/authen/checkCreate";
    private final String checkLoginURL = "http://localhost:8080/authen/checkLogin";

    public JsonObject receivedUserDetailToJson(UsernamePassword details) {

        JsonObject result = Json.createObjectBuilder()
                .add("username", details.getUsername())
                .add("password", details.getPassword())
                .build();

        return result;
    }

    public String callToCreateAcc(UsernamePassword details) {

        String reply = "Error";

        JsonObject converted = receivedUserDetailToJson(details);

        // call my restController

        RequestEntity<String> req = RequestEntity
                .post(checkCreateURL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.TEXT_PLAIN)
                .body(converted.toString(), String.class);

        RestTemplate template = new RestTemplate();

        try {
            ResponseEntity<String> resp = template.exchange(req, String.class);
            if (resp.getStatusCode().toString().equals("201")) {
                String receivedPayload = resp.getBody();
                reply = receivedPayload; // "OK"

            }

        } catch (HttpClientErrorException ex) {

            HttpStatusCode statusCode = ex.getStatusCode();
            String status = statusCode.toString();
            System.out.println(ex.getResponseBodyAsString());

            // username exists
            if (status.equals("409")) {
                String errorPayload = ex.getResponseBodyAsString();
                reply = errorPayload; // "User already exists, use another username"

            }

            return reply;
        }

        return reply;

    }

    public String callToCheckLogin(UsernamePassword details) {

        String reply = "Error";

        JsonObject converted = receivedUserDetailToJson(details);

        // call my restController

        RequestEntity<String> req = RequestEntity
                .post(checkLoginURL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.TEXT_PLAIN)
                .body(converted.toString(), String.class);

        RestTemplate template = new RestTemplate();

        try {
            ResponseEntity<String> resp = template.exchange(req, String.class);
            if (resp.getStatusCode().toString().equals("202")) {
                String receivedPayload = resp.getBody();
                reply = receivedPayload; // "OK"

            }

        } catch (HttpClientErrorException ex) {

            HttpStatusCode statusCode = ex.getStatusCode();
            String status = statusCode.toString();

            // username exists
            if (status.equals("401")) {
                String errorPayload = ex.getResponseBodyAsString();
                reply = errorPayload; ////"Either Username does not exist or Password is wrong"

            }

            return reply;
        }

        return reply;

    }

    public void updateUserRouteList(String username, String keyid){

        userrepo.updateUserRouteList(username, keyid);

    }

    public List<String> getUserRouteList(String username){

        List<Object> fromRepo = userrepo.getUserRouteList(username);
        List<String> finaList = new LinkedList<>();

        for(Object o :  fromRepo){

            String temp = o.toString();
            finaList.add(temp);

        }


        return finaList;

        
    }


}
