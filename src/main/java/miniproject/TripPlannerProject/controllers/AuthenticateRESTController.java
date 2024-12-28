package miniproject.TripPlannerProject.controllers;

import java.io.StringReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import miniproject.TripPlannerProject.services.AuthenService;

import org.springframework.http.MediaType;

@RestController
@RequestMapping("/authen")
public class AuthenticateRESTController {

    @Autowired
    public AuthenService authserv;

    @PostMapping(path = "/checkCreate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createAccount(
            @RequestBody String payload) {

        JsonReader reader = Json.createReader(new StringReader(payload));
        JsonObject jsonobj = reader.readObject();
        // System.out.println("from authen " + jsonobj.toString());

        String username = jsonobj.getString("username").toLowerCase();
        String password = jsonobj.getString("password");

        String reply = authserv.checkAndCreate(username, password);
        System.out.println("AUTHEN REST CONTROLLER MESSAGE FROM REPO " + reply);

        // make ok reply in JsonObject
        JsonObject okbody = Json.createObjectBuilder()
                .add("message", "OK")
                .build();

        // make bad reply in JsonObject
        JsonObject badbody = Json.createObjectBuilder()
                .add("message", "User already exists, use another username.")
                .build();

        if (reply.equals("OK")) {

            return ResponseEntity.status(201)
                    .body(okbody.toString());
        } else {

            return ResponseEntity.status(202)
                    .body(badbody.toString()); // ""User already exists, use another username""
        }

    }

    @PostMapping(path = "/checkLogin", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> loginAuthen(
            @RequestBody String payload) {

        JsonReader reader = Json.createReader(new StringReader(payload));
        JsonObject jsonobj = reader.readObject();
        System.out.println("received on checkLogin " + jsonobj.toString());
        String username = jsonobj.getString("username").toLowerCase();
        String password = jsonobj.getString("password");

        String reply = authserv.checkLogin(username, password);
        System.out.println("checkLogin received this from repo " + reply);

        // make ok reply in JsonObject
        JsonObject okbody = Json.createObjectBuilder()
                .add("message", "OK")
                .build();

        // make bad reply in JsonObject
        JsonObject badbody = Json.createObjectBuilder()
                .add("message", "Either Username does not exist or Password is wrong.")
                .build();

        if (reply.equals("OK")) {

            return ResponseEntity.status(202)
                    .body(okbody.toString());
        } else {

            return ResponseEntity.status(202)
                    .body(badbody.toString()); // "Either Username does not exist or Password is wrong."
        }

    }

}
