package miniproject.TripPlannerProject.controllers;

import java.io.StringReader;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@RestController
@RequestMapping
public class DemoTrainFailRESTController {

    private String call_fake_train_alert = "http://localhost:8080/fakeAPI/demo888";
    private JsonObject rootObject = Json.createObjectBuilder()
            .add("odata.metadata",
                    "http://datamall2.mytransport.sg/ltaodataservice/$metadata#TrainServicesAlerts")
            .add("value", Json.createObjectBuilder()
                    .add("Status", 2)
                    .add("AffectedSegments", Json.createArrayBuilder()
                            .add(Json.createObjectBuilder()
                                    .add("Line", "NEL")
                                    .add("Direction", "HarbourFront")
                                    .add("Stations", "NE9,NE8,NE7,NE6")
                                    .add("FreePublicBus", "NE9,NE8,NE7,NE6")
                                    .add("FreeMRTShuttle",
                                            "NE9,NE8,NE7,NE6")
                                    .add("MRTShuttleDirection",
                                            "HarbourFront"))
                            .add(Json.createObjectBuilder()
                                    .add("Line", "NSL")
                                    .add("Direction", "Both")
                                    .add("Stations", "NS9,NS8,NS7,NS6,NS13,NS10,NS11,NS12")
                                    .add("FreePublicBus",
                                            "NS9,NS8,NS7,NS6,NS13,NS10,NS11,NS12")
                                    .add("FreeMRTShuttle",
                                            "NS9,NS8,NS7,NS6,NS13")
                                    .add("MRTShuttleDirection", "Both")))
                    .add("Message", Json.createArrayBuilder()
                            .add(Json.createObjectBuilder()
                                    .add("Content",
                                            "0901hrs : NSL - Additional travelling time of 20 minutes between Yishun and Kranji stations in both directions due to a signal fault.")
                                    .add("CreatedDate",
                                            "2018-03-16 09:01:53"))
                            .add(Json.createObjectBuilder()
                                    .add("Content",
                                            "0813hrs : NEL - Additional travelling time of 20 minutes between Boon Keng and Dhoby Ghuat stations towards HarbourFront station due to a signal fault.")
                                    .add("CreatedDate",
                                            "2018-03-16 09:01:53"))))
            .build();

    @GetMapping(path = "/fakeAPI/demo888", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createAccount() {
        System.out.println("fake server is called");
        return ResponseEntity.status(200)
                .body(rootObject.toString());

    }

}
