package miniproject.TripPlannerProject.services;

import java.io.IOException;
import java.io.StringReader;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import miniproject.TripPlannerProject.models.BusDetails;

import miniproject.TripPlannerProject.models.LocationRequest;
import miniproject.TripPlannerProject.models.QuickView;
import miniproject.TripPlannerProject.models.ResultObj;
import miniproject.TripPlannerProject.models.TrainCodeToStation;
import miniproject.TripPlannerProject.models.TrainDetails;
import miniproject.TripPlannerProject.models.TrainService;
import miniproject.TripPlannerProject.models.TransitDetails;
import miniproject.TripPlannerProject.repositories.DirectonsRepository;
import miniproject.TripPlannerProject.repositories.UserDetailsRepo;

@Service
public class DirectionsService {

        @Autowired
        private DirectonsRepository repo;

        @Autowired
        private UserService userService;

        @Value("${google.api.key}")
        private String googleKey;

        private String get_bus_code = "https://data.busrouter.sg/v1/stops.min.json";
        private String get_bus_ETA = "https://datamall2.mytransport.sg/ltaodataservice/v3/BusArrival";
        private String get_train_RealTimeCrowd = "https://datamall2.mytransport.sg/ltaodataservice/PCDRealTime";
        private String get_train_Service_Alert = "https://datamall2.mytransport.sg/ltaodataservice/TrainServiceAlerts";

        // to insert later
        @Value("${own.server.demo.api}")
        private String call_fake_train_alert;

        // to replace with insertion later
        @Value("${lta.api.one}")
        private String ltaAPI1;

        @Value("${lta.api.two}")
        private String ltaAPI2;


        public static JsonArray listOfCodes;

        public static JsonArray getListOfCodes() {
                return listOfCodes;
        }

        public static void setListOfCodes(JsonArray listOfCodes) {
                DirectionsService.listOfCodes = listOfCodes;
        }

        public String retrieveBusCode(String busStopName, String depLat, String depLng) {

                String result = "Not Found";
        
                JsonArray toRead = listOfCodes;
        
                for (int i = 0; i < toRead.size(); i++) {
                    JsonObject read = toRead.getJsonObject(i);
                    String name = read.getString("Name").toLowerCase().trim().replaceAll("[^a-zA-Z0-9]", "");
                    String matchname = busStopName.toLowerCase().trim().replaceAll("[^a-zA-Z0-9]", "");
                    String busStopLat = String.valueOf(read.getJsonNumber("Latitude").doubleValue());
                    String busStopLng = String.valueOf(read.getJsonNumber("Longitude").doubleValue());
        
                    if ((depLat.startsWith(busStopLat) || depLat.contains(busStopLat))
                            && (depLng.startsWith(busStopLng) || depLng.contains(busStopLng))) {
        
                        System.out.println("id from latlng match:");
                        result = read.getString("ID");
        
                    } else if (matchname.equals(name)) {
                        System.out.println("id from name exact:");
                        result = read.getString("ID");
        
                    } 
                    // else if (result.equals("Not Found") &&
                    //         matchname.contains(name) ||
                    //         name.startsWith(matchname.toLowerCase())) {
                    //     System.out.println("id from name slight match:");
                    //     result = read.getString("ID");
        
                    // }
        
                }
                return result;
            }
        
        

        // get list of busstop codes @@@@Change this to load Bus codes file into a fixed
        // JsonArray tht is read
        // @@@@ static, returns JsonArray
        public JsonObject getBusStopCodes() {

                JsonObject result = null;

                String getBusCodeUrl = UriComponentsBuilder
                                .fromUriString(get_bus_code)
                                .toUriString();

                RequestEntity<Void> req = RequestEntity
                                .get(getBusCodeUrl)
                                // .header("AccountKey", "fc9gZM/bTiSihnmKGWlWVw==") // TO INSERT LATER
                                .accept(MediaType.APPLICATION_JSON)
                                .build();

                RestTemplate template = new RestTemplate();

                ResponseEntity<String> resp;

                try {

                        // actual exchange:
                        resp = template.exchange(req, String.class); // String.class as we are expecting JsonObject
                                                                     // String

                        // read what has been gotten
                        String payload = resp.getBody(); // pure data in string
                        JsonReader reader = Json.createReader(new StringReader(payload)); // read in Json for received
                                                                                          // String

                        JsonObject wholeResult = reader.readObject();
                        result = wholeResult;
                        JsonArray valuesArray = wholeResult.getJsonArray("value");

                        for (int i = 0; i < valuesArray.size(); i++) {

                                JsonObject eachBusStop = valuesArray.getJsonObject(i);
                                System.out.println(eachBusStop.getString("BusStopCode"));
                                System.out.println(eachBusStop.getString("Description"));

                        }

                } catch (Exception ex) {

                        ex.printStackTrace();
                        System.out.println(ex.getMessage());
                }
                return result;

        }

