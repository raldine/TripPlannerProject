package miniproject.TripPlannerProject.models;



import java.util.Arrays;

import org.springframework.stereotype.Component;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;

@Component
public class TrainDetails {

    private String departureStop = "N/A";
    private String trainLine = "N/A";
    private String arrivalStop = "N/A";
    private String numStops = "N/A";
    private String trainStationCode = "N/A";
    private String trainToward = "N/A";
    private String dataFetchedOn = "N/A";
    private String realTimeCrowdInterval = "N/A";
    private String realTimeCrowdLevel = "N/A";


    private String servStatus = "Normal"; //to change when called ServiceAlertApi

    public String getDepartureStop() {
        return departureStop;
    }

    public void setDepartureStop(String departureStop) {
        this.departureStop = departureStop;
    }

    public String getTrainLine() {
        return trainLine;
    }

    public void setTrainLine(String trainLine) {
        this.trainLine = trainLine;
    }

    public String getArrivalStop() {
        return arrivalStop;
    }

    public void setArrivalStop(String arrivalStop) {
        this.arrivalStop = arrivalStop;
    }

    public String getNumStops() {
        return numStops;
    }

    public void setNumStops(String numStops) {
        this.numStops = numStops;
    }

    public String getTrainStationCode() {
        return trainStationCode;
    }

    public void setTrainStationCode(String trainStationCode) {
        this.trainStationCode = trainStationCode;
    }

    public String getTrainToward() {
        return trainToward;
    }

    public void setTrainToward(String trainToward) {
        this.trainToward = trainToward;
    }

    public String getDataFetchedOn() {
        return dataFetchedOn;
    }

    public void setDataFetchedOn(String dataFetchedOn) {
        this.dataFetchedOn = dataFetchedOn;
    }

    public String getRealTimeCrowdInterval() {
        return realTimeCrowdInterval;
    }

    public void setRealTimeCrowdInterval(String realTimeCrowdInterval) {
        this.realTimeCrowdInterval = realTimeCrowdInterval;
    }

    public String getRealTimeCrowdLevel() {
        return realTimeCrowdLevel;
    }

    public void setRealTimeCrowdLevel(String realTimeCrowdLevel) {
        this.realTimeCrowdLevel = realTimeCrowdLevel;
    }


    public String getServStatus() {
        return servStatus;
    }

    public void setServStatus(String servStatus) {
        this.servStatus = servStatus;
    }




    @Override
    public String toString() {
        return "TrainDetails [departureStop=" + departureStop + ", trainLine=" + trainLine + ", arrivalStop="
                + arrivalStop + ", numStops=" + numStops + ", trainStationCode=" + trainStationCode + ", trainToward="
                + trainToward + ", dataFetchedOn=" + dataFetchedOn + ", realTimeCrowdInterval=" + realTimeCrowdInterval
                + ", realTimeCrowdLevel=" + realTimeCrowdLevel + ", servStatus=" + servStatus + "]";
    }

    public JsonObject trainDetailToJsonObject() {

        return Json.createObjectBuilder()
                .add("departureStop", departureStop)
                .add("trainLine", trainLine)
                .add("arrivalStop", arrivalStop)
                .add("numStops", numStops)
                .add("trainStationCode", trainStationCode)
                .add("trainToward", trainToward)
                .add("dataFetchedOn", dataFetchedOn)
                .add("realTimeCrowdInterval", realTimeCrowdInterval)
                .add("realTimeCrowdLevel", realTimeCrowdLevel)
                .add("servStatus", servStatus)
                .build();
    }

    public static TrainDetails fromJsonObjectToTrainDetails(JsonObject jsonObject) {
        TrainDetails trainDetails = new TrainDetails();

        if (jsonObject.containsKey("departureStop")) {
            trainDetails.setDepartureStop(jsonObject.getString("departureStop"));
        } else {
            trainDetails.setDepartureStop("N/A");
        }

        if (jsonObject.containsKey("trainLine")) {
            trainDetails.setTrainLine(jsonObject.getString("trainLine"));
        } else {
            trainDetails.setTrainLine("N/A");
        }

        if (jsonObject.containsKey("arrivalStop")) {
            trainDetails.setArrivalStop(jsonObject.getString("arrivalStop"));
        } else {
            trainDetails.setArrivalStop("N/A");
        }

        if (jsonObject.containsKey("numStops")) {
            trainDetails.setNumStops(jsonObject.getString("numStops"));
        } else {
            trainDetails.setNumStops("N/A");
        }

        if (jsonObject.containsKey("trainStationCode")) {
            trainDetails.setTrainStationCode(jsonObject.getString("trainStationCode"));
        } else {
            trainDetails.setTrainStationCode("N/A");
        }

        if (jsonObject.containsKey("trainToward")) {
            trainDetails.setTrainToward(jsonObject.getString("trainToward"));
        } else {
            trainDetails.setTrainToward("N/A");
        }

        if (jsonObject.containsKey("dataFetchedOn")) {
            trainDetails.setDataFetchedOn(jsonObject.getString("dataFetchedOn"));
        } else {
            trainDetails.setDataFetchedOn("N/A");
        }

        if (jsonObject.containsKey("realTimeCrowdInterval")) {
            trainDetails.setRealTimeCrowdInterval(jsonObject.getString("realTimeCrowdInterval"));
        } else {
            trainDetails.setRealTimeCrowdInterval("N/A");
        }

        if (jsonObject.containsKey("realTimeCrowdLevel")) {
            trainDetails.setRealTimeCrowdLevel(jsonObject.getString("realTimeCrowdLevel"));
        } else {
            trainDetails.setRealTimeCrowdLevel("N/A");
        }


        if (jsonObject.containsKey("servStatus")) {
            trainDetails.setServStatus(jsonObject.getString("servStatus"));
        } else {
            trainDetails.setServStatus("Normal");
        }

        return trainDetails;
    }



}
