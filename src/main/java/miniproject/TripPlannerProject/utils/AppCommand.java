package miniproject.TripPlannerProject.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonReader;
import miniproject.TripPlannerProject.services.DirectionsService;

@Component
public class AppCommand implements CommandLineRunner {

    @Autowired
    private DirectionsService service;

    @Value("${bus.stop.path}")
    private String filePath;

    @Override
    public void run(String... args) throws FileNotFoundException, IOException {

           File file = new File(filePath);
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
        service.setListOfCodes(inJson);
      
    }


    
}
