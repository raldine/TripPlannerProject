package miniproject.TripPlannerProject.models;


import org.springframework.stereotype.Component;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

@Component
public class ResultObj {

    private String keyid;
    private String resultObj;
    private String departTime;
    private String arrivalTime;
    private String totalDuration;
    private String totalDistance;

    public String getKeyid() {
        return keyid;
    }

    public void setKeyid(String keyid) {
        this.keyid = keyid;
    }

    public String getResultObj() {
        return resultObj;
    }

    public void setResultObj(String resultObj) {
        this.resultObj = resultObj;
    }

    public String getDepartTime() {
        return departTime;
    }

    public void setDepartTime(String departTime) {
        this.departTime = departTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }


    public String getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(String totalDuration) {
        this.totalDuration = totalDuration;
    }

    public String getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(String totalDistance) {
        this.totalDistance = totalDistance;
    }


    
    @Override
    public String toString() {
        return "ResultObj [keyid=" + keyid + ", resultObj=" + resultObj + ", departTime=" + departTime
                + ", arrivalTime=" + arrivalTime + ", totalDuration=" + totalDuration + ", totalDistance="
                + totalDistance + "]";
    }

// Static method to create a ResultObj from a JsonObject
// Static method to create a ResultObj from a JsonObject
public static ResultObj fromJsonObjectToResult(JsonObject jsonObject) {
    ResultObj resultObj = new ResultObj();

    if (jsonObject.containsKey("keyid")) {
        resultObj.keyid = jsonObject.getString("keyid");
    } else {
        resultObj.keyid = "N/A";
    }

    if (jsonObject.containsKey("resultObj")) {
        resultObj.resultObj = jsonObject.getString("resultObj");
    } else {
        resultObj.resultObj = "N/A";
    }

    if (jsonObject.containsKey("departTime")) {
        resultObj.departTime = jsonObject.getString("departTime");
    } else {
        resultObj.departTime = "N/A";
    }

    if (jsonObject.containsKey("arrivalTime")) {
        resultObj.arrivalTime = jsonObject.getString("arrivalTime");
    } else {
        resultObj.arrivalTime = "N/A";
    }

    if (jsonObject.containsKey("totalDuration")) {
        resultObj.totalDuration = jsonObject.getString("totalDuration");
    } else {
        resultObj.totalDuration = "N/A";
    }

    if (jsonObject.containsKey("totalDistance")) {
        resultObj.totalDistance = jsonObject.getString("totalDistance");
    } else {
        resultObj.totalDistance = "N/A";
    }

    return resultObj;
}


    // Convert ResultObj to JsonObject
    public JsonObject toJsonObject() {
        JsonObjectBuilder builder = Json.createObjectBuilder();

        if (keyid != null) {
            builder.add("keyid", keyid);
        }

        if (resultObj != null) {
            builder.add("resultObj", resultObj);
        }

        if (departTime != null) {
            builder.add("departTime", departTime);
        }

        if (arrivalTime != null) {
            builder.add("arrivalTime", arrivalTime);
        }

        if (totalDuration != null) {
            builder.add("totalDuration", totalDuration);
        }

        if (totalDistance != null) {
            builder.add("totalDistance", totalDistance);
        }


        return builder.build();
    }

}