        // process whole Result (result from Google directions service) result from
        // DireService --> TransitDetails list
        public List<TransitDetails> processObjectReader(String result) {

                // Extract Transit Details only
                List<TransitDetails> extract = new LinkedList<>();

                // read the object
                JsonReader reader = Json.createReader(new StringReader(result));
                JsonObject wholeResult = reader.readObject();

                // for API and start --> change latlng to actual place name
                GeoApiContext context = new GeoApiContext.Builder()
                                .apiKey(googleKey)
                                .build();

                JsonArray routesArray = wholeResult.getJsonArray("routes");
                System.out.println("Total Routes: " + routesArray.size());
                for (int x = 0; x < routesArray.size(); x++) {
                        // route[]
                        JsonObject route = routesArray.getJsonObject(x);
                        // overall polyline
                        // System.out.println("Overview Polyline: " +
                        // route.getJsonObject("overview_polyline").getString("points"));

                        // route[] --> legs[]
                        JsonArray legs = route.getJsonArray("legs");

                        for (int y = 0; y < legs.size(); y++) {

                                JsonObject leg = legs.getJsonObject(y);
                                System.out.println("Leg Start Address: " + leg.getString("start_address"));
                                System.out.println("Leg End Address: " + leg.getString("end_address"));
                                System.out.println(
                                                "Total Distance: " + leg.getJsonObject("distance").getString("text")); // e.g.,
                                                                                                                       // "12.4
                                                                                                                       // km"
                                System.out.println(
                                                "Total Duration: " + leg.getJsonObject("duration").getString("text")); // e.g.,
                                                                                                                       // "15
                                                                                                                       // mins"

                                // Format time (departure and arrival)
                                String departureTime = leg.getJsonObject("departure_time").getString("text");
                                String arrivalTime = leg.getJsonObject("arrival_time").getString("text");
                                System.out.println("Start Departure Time: " + departureTime);
                                System.out.println("Estimated Arrival Time: " + arrivalTime);

                                // route[] --> legs[] --> steps[]
                                JsonArray steps = leg.getJsonArray("steps");
                                for (int i = 0; i < steps.size(); i++) {
                                        // each step in steps[]
                                        JsonObject step = steps.getJsonObject(i);

                                        // for extaction
                                        TransitDetails temp = new TransitDetails();
                                        System.out.println("Step Instruction: " + step.getString("instructions"));
                                        System.out.println("Step Distance: "
                                                        + step.getJsonObject("distance").getString("text"));

                                        if (step.getString("travel_mode").equals("TRANSIT")) {
                                                JsonObject transitDetails = step.getJsonObject("transit");
                                                System.out.println("Step Transit Details: Depart From: "
                                                                + transitDetails.getJsonObject("departure_stop")
                                                                                .getString("name"));
                                                // add
                                                temp.setDepartureStopName(transitDetails.getJsonObject("departure_stop")
                                                                .getString("name"));
                                                // add lat lng
                                                temp.setDepartLat(String
                                                                .valueOf(transitDetails.getJsonObject("departure_stop")
                                                                                .getJsonObject("location")
                                                                                .getJsonNumber("lat").doubleValue()));
                                                temp.setDepartLng(String
                                                                .valueOf(transitDetails.getJsonObject("departure_stop")
                                                                                .getJsonObject("location")
                                                                                .getJsonNumber("lng").doubleValue()));

                                                if (transitDetails.containsKey("headsign")) {
                                                        System.out.println("Ride towards: "
                                                                        + transitDetails.getString("headsign"));

                                                        temp.setTrainTowards(transitDetails.getString("headsign"));
                                                }

                                                // add
                                                temp.setArrivalStopName(transitDetails.getJsonObject("arrival_stop")
                                                                .getString("name"));

                                                System.out.println("Ride til stop name: "
                                                                + transitDetails.getJsonObject("arrival_stop")
                                                                                .getString("name"));

                                                // add
                                                temp.setArrivalStopName(transitDetails.getJsonObject("arrival_stop")
                                                                .getString("name"));

                                                System.out.println(
                                                                "Departure Time: " + transitDetails
                                                                                .getJsonObject("departure_time")
                                                                                .getString("text"));
                                                // add
                                                temp.setDepatureTime(transitDetails.getJsonObject("departure_time")
                                                                .getString("text"));

                                                System.out.println(
                                                                "Arrival Time: " + transitDetails
                                                                                .getJsonObject("arrival_time")
                                                                                .getString("text"));

                                                System.out.println("Transport Line: " + transitDetails
                                                                .getJsonObject("line").getString("name"));
                                                // add
                                                temp.setTransportName(
                                                                transitDetails.getJsonObject("line").getString("name"));

                                                System.out.println("Transport provided by "
                                                                + transitDetails.getJsonObject("line")
                                                                                .getJsonArray("agencies")
                                                                                .getJsonObject(0).getString("name"));
                                                // add
                                                temp.setTransportProvider(transitDetails.getJsonObject("line")
                                                                .getJsonArray("agencies").getJsonObject(0)
                                                                .getString("name"));

                                                System.out.println("Number of stops: "
                                                                + transitDetails.getInt("num_stops"));

                                                // add
                                                temp.setNumStops(String.valueOf(transitDetails.getInt("num_stops")));

                                                System.out.println("Ride for Duration Of: "
                                                                + step.getJsonObject("duration").getString("text"));

                                                // get vehicle type
                                                String vehicleType = transitDetails.getJsonObject("line")
                                                                .getJsonObject("vehicle")
                                                                .getString("type");
                                                // add
                                                temp.setVehicleType(transitDetails.getJsonObject("line")
                                                                .getJsonObject("vehicle")
                                                                .getString("type"));
                                                System.out.println(">>>>>> vehicle type is " + vehicleType);

                                                // add to list
                                                extract.add(temp);

                                        } else if (step.getString("travel_mode").equals("WALKING")) {
                                                // each substeps in step(walking)

                                                JsonArray substeps = step.getJsonArray("steps");
                                                for (int o = 0; o < substeps.size(); o++) {
                                                        JsonObject substep = substeps.getJsonObject(o);
                                                        System.out.println("Walking Substep:");
                                                        LatLng sLLatLng = new LatLng(
                                                                        substep.getJsonObject("start_location")
                                                                                        .getJsonNumber("lat")
                                                                                        .doubleValue(),
                                                                        substep.getJsonObject("start_location")
                                                                                        .getJsonNumber("lng")
                                                                                        .doubleValue());
                                                        System.out.println(
                                                                        "Start Walking From: " + sLLatLng.toString());
                                                        try {
                                                                GeocodingResult[] startLocate = GeocodingApi
                                                                                .newRequest(context) // print street
                                                                                                     // name
                                                                                                     // for walking
                                                                                .latlng(sLLatLng)
                                                                                .await();
                                                                System.out.println(startLocate[0].formattedAddress);

                                                        } catch (Exception ex) {
                                                                ex.printStackTrace();
                                                        }

                                                        if (substep.containsKey("instructions")) {
                                                                System.out.println(
                                                                                "Walking Instructions: " + substep
                                                                                                .getString("instructions")
                                                                                                .toString());
                                                        }
                                                        System.out.println(
                                                                        "Walking Distance: " + substep
                                                                                        .getJsonObject("distance")
                                                                                        .getString("text"));
                                                        System.out.println(
                                                                        "Walking Duration: " + substep
                                                                                        .getJsonObject("duration")
                                                                                        .getString("text"));

                                                        LatLng eLLatLng = new LatLng(
                                                                        substep.getJsonObject("end_location")
                                                                                        .getJsonNumber("lat")
                                                                                        .doubleValue(),
                                                                        substep.getJsonObject("end_location")
                                                                                        .getJsonNumber("lng")
                                                                                        .doubleValue());
                                                        System.out.println(
                                                                        "Walk To: " + eLLatLng.toString());
                                                        try {
                                                                GeocodingResult[] startLocate = GeocodingApi
                                                                                .newRequest(context) // print street
                                                                                                     // name
                                                                                                     // for walking
                                                                                .latlng(eLLatLng)
                                                                                .await();
                                                                System.out.println(startLocate[0].formattedAddress);

                                                        } catch (Exception ex) {
                                                                ex.printStackTrace();
                                                        }

                                                }

                                        }
                                }

                        }

                }
                return extract;
        }

