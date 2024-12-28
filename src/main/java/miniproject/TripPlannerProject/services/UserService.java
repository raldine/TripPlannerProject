package miniproject.TripPlannerProject.services;

import java.io.StringReader;
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
import jakarta.json.JsonReader;
import miniproject.TripPlannerProject.models.QuickView;
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

        String reply = "Error on Service Side";

        JsonObject converted = receivedUserDetailToJson(details);
        System.out.println("received on userservice side" + converted.toString());

        // call my restController

        RequestEntity<String> req = RequestEntity
                .post(checkCreateURL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(converted.toString(), String.class);

        RestTemplate template = new RestTemplate();

        try {
            ResponseEntity<String> resp = template.exchange(req, String.class);
            if (resp.getStatusCode().is2xxSuccessful()) {
                String receivedPayload = resp.getBody();
                

                JsonReader reader = Json.createReader(new StringReader(receivedPayload));
                JsonObject toread = reader.readObject();

                String replyFromRest = toread.getString("message");
                System.out.println("SERVICE SIDE WHEN CREATED NEW ACCOUNT REPLY FROM REST CONTROL IS " + replyFromRest);
                reply = replyFromRest;

                return reply;
            }

        } catch (HttpClientErrorException ex) {

            HttpStatusCode statusCode = ex.getStatusCode();
            String receivedPayload = ex.getResponseBodyAsString();
                

            // JsonReader reader = Json.createReader(new StringReader(receivedPayload));
            // JsonObject toread = reader.readObject();

            // String replyFromRest = toread.getString("message");
            // System.out.println("SERVICE SIDE WHEN CREATED NEW ACCOUNT REPLY FROM REST CONTROL IS " + replyFromRest);
            // reply = replyFromRest;

            // return reply;
        }

        return reply;

    }

    public String callToCheckLogin(UsernamePassword details) {

        String reply = "Error on service Side";

        JsonObject converted = receivedUserDetailToJson(details);

        // call my restController

        RequestEntity<String> req = RequestEntity
                .post(checkLoginURL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(converted.toString(), String.class);

        RestTemplate template = new RestTemplate();

        try {
            ResponseEntity<String> resp = template.exchange(req, String.class);
            System.out.println(resp.getStatusCode().toString());
            if (resp.getStatusCode().is2xxSuccessful()) {
                String receivedPayload = resp.getBody();

                JsonReader reader = Json.createReader(new StringReader(receivedPayload));
                JsonObject toread = reader.readObject();

                String replyFromRest = toread.getString("message");
                System.out.println("SERVICE SIDE WHEN LOGIN ACCOUNT REPLY FROM REST CONTROL IS " + replyFromRest);
                reply = replyFromRest;
                return reply;

            }

        } catch (HttpClientErrorException ex) {

            HttpStatusCode statusCode = ex.getStatusCode();
            String status = statusCode.toString();
            System.out.println("Login error code on user service: " + status);
            String receivedPayload = ex.getResponseBodyAsString();
            System.out.println("service what is ex.getRes " + receivedPayload);
            // JsonReader reader = Json.createReader(new StringReader(receivedPayload));
            // JsonObject toread = reader.readObject();

            // String replyFromRest = toread.getString("message");
            // System.out.println("SERVICE SIDE WHEN LOGIN ACCOUNT REPLY FROM REST CONTROL IS " + replyFromRest);
            // reply = replyFromRest;
        }

        return reply;

    }

    public void updateUserRouteList(String username, String keyid) {

        userrepo.updateUserRouteList(username, keyid);

    }

    public void putQuickViewIntoRepo(String username, JsonObject qVinJson){

        userrepo.putQuickView(username, qVinJson);
    }

    public List<String> getUserRouteList(String username) {

        List<Object> fromRepo = userrepo.getUserRouteList(username);
        List<String> finaList = new LinkedList<>();

        for (Object o : fromRepo) {

            String temp = o.toString();
            finaList.add(temp);

        }

        return finaList;

    }

    public List<QuickView> getQuickViewfromRepo(String username, List<String> userRList) {

        List<Object> allQVJsonObject = userrepo.getQuickViewObjects(username, userRList);

        List<QuickView> finalList = new LinkedList<>();

        if (allQVJsonObject.size() != 0) {
            for (Object ob : allQVJsonObject) {

                QuickView temp = new QuickView();

                JsonObject jsonTemp = Json.createReader(new StringReader(ob.toString())).readObject();

                temp = QuickView.fromJsonToQuickView(jsonTemp);

                finalList.add(temp);

            }

        }

        return finalList;
    }

    public void removeIDfromUserList(String username, String keyid){

        userrepo.removeIDfromUserRouteList(username, keyid);
    }

    public void removeAllItemsfromUser(String username, String keyid){
        userrepo.removeAllWithUserKeyID(username, keyid);

    }

}
