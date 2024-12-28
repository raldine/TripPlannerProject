package miniproject.TripPlannerProject.services;

import java.io.StringReader;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import miniproject.TripPlannerProject.models.BusDetails;
import miniproject.TripPlannerProject.models.LocationRequest;
import miniproject.TripPlannerProject.models.ResultObj;
import miniproject.TripPlannerProject.models.TrainDetails;
import miniproject.TripPlannerProject.repositories.DirectonsRepository;


@Service
public class GetRepoService {
    @Autowired
    private DirectonsRepository repo;

    // REPO OPERATIONSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS
    //change to respective Object, also make all time human readable
    public Map<String, Object> retrieveAll(String username, String keyid) {

        Map<String, Object> retrievedObjects = new HashMap<>();
        retrievedObjects = repo.getAllDetails(username, keyid);

        LocationRequest request = new LocationRequest();
        ResultObj result = new ResultObj();
        List<BusDetails> busesInfo = new LinkedList<>();
        String createdOn = "";
        List<TrainDetails> trainInfo = new LinkedList<>();

        if (!retrievedObjects.isEmpty()) {

            Set<String> keys = retrievedObjects.keySet();
            for (String key : keys) {
                System.out.println("service side:");
                System.out.println(">>>>>>>> " + key);

                if (key.startsWith("request")) {
                    JsonObject locationRequest = Json
                            .createReader(new StringReader(retrievedObjects.get(key).toString())).readObject();

                    request = LocationRequest.fromJsonObjectToLR(locationRequest);
                    
                    System.out.println("parsed Location Request>> " + request);
                }

                if (key.startsWith("result")) {
                    JsonObject resultObj = Json.createReader(new StringReader(retrievedObjects.get(key).toString()))
                            .readObject();

                    result = ResultObj.fromJsonObjectToResult(resultObj);
                    // System.out.println("parsed Result Request>> " + result);
                }

                if (key.startsWith("busdetail")) {

                    JsonArray array = Json.createReader(new StringReader(retrievedObjects.get(key).toString()))
                            .readArray();
                    for (int o = 0; o < array.size(); o++) {
                        JsonObject bdObj = array.getJsonObject(o);
                        BusDetails item = BusDetails.fromJsonObjectToBD(bdObj);
                        busesInfo.add(item);
                    }
                    System.out.println("parsed ist of BusDetails>> " + busesInfo.toString());
                }

                if (key.startsWith("traindetail")) {

                    JsonArray array = Json.createReader(new StringReader(retrievedObjects.get(key).toString()))
                            .readArray();
                    for (int u = 0; u < array.size(); u++) {
                        JsonObject tdObj = array.getJsonObject(u);
                        TrainDetails item = TrainDetails.fromJsonObjectToTrainDetails(tdObj);
                        trainInfo.add(item);
                    }
                    System.out.println("parsed ist of trainDetails>> " + trainInfo.toString());
                }

                if (key.startsWith("createdon")) {
                    long createdOnRaw = Long.parseLong(retrievedObjects.get(key).toString());
                    createdOn = longToDate(createdOnRaw);
                }
            }

        }

        Map<String, Object> allObjects = new HashMap<>();
        allObjects.put("request", request);
        allObjects.put("result", result);
        allObjects.put("busdeet", busesInfo);
        allObjects.put("createdon", createdOn);
        allObjects.put("traindeet", trainInfo);

        return allObjects;

    }

    public String longToDate(long dateinepoch) {
        String formattedDate = "";

        ZonedDateTime dateTime = Instant.ofEpochMilli(dateinepoch)
                .atZone(ZoneId.of("Asia/Singapore")); // Replace with your desired time zone

        // Format the ZonedDateTime in the desired format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a");
        formattedDate = dateTime.format(formatter);

        return formattedDate;

    }


    public String toSentenceCase(String str) {
        if (str == null || str.isEmpty()) {
            return str;  
        }
        
       
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

}