        // process Buses and Trains Details to SAVE to Repo (TransitDetails --> Bus
        // details and Train details)
        public List<BusDetails> processResultObj(String username, ResultObj result, LocationRequest requested)
                        throws IOException {

                System.out.println("this is from service side >>>>>>>");
                System.out.println("result id is " + result.getKeyid());
                // System.out.println("result obj is " + result.getResultObj());
                System.out.println("this is from service side >>>>>>>");
                System.out.println("locationrequest id is " + requested.getKeyid());

                // get overall depature and arrival time --> set to ResultObj
                JsonReader reader = Json.createReader(new StringReader(result.getResultObj()));
                JsonObject wholeResult = reader.readObject();

                JsonArray routes = wholeResult.getJsonArray("routes");
                JsonObject route = routes.getJsonObject(0);
                JsonArray legs = route.getJsonArray("legs");
                JsonObject leg = legs.getJsonObject(0);

                String departureDateTime = convertToFormattedDate(
                                leg.getJsonObject("departure_time").getString("value"));
                String arrivalDateTime = convertToFormattedDate(leg.getJsonObject("arrival_time").getString("value"));
                result.setArrivalTime(arrivalDateTime); // in dd-MM-yyyy hh:mm a
                result.setDepartTime(departureDateTime);

                // set total distance and duration
                result.setTotalDuration(leg.getJsonObject("duration").getString("text"));
                result.setTotalDistance(leg.getJsonObject("distance").getString("text"));

                //////////////

                // make sense of directions JSON --> extract transitDetails
                List<TransitDetails> readTransports = new LinkedList<>();
                readTransports = processObjectReader(result.getResultObj());

                // get Bus Details
                System.out.println("important transport details: >>>>>>>");

                // extract bus stop codes
                List<String> busStopCodes = new LinkedList<>();
                List<String> busStopNames = new LinkedList<>();
                List<String> busNumbers = new LinkedList<>();
                List<String> busArrStop = new LinkedList<>();
                List<String> busNumStops = new LinkedList<>();
                int countOfBuses = 0;
                List<BusDetails> busesInfo = new LinkedList<>();
        

                // train processing
                TrainCodeToStation trainMatch = new TrainCodeToStation();
                List<TrainDetails> finalTrainList = new LinkedList<>();

                for (TransitDetails td : readTransports) {

                        System.out.println(td.toString());
                        if (td.getVehicleType().toLowerCase().equals("bus")) {
                                String busCodes = retrieveBusCode(td.getDepartureStopName(), td.getDepartLat(),
                                                td.getDepartLng()); // retrieve buscodes
                                System.out.println(busCodes);
                                busNumbers.add(td.getTransportName());
                                busStopNames.add(td.getDepartureStopName());
                                busArrStop.add(td.getArrivalStopName());
                                busStopCodes.add(busCodes);
                                busNumStops.add(td.getNumStops());
                                countOfBuses++;
                        } else if (td.getVehicleType().toLowerCase().equals("subway")
                                        || td.getVehicleType().toLowerCase().equals("tram")) {

                                TrainDetails tempNew = new TrainDetails();
                                // make new TrainDetail one object
                                // set trainstationName -> get td.getDepartureStopName
                                tempNew.setDepartureStop(td.getDepartureStopName());
                                // set train line
                                tempNew.setTrainLine(td.getTransportName());
                                // set headsign
                                tempNew.setTrainToward(td.getTrainTowards());
                                // set trainArrStationName -> get td.getArrStopName
                                tempNew.setArrivalStop(td.getArrivalStopName());

                                // set Numstops
                                tempNew.setNumStops(td.getNumStops());
                                // do trainMatch to get trainStationCode -> set StationCode
                                String stationCodeFromName = trainMatch.returnCode(td.getDepartureStopName(),
                                                td.getTransportName());
                                tempNew.setTrainStationCode(stationCodeFromName);
                                // do api realtimeCrowdLevel -> put in trainDetailObj, return modded
                                tempNew = retrieveTrainRealTime(tempNew); // call api

                                // add final train detail obj to finalTrainList
                                finalTrainList.add(tempNew);
                                System.out.println("SERVICE SIDE>>>>>>>>>>>> CHECK IF MOD CORRECTLY:");
                                System.out.println(tempNew.toString());

                        }
                }

                // iterate with bus number, busstop code to get bus ETA info
                for (int i = 0; i < countOfBuses; i++) {

                        BusDetails temp = new BusDetails();
                        JsonObject tempObj = retrieveBusInfoTimings(busStopCodes.get(i), busNumbers.get(i)); // CALL API
                        temp = processBusJson(tempObj, busNumbers.get(i)); // process api result
                        temp.setBusStopName(busStopNames.get(i));
                        temp.setArrivalStop(busArrStop.get(i));
                        temp.setNumStops(busNumStops.get(i));
                        busesInfo.add(temp);

                }

                prepAllForInsertion(username, requested, result, busesInfo, finalTrainList);

                return busesInfo; // to remove

        }

