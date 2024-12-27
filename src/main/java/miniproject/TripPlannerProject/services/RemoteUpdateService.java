package miniproject.TripPlannerProject.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import miniproject.TripPlannerProject.models.BusDetails;
import miniproject.TripPlannerProject.models.TrainDetails;
import miniproject.TripPlannerProject.repositories.DirectonsRepository;

@Service
public class RemoteUpdateService {

    @Autowired
    private DirectionsService dirService;

    @Autowired
    private DirectonsRepository repo;

    public void updateBusDetailInRepo(String username, String keyid, List<BusDetails> oldToMod) {
        System.out.println("CHECK HERE old: " + oldToMod.get(0).toString());
        List<BusDetails> updated = updateBusDetail(oldToMod);
        System.out.println("CHECK HERE new: " + updated.get(0).toString());

        // prep to insert repo
        JsonArrayBuilder busArrayBuilder = Json.createArrayBuilder();
        if (updated.size() != 0) {
            for (BusDetails bd : updated) {
                JsonObject ob = bd.BusDetailtoJsonObject();
                busArrayBuilder.add(ob);
            }
            JsonArray busArray = busArrayBuilder.build();

            repo.updateExistingBusInfo(username, keyid, busArray);

        }
    }

    public void updateTrainDetailInRepo(String username, String keyid, List<TrainDetails> oldToMod) {

        List<TrainDetails> updated = updateTrainDetail(oldToMod);

        // prep to insert repo
        JsonArrayBuilder trainArrayBuilder = Json.createArrayBuilder();
        if (updated.size() != 0) {
            for (TrainDetails td : updated) {
                JsonObject ob = td.trainDetailToJsonObject();
                trainArrayBuilder.add(ob);
            }
            JsonArray trainArray = trainArrayBuilder.build();

            repo.updateTrainInfo(username, keyid, trainArray);

        }
    }

    

    // call bus api
    public List<BusDetails> updateBusDetail(List<BusDetails> oldFromRoute) {

        for (int i = 0; i < oldFromRoute.size(); i++) {
            BusDetails old = oldFromRoute.get(i);
            String busCodeStop = old.getBusStopCode();
            String busNo = old.getName();
            String preserveBusStopName = old.getBusStopName();
            String preserveArrivalStop = old.getArrivalStop();
            String preserveNumStops = old.getNumStops();

            JsonObject tempObj = dirService.retrieveBusInfoTimings(busCodeStop, busNo); // call api
            BusDetails newRef = dirService.processBusJson(tempObj, busNo); // update old
            System.out.println("HEY HEY HEY HERE NEW: " + newRef.toString());
          
            // set back possible missinginfo
            old.setBusStopName(preserveBusStopName);
            old.setArrivalStop(preserveArrivalStop);
            old.setNumStops(preserveNumStops);
            old.setName(busNo);
            old.setBusStopCode(busCodeStop);
            old.setEta1(newRef.getEta1());
            old.setEta2(newRef.getEta2());
            old.setEta3(newRef.getEta3());
            old.setLoad1(newRef.getLoad1());
            old.setLoad2(newRef.getLoad2());
            old.setLoad3(newRef.getLoad3());
            old.setBusType1(newRef.getBusType1());
            old.setBusType2(newRef.getBusType2());
            old.setBusType3(newRef.getBusType3());
            old.setDataFetchedOn(newRef.getDataFetchedOn());

        }

        return oldFromRoute;
    }

        // call train api
        public List<TrainDetails> updateTrainDetail(List<TrainDetails> oldTrainFromRoute) {

            for (int i = 0; i < oldTrainFromRoute.size(); i++) {
                TrainDetails old = oldTrainFromRoute.get(i);
                String preserveDepartureStop = old.getDepartureStop();
                String preserveTrainLine = old.getTrainLine();
                String preserveToward = old.getTrainToward();
                String preserveArrStop = old.getArrivalStop();
                String preserveNumStop = old.getNumStops();
                String preserveStationCode = old.getTrainStationCode();

                old = dirService.retrieveTrainRealTime(old);
                old.setDepartureStop(preserveDepartureStop);
                old.setTrainLine(preserveTrainLine);
                old.setTrainToward(preserveToward);
                old.setArrivalStop(preserveArrStop);
                old.setNumStops(preserveNumStop);
                old.setTrainStationCode(preserveStationCode);
    
            }
    
            return oldTrainFromRoute;
        }

}
