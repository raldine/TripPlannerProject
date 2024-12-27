package miniproject.TripPlannerProject.models;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;

import org.springframework.stereotype.Component;

import jakarta.json.Json;
import jakarta.json.JsonArray;

import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@Component
public class BusStopToCode {

    private File file = new File("src/main/resources/static/formattedData.json");
    private JsonArray listOfCodes;

    public BusStopToCode() throws IOException {

        FileReader reader = new FileReader(file);
        BufferedReader br = new BufferedReader(reader);

        StringBuilder wholePayload = new StringBuilder();
        String line;

        while ((line = br.readLine()) != null) {
            wholePayload.append(line);
        }
        br.close();

        String jsonArrayString = wholePayload.toString();

        JsonReader readerJ = Json.createReader(new StringReader(jsonArrayString));
        JsonArray inJson = readerJ.readArray();
        this.listOfCodes = inJson;
        System.out.println("bus codes initialised");

    }

    public String retrieveBusCode(String busStopName, String depLat, String depLng) {

        String result = "Not Found";

        JsonArray toRead = this.listOfCodes;

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

}