        // insert to repo PLUS PRODUCE QUICKVIEW
        public void prepAllForInsertion(String username, LocationRequest req, ResultObj result,
                        List<BusDetails> busesInfo, List<TrainDetails> trainsInfo) {
                // SAVE
                // assign "created on DateTime"
                ZoneOffset zoneOffset = ZoneOffset.ofHours(8); // GMT+08:00
                LocalDateTime currDateTime = LocalDateTime.now(ZoneId.of("Asia/Singapore"));
                ZonedDateTime cZonedDateTime = currDateTime.atOffset(zoneOffset).toZonedDateTime(); // Attach the zone
                Instant instantC = cZonedDateTime.toInstant();
                long createdTimeLong = instantC.toEpochMilli();
                String createdTimeLongString = String.valueOf(createdTimeLong);

                // quickView stuff
                QuickView newToInsert = new QuickView();
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
                String formattedDate = currDateTime.format(dateFormatter);
                String formattedTime = currDateTime.format(timeFormatter);
                String formattedDateTime = formattedDate + "\n" + formattedTime;
                newToInsert.setCreatedOn(formattedDateTime);
                newToInsert.setOriginName(req.getoName());
                newToInsert.setDestName(req.getdName());
                String departTime = result.getDepartTime();
                String arrTime = result.getArrivalTime();
                String datePart = departTime.substring(0, 10);
                String departTimepart = departTime.substring(11, 19);
                String arrtimepart = arrTime.substring(11, 19);
                String finalTime = datePart + "\n" + departTimepart + " to " + arrtimepart;
                newToInsert.setPrefTime(finalTime);
                List<String> tempList = req.getTransportModes();
                StringBuilder tempSB = new StringBuilder();
                for (String s : tempList) {
                        tempSB.append(s);
                        tempSB.append(", ");
                }
                String almost = tempSB.toString();
                String trimmed = almost.trim();
                String finalStr = trimmed.substring(0, trimmed.length() - 1);
                newToInsert.setPrefTransport(finalStr);
                String forBuses = "";
                if (busesInfo.size() != 0) {
                        for (BusDetails bd : busesInfo) {
                                forBuses = forBuses + bd.getName() + ", ";
                        }
                } else {
                        forBuses = "N/A";
                }
                if (!forBuses.equals("N/A")) {
                        String trimmedbus = forBuses.trim();
                        String finalForbus = trimmedbus.substring(0, trimmedbus.length() - 1);
                        newToInsert.setBuses(finalForbus);

                } else {
                        newToInsert.setBuses("N/A");
                }
                List<String> perTrainString = new LinkedList<>();
                if (trainsInfo.size() != 0) {
                        for (TrainDetails td : trainsInfo) {
                                String toAdd = td.getTrainStationCode() + " " + td.getDepartureStop() + " to "
                                                + td.getArrivalStop();
                                perTrainString.add(toAdd);
                        }

                        StringBuilder trainFinal = new StringBuilder();

                        for (int i = 0; i < perTrainString.size(); i++) {
                                trainFinal.append(perTrainString.get(i));
                                if (i < perTrainString.size() - 1) { // Avoid adding "\n" after the last element
                                        trainFinal.append("\n");
                                }
                        }
                        String trainFinalStringBuilt = trainFinal.toString();

                        newToInsert.setTrainCodeAndName(trainFinalStringBuilt);
                } else {
                        newToInsert.setTrainCodeAndName("N/A");
                }
                newToInsert.setTrainTowards("N/A");
                newToInsert.setKeyid(req.getKeyid());
                newToInsert.setPrefTravelOp(req.getPref());
                System.out.println("from service !!@@@@@@@@@@@@@ " + newToInsert.toString());

                JsonObject quickViewInJson = newToInsert.quickViewToJsonObject();
                userService.putQuickViewIntoRepo(username, quickViewInJson);
                //////// end of quickView

                // to put in repo" LocationRequest, Result, Bus or/And Train details,
                // createdTime
                // get username
                String checkUsername = username;
                // get key id
                String id = req.getKeyid();
                // req to Json
                JsonObject reqObj = req.LRtoJsonObject();
                // result to Json
                JsonObject resultJObj = result.toJsonObject();
                // businfo to JsonArray of busJson + trainlist to JsonArray
                JsonArrayBuilder busArrayBuilder = Json.createArrayBuilder();
                JsonArrayBuilder trainArrayBuilder = Json.createArrayBuilder();

                // update userRouteList
                userService.updateUserRouteList(checkUsername, id);

                // insert to repo (if bus details have, no train details)
                // THIS IS FOR ONLY ROUTE WITH BUS DETAILS ONLY
                if (busesInfo.size() != 0 && trainsInfo.size() == 0) {
                        for (BusDetails bd : busesInfo) {
                                JsonObject ob = bd.BusDetailtoJsonObject();
                                busArrayBuilder.add(ob);
                        }
                        JsonArray busArray = busArrayBuilder.build();
                        repo.insertAllBusOnly(checkUsername, id, reqObj, resultJObj, busArray, createdTimeLongString);
                }

                // insert to repo (if train details only)
                if (busesInfo.size() == 0 && trainsInfo.size() != 0) {
                        for (TrainDetails td : trainsInfo) {
                                JsonObject ob = td.trainDetailToJsonObject();
                                trainArrayBuilder.add(ob);
                        }
                        JsonArray trainArray = trainArrayBuilder.build();
                        repo.insertAllTrainOnly(checkUsername, id, reqObj, resultJObj, trainArray,
                                        createdTimeLongString);
                }
                // insert to repo (if bus and train)
                if (busesInfo.size() != 0 && trainsInfo.size() != 0) {
                        for (BusDetails bd : busesInfo) {
                                JsonObject ob = bd.BusDetailtoJsonObject();
                                busArrayBuilder.add(ob);
                        }
                        JsonArray busArray = busArrayBuilder.build();

                        for (TrainDetails td : trainsInfo) {
                                JsonObject ob = td.trainDetailToJsonObject();
                                trainArrayBuilder.add(ob);
                        }
                        JsonArray trainArray = trainArrayBuilder.build();
                        repo.insertAllBusTrain(checkUsername, id, reqObj, resultJObj, busArray, trainArray,
                                        createdTimeLongString);
                }

        }

        // BusInfo API
        public JsonObject retrieveBusInfoTimings(String busStopCode, String busNo) {

                JsonObject result = null;

                List<String> apiKeys = new LinkedList<>();
                apiKeys.add(ltaAPI1);
                apiKeys.add(ltaAPI2);

                String getURLFinal = UriComponentsBuilder
                                .fromUriString(get_bus_ETA)
                                .queryParam("BusStopCode", busStopCode)
                                .queryParam("ServiceNo", busNo)
                                .toUriString();

                RestTemplate template = new RestTemplate();

                ResponseEntity<String> resp;

                for (String apiKey : apiKeys) {
                        RequestEntity<Void> req = RequestEntity
                                        .get(getURLFinal)
                                        .header("AccountKey", apiKey)
                                        .accept(MediaType.APPLICATION_JSON)
                                        .build();

                        try {

                                // actual exchange:
                                resp = template.exchange(req, String.class); // String.class as we are expecting
                                                                             // JsonObject
                                                                             // String

                                // read what has been gotten
                                String payload = resp.getBody(); // pure data in string

                                if (resp.getStatusCode().is2xxSuccessful() && payload != null) {
                                        JsonReader reader = Json.createReader(new StringReader(payload)); // read in
                                                                                                          // Json for
                                                                                                          // received
                                                                                                          // String
                                        result = reader.readObject();
                                        processBusJson(result, busNo);
                                        return result;
                                }

                        } catch (Exception ex) {
                                System.out.println("Error with API key " + apiKey + ": " + ex.getMessage());
                                ex.printStackTrace();

                        }

                }
                return result;
        }

        // TRAIN REALTIME CROWD API
        public TrainDetails retrieveTrainRealTime(TrainDetails trainDetailsToMod) {
                String trainLine = TrainCodeToStation.returnTrainLineForAPI(trainDetailsToMod.getTrainStationCode());

                String getURLFinal = UriComponentsBuilder
                                .fromUriString(get_train_RealTimeCrowd)
                                .queryParam("TrainLine", trainLine)
                                .toUriString();

                List<String> apiKeys = new LinkedList<>();
                apiKeys.add(ltaAPI2);
                apiKeys.add(ltaAPI1);

                RestTemplate template = new RestTemplate();
                ResponseEntity<String> resp;

                for (String apiKey : apiKeys) {
                        RequestEntity<Void> req = RequestEntity
                                        .get(getURLFinal)
                                        .header("AccountKey", apiKey)
                                        .accept(MediaType.APPLICATION_JSON)
                                        .build();

                        try {

                                resp = template.exchange(req, String.class);

                                String payload = resp.getBody();

                                if (resp.getStatusCode().is2xxSuccessful() && payload != null) {
                                        JsonReader reader = Json.createReader(new StringReader(payload));
                                        JsonObject fromRTCapi = reader.readObject();

                                        trainDetailsToMod = processTrainRealTimeJson(fromRTCapi, trainDetailsToMod);
                                        return trainDetailsToMod;
                                }

                        } catch (Exception ex) {

                                System.out.println("Error with API key " + apiKey + ": " + ex.getMessage());
                                ex.printStackTrace();
                        }
                }

                return trainDetailsToMod;
        }

        // TRAINS Service Check
        public TrainService retrieveTrainServiceStatus(String keyid, String user) {

                TrainService result = new TrainService();

                // this condition for demo only
                if (keyid.equals("508c2f2b") && user.equals("freddy")) {
                        System.out.println("fake call to fake api activated");
                        // DEMO ONLY FOR FAKE TRAIN FAILURE RESPONSE - call own server acting as train
                        // api with disruption repsonse
                        String getFAKEURLFinal = UriComponentsBuilder
                                        .fromUriString(call_fake_train_alert)
                                        .toUriString();

                        RestTemplate template = new RestTemplate();
                        ResponseEntity<String> resp;

                        RequestEntity<Void> req = RequestEntity
                                        .get(getFAKEURLFinal)
                                        .accept(MediaType.APPLICATION_JSON)
                                        .build();

                        try {
                                resp = template.exchange(req, String.class);

                                String payload = resp.getBody();

                                if (resp.getStatusCode().is2xxSuccessful() && payload != null) {
                                        System.out.println("gotten reply from FAKE server");
                                        JsonReader reader = Json.createReader(new StringReader(payload));
                                        JsonObject fromServiceAlert = reader.readObject();
                                        JsonObject valueObj = fromServiceAlert.getJsonObject("value");

                                        int status = valueObj.getInt("Status");
                                        JsonArray affectedSegmentsArray = valueObj
                                                        .getJsonArray("AffectedSegments");
                                        JsonArray messagesArray = valueObj.getJsonArray("Message");
                                        System.out.println("check message array: " + messagesArray.toString());
                                        result = processTrainServiceAPIresult(status, affectedSegmentsArray,
                                                        messagesArray);

                                        System.out.println("processed TrainService " + result.toString());
                                        return result; // RESPONSE FAKE TRAIN BREAKDOWN FROM FAKE SERVER
                                }

                        } catch (Exception ex) {

                                ex.printStackTrace();
                                System.out.println(ex.getMessage());

                        }

                } else {
                        System.out.println("calling LTA service alert");
                        String getURLFinal = UriComponentsBuilder
                                        .fromUriString(get_train_Service_Alert)
                                        .toUriString();

                        List<String> apiKeys = new LinkedList<>();
                        apiKeys.add(ltaAPI2);
                        apiKeys.add(ltaAPI1);

                        RestTemplate template = new RestTemplate();
                        ResponseEntity<String> resp;

                        for (String apiKey : apiKeys) {
                                RequestEntity<Void> req = RequestEntity
                                                .get(getURLFinal)
                                                .header("AccountKey", apiKey)
                                                .accept(MediaType.APPLICATION_JSON)
                                                .build();

                                try {

                                        resp = template.exchange(req, String.class);

                                        String payload = resp.getBody();

                                        if (resp.getStatusCode().is2xxSuccessful() && payload != null) {
                                                System.out.println("gotten reply from REAL train server");
                                                JsonReader reader = Json.createReader(new StringReader(payload));
                                                JsonObject fromServiceAlert = reader.readObject();
                                                JsonObject valueObj = fromServiceAlert.getJsonObject("value");

                                                int status = valueObj.getInt("Status");
                                                JsonArray affectedSegmentsArray = valueObj
                                                                .getJsonArray("AffectedSegments");
                                                JsonArray messagesArray = valueObj.getJsonArray("Message");
                                                System.out.println("check message array: " + messagesArray.toString());
                                                result = processTrainServiceAPIresult(status, affectedSegmentsArray,
                                                                messagesArray);

                                                System.out.println("processed TrainService " + result.toString());
                                                return result; // CORRECT REPSONSE FROM API

                                        }

                                } catch (Exception ex) {

                                        System.out.println("Error with API key " + apiKey + ": " + ex.getMessage());
                                        ex.printStackTrace();
                                        System.out.println(ex.getMessage());
                                }
                        } // end of keys loop with correct calling of real api
                } // end of else condition - switch between fake api and real

                return result;
        }

        public TrainService processTrainServiceAPIresult(int status, JsonArray afSegArray, JsonArray messagesArray) {
                TrainService obj = new TrainService();

                obj.setStatus(status);

                if (status == 2) {
                        obj.setStatusInString("Disrupted");
                        Map<String, List<String>> affectedanddirectionMultiLines = new HashMap<>();
                        List<String> justAffectedLines = new LinkedList<>();

                        // affectedSegmentsArray
                        // get one segment == one line
                        for (int i = 0; i < afSegArray.size(); i++) {
                                JsonObject oneSeg = afSegArray.getJsonObject(i);
                                String direction = oneSeg.getString("Direction");
                                String stationsAff = oneSeg.getString("Stations");
                                String line = oneSeg.getString("Line");
                                String[] stationsA = stationsAff.split(",");
                                List<String> affStationsPerLinePlusDirection = new LinkedList<>();

                                for (String station : stationsA) {
                                        String code = station;
                                        String keyIdentifier = code + "@" + direction;
                                        affStationsPerLinePlusDirection.add(keyIdentifier);

                                }

                                justAffectedLines.add(line);
                                affectedanddirectionMultiLines.put(line, affStationsPerLinePlusDirection);

                        }

                        obj.setAffectedLinesAndDirections(affectedanddirectionMultiLines);
                        obj.setAffectedLines(justAffectedLines);

                }

                if (status == 1 && messagesArray.size() != 0) {
                        obj.setStatus(1);
                        obj.setStatusInString("Recovered");
                        ;
                        List<String> messagesString = new LinkedList<>();
                        // put in reverse because latest message is first in original
                        for (int o = 0; o < messagesArray.size(); o++) {
                                JsonObject oneMessagObject = messagesArray.getJsonObject(o);
                                String messageContent = oneMessagObject.getString("Content");
                                messagesString.add(messageContent);
                        }

                        obj.setMessages(messagesString);

                }

                if (status == 2 && messagesArray.size() != 0) {
                        List<String> messagesString = new LinkedList<>();

                        for (int o = 0; o < messagesArray.size(); o++) {
                                JsonObject oneMessagObject = messagesArray.getJsonObject(o);
                                String messageContent = oneMessagObject.getString("Content");
                                messagesString.add(messageContent);
                        }

                        obj.setMessages(messagesString);

                }

                if (status == 1 && messagesArray.size() == 0) {
                        obj.setStatus(1);
                        obj.setStatusInString("Normal");
                }

                return obj;
        }

        // reflect trainServiceStatus with results from TrainService Obj
        public List<TrainDetails> modTrainDetailsServiceStatus(TrainService resultFromApiProcessing,
                        List<TrainDetails> listOfTrains) {

                                //NE6@Harbourfront ==> TrainDetail
                Map<String, TrainDetails> tempToMatch = new HashMap<>();

                for (TrainDetails oneTrain : listOfTrains) {
                        String trainCodefromTd = oneTrain.getTrainStationCode();
                        String towardfromTd = oneTrain.getTrainToward();
                        String oneTDidentifier = trainCodefromTd + "@" + towardfromTd;
                        tempToMatch.put(oneTDidentifier, oneTrain);

                }

                System.out.println("@@@@@@@@ CHECK MATCHING OF AFFECTED LINES THIS IS " + tempToMatch.toString());

                if (resultFromApiProcessing.getStatusInString().equals("Disrupted")) {

                        // retrieve affectedanddirection
                        Map<String, List<String>> manyLines = resultFromApiProcessing.getAffectedLinesAndDirections();
                        System.out.println("MANY LINES WHAT IS IT" + manyLines.toString());

                        // get key from map (actual api response)
                        Set<String> mapKeys = manyLines.keySet();

                        List<String> allAffectedFromAPI  = new LinkedList<>();
                        //oneList
                        for(String key : mapKeys){

                                List<String> toExtract = manyLines.get(key);

                                for(String inside: toExtract){
                                        String temp = inside.toLowerCase();

                                        if(temp.endsWith("both")){

                                                temp = temp.replaceAll("@both", "");
                                        }

                                        allAffectedFromAPI.add(temp);
                                }


                        }

                        System.out.println("EDITED LIST IS IS ISI SIISISIS " + allAffectedFromAPI.toString());

                        // get keys for tempToMatch (the trainsinfo we trying to eval)
                        Set<String> keysForTD = tempToMatch.keySet();
                        System.out.println("Get keys of train details" + keysForTD.toString());

                        //GET ONE traindetail --> 
                        for(String tdIdentifier : keysForTD){

                                TrainDetails toEval =  tempToMatch.get(tdIdentifier);

                                for(int i = 0; i < allAffectedFromAPI.size(); i++){
                                        String toCompareFromAPI = allAffectedFromAPI.get(i);
                                        System.out.println("Now comparing the route " + tdIdentifier + " with " + toCompareFromAPI);

                                        if(tdIdentifier.toLowerCase().equals(toCompareFromAPI)){
                                                System.out.println("exact match");
                                                toEval.setServStatus("Disrupted");
                                                System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@ CHANGED: " + toEval.toString());
                                        }
                                        
                                        if(toEval.getTrainStationCode().toLowerCase().equals(toCompareFromAPI)){
                                                System.out.println("match in @both");
                                                toEval.setServStatus("Disrupted");
                                                System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@ CHANGED: " + toEval.toString());

                                        }

                                }



                        }

                }
                for (TrainDetails oneTrain : listOfTrains) {

                        System.out.println(oneTrain.getDepartureStop() + " towards " + oneTrain.getTrainToward()
                                        + " is " + oneTrain.getServStatus());
                }

                return listOfTrains;
        }

        // pure BusJsonObject to our own Model BusDetail
        public BusDetails processBusJson(JsonObject busJson, String serviceNo) {

                BusDetails result = new BusDetails();
                ZoneOffset zoneOffset = ZoneOffset.ofHours(8); // GMT+08:00
                LocalDateTime currDateTime = LocalDateTime.now(ZoneId.of("Asia/Singapore"));
                ZonedDateTime cZonedDateTime = currDateTime.atOffset(zoneOffset).toZonedDateTime(); // Attach the zone
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a");
                String formattedDate = cZonedDateTime.format(formatter);
                result.setDataFetchedOn(formattedDate); // Get current time in Singapore timezone);

                if (busJson.containsKey("BusStopCode")) {
                        result.setBusStopCode(busJson.getString("BusStopCode")); // setBusCode
                }

                // get Services Array
                if (busJson.containsKey("Services")) {

                        JsonArray servicesArray = busJson.getJsonArray("Services");

                        for (int i = 0; i < servicesArray.size(); i++) {

                                JsonObject oneServiceObj = servicesArray.getJsonObject(i);
                                result.setName(serviceNo);
                                if (oneServiceObj.containsKey("NextBus")) {
                                        JsonObject bus1 = oneServiceObj.getJsonObject("NextBus");
                                        result.setEta1(timeConversion(bus1.getString("EstimatedArrival"),
                                                        cZonedDateTime));
                                        result.setLoad1(loadConversion(bus1.getString("Load")));
                                        result.setBusType1(busTypeConversion(bus1.getString("Type")));
                                }

                                if (oneServiceObj.containsKey("NextBus2")) {
                                        JsonObject bus2 = oneServiceObj.getJsonObject("NextBus2");
                                        result.setEta2(timeConversion(bus2.getString("EstimatedArrival"),
                                                        cZonedDateTime));
                                        result.setLoad2(loadConversion(bus2.getString("Load")));
                                        result.setBusType2(busTypeConversion(bus2.getString("Type")));
                                }

                                if (oneServiceObj.containsKey("NextBus3")) {
                                        JsonObject bus3 = oneServiceObj.getJsonObject("NextBus3");
                                        result.setEta3(timeConversion(bus3.getString("EstimatedArrival"),
                                                        cZonedDateTime));
                                        result.setLoad3(loadConversion(bus3.getString("Load")));
                                        result.setBusType3(busTypeConversion(bus3.getString("Type")));
                                }

                        }

                }

                return result;
        }

        // conversion Of current time how many mins to bus ETA
        public String timeConversion(String eta, ZonedDateTime fetchedTime) {
                String minutes = "N/A";
                String minutesFinal = "N/A";

                if (!eta.equals("")) {
                        ZonedDateTime etaTime = ZonedDateTime.parse(eta);
                        long minutesCalc = Duration.between(fetchedTime, etaTime).toMinutes();

                        minutes = String.valueOf(minutesCalc);
                        // System.out.println("minutes calc is " + minutes);
                        if (minutes.equals("0")) {
                                minutes = "Arr";
                        }
                        if (minutes.contains("-")) {
                                minutes = "Left";
                        }
                        if (minutes.equals("")) {
                                minutes = "N/A";
                        }
                        // System.out.println("minutes now is " + minutes);
                }

                if (minutes.equals("Arr") || minutes.equals("Left") || minutes.equals("N/A")) {
                        minutesFinal = minutes;
                } else {
                        minutesFinal = minutes + "min(s)";
                }

                return minutesFinal;
        }

        // for BusDetails
        public String loadConversion(String code) {

                String result = "N/A";

                if (code.equals("SEA")) {
                        result = "Seats available";

                } else if (code.equals("SDA")) {
                        result = "Standing needed";
                } else if (code.equals("LSD")) {
                        result = "Almost full OR Full";

                }

                return result;
        }

        // For BusDetails
        public String busTypeConversion(String code) {

                String result = "N/A";

                if (code.equals("SD")) {
                        result = "Single";

                } else if (code.equals("DD")) {
                        result = "Doubledeck";
                } else if (code.equals("BD")) {
                        result = "Bendy";

                }

                return result;
        }

        // format: "2024-12-21T06:59:23.000Z" to "21-12-2024 6:59 PM"
        public static String convertToFormattedDate(String inputDateString) {
                Instant instant = Instant.parse(inputDateString);

                ZonedDateTime dateTime = instant.atZone(ZoneId.of("Asia/Singapore"));

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a");
                return dateTime.format(formatter);
        }

        // format: "2024-12-25T15:50:00+08:00" to "dd-MM-yyyy hh:mm a"
        public static String convertTrainTimeToDate(String inputDateString) {

                ZonedDateTime zonedDateTime = ZonedDateTime.parse(inputDateString);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a");

                String formattedDateTime = zonedDateTime.format(formatter);

                return formattedDateTime;

        }

        public TrainDetails processTrainRealTimeJson(JsonObject trainRealTimeJsonOneLine,
                        TrainDetails trainDetailToMod) {

                ZoneOffset zoneOffset = ZoneOffset.ofHours(8); // GMT+08:00
                LocalDateTime currDateTime = LocalDateTime.now(ZoneId.of("Asia/Singapore"));
                ZonedDateTime cZonedDateTime = currDateTime.atOffset(zoneOffset).toZonedDateTime(); // Attach the zone
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a");
                String formattedDate = cZonedDateTime.format(formatter);
                trainDetailToMod.setDataFetchedOn(formattedDate); // Get current time in Singapore timezone);
                String trainStatCode = trainDetailToMod.getTrainStationCode();

                if (trainRealTimeJsonOneLine.containsKey("value")) {
                        JsonArray valuesArray = trainRealTimeJsonOneLine.getJsonArray("value");

                        for (int i = 0; i < valuesArray.size(); i++) {
                                JsonObject temp = valuesArray.getJsonObject(i);
                                if (temp.getString("Station").equals(trainStatCode)) {
                                        String startIntervalRaw = temp.getString("StartTime");
                                        String endIntervalRaw = temp.getString("EndTime");
                                        String crowdLevelC = temp.getString("CrowdLevel");

                                        String startIntervalwD = convertTrainTimeToDate(startIntervalRaw);
                                        String endIntervalwD = convertTrainTimeToDate(endIntervalRaw);
                                        String datePart = startIntervalwD.substring(0, 10);
                                        String starttimePart = startIntervalwD.substring(11, 19); // From index 11 to 17
                                        String endtimePart = endIntervalwD.substring(11, 19);

                                        String finalInterval = datePart + ", " + starttimePart + " - " + endtimePart;
                                        trainDetailToMod.setRealTimeCrowdInterval(finalInterval);

                                        String realTimeCrowdLevel = "N/A";

                                        if (crowdLevelC.equals("l")) {
                                                realTimeCrowdLevel = "Low";

                                        } else if (crowdLevelC.equals("m")) {

                                                realTimeCrowdLevel = "Moderate";

                                        } else if (crowdLevelC.equals("h")) {

                                                realTimeCrowdLevel = "High";

                                        }

                                        trainDetailToMod.setRealTimeCrowdLevel(realTimeCrowdLevel);

                                }
                        }

                }

                return trainDetailToMod;
        }

}
