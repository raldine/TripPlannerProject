package miniproject.TripPlannerProject.models;
import org.springframework.stereotype.Component;

import jakarta.json.Json;
import jakarta.json.JsonObject;

@Component
public class BusDetails {

    private String name = "N/A";
    private String busStopName = "N/A";
    private String Eta1 = "N/A";
    private String Eta2 = "N/A";
    private String Eta3 = "N/A";

    private String Load1 = "N/A";
    private String Load2 = "N/A";
    private String Load3 = "N/A";

    private String busType1 = "N/A";
    private String busType2 = "N/A";
    private String busType3 = "N/A";

    private String busStopCode = "N/A";

    private String dataFetchedOn = "N/A";

    private String arrivalStop = "N/A";

    private String numStops = "N/A";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEta1() {
        return Eta1;
    }

    public void setEta1(String eta1) {
        Eta1 = eta1;
    }

    public String getEta2() {
        return Eta2;
    }

    public void setEta2(String eta2) {
        Eta2 = eta2;
    }

    public String getEta3() {
        return Eta3;
    }

    public void setEta3(String eta3) {
        Eta3 = eta3;
    }

    public String getLoad1() {
        return Load1;
    }

    public void setLoad1(String load1) {
        Load1 = load1;
    }

    public String getLoad2() {
        return Load2;
    }

    public void setLoad2(String load2) {
        Load2 = load2;
    }

    public String getLoad3() {
        return Load3;
    }

    public void setLoad3(String load3) {
        Load3 = load3;
    }

    public String getBusType1() {
        return busType1;
    }

    public void setBusType1(String busType1) {
        this.busType1 = busType1;
    }

    public String getBusType2() {
        return busType2;
    }

    public void setBusType2(String busType2) {
        this.busType2 = busType2;
    }

    public String getBusType3() {
        return busType3;
    }

    public void setBusType3(String busType3) {
        this.busType3 = busType3;
    }

    public String getBusStopCode() {
        return busStopCode;
    }

    public void setBusStopCode(String busStopCode) {
        this.busStopCode = busStopCode;
    }

    public String getBusStopName() {
        return busStopName;
    }

    public void setBusStopName(String busStopName) {
        this.busStopName = busStopName;
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

    public void setDataFetchedOn(String dataFetchedOn) {
        this.dataFetchedOn = dataFetchedOn;
    }

    public String getDataFetchedOn() {
        return dataFetchedOn;
    }

    public JsonObject BusDetailtoJsonObject() {
        return Json.createObjectBuilder()
                .add("name", name)
                .add("busStopName", busStopName)
                .add("Eta1", Eta1)
                .add("Eta2", Eta2)
                .add("Eta3", Eta3)
                .add("Load1", Load1)
                .add("Load2", Load2)
                .add("Load3", Load3)
                .add("busType1", busType1)
                .add("busType2", busType2)
                .add("busType3", busType3)
                .add("busStopCode", busStopCode)
                .add("dataFetchedOn", dataFetchedOn)
                .add("arrivalStop", arrivalStop)
                .add("numStops", numStops)
                .build();
    }

    public static BusDetails fromJsonObjectToBD(JsonObject jsonObject) {
        BusDetails busDetails = new BusDetails();

        if (jsonObject.containsKey("name")) {
            busDetails.name = jsonObject.getString("name");
        } else {
            busDetails.name = "N/A";
        }

        if (jsonObject.containsKey("busStopName")) {
            busDetails.busStopName = jsonObject.getString("busStopName");
        } else {
            busDetails.busStopName = "N/A";
        }

        if (jsonObject.containsKey("Eta1")) {
            busDetails.Eta1 = jsonObject.getString("Eta1");
        } else {
            busDetails.Eta1 = "N/A";
        }

        if (jsonObject.containsKey("Eta2")) {
            busDetails.Eta2 = jsonObject.getString("Eta2");
        } else {
            busDetails.Eta2 = "N/A";
        }

        if (jsonObject.containsKey("Eta3")) {
            busDetails.Eta3 = jsonObject.getString("Eta3");
        } else {
            busDetails.Eta3 = "N/A";
        }

        if (jsonObject.containsKey("Load1")) {
            busDetails.Load1 = jsonObject.getString("Load1");
        } else {
            busDetails.Load1 = "N/A";
        }

        if (jsonObject.containsKey("Load2")) {
            busDetails.Load2 = jsonObject.getString("Load2");
        } else {
            busDetails.Load2 = "N/A";
        }

        if (jsonObject.containsKey("Load3")) {
            busDetails.Load3 = jsonObject.getString("Load3");
        } else {
            busDetails.Load3 = "N/A";
        }

        if (jsonObject.containsKey("busType1")) {
            busDetails.busType1 = jsonObject.getString("busType1");
        } else {
            busDetails.busType1 = "N/A";
        }

        if (jsonObject.containsKey("busType2")) {
            busDetails.busType2 = jsonObject.getString("busType2");
        } else {
            busDetails.busType2 = "N/A";
        }

        if (jsonObject.containsKey("busType3")) {
            busDetails.busType3 = jsonObject.getString("busType3");
        } else {
            busDetails.busType3 = "N/A";
        }

        if (jsonObject.containsKey("busStopCode")) {
            busDetails.busStopCode = jsonObject.getString("busStopCode");
        } else {
            busDetails.busStopCode = "N/A";
        }

        if (jsonObject.containsKey("dataFetchedOn")) {
            busDetails.dataFetchedOn = jsonObject.getString("dataFetchedOn");
        } else {
            busDetails.dataFetchedOn = "N/A";
        }

        if (jsonObject.containsKey("arrivalStop")) {
            busDetails.arrivalStop = jsonObject.getString("arrivalStop");
        } else {
            busDetails.arrivalStop = "N/A";
        }

        if (jsonObject.containsKey("numStops")) {
            busDetails.numStops = jsonObject.getString("numStops");
        } else {
            busDetails.numStops = "N/A";
        }

        return busDetails;
    }

    @Override
    public String toString() {
        return "BusDetails [name=" + name + ", busStopName=" + busStopName + ", Eta1=" + Eta1 + ", Eta2=" + Eta2
                + ", Eta3=" + Eta3 + ", Load1=" + Load1 + ", Load2=" + Load2 + ", Load3=" + Load3 + ", busType1="
                + busType1 + ", busType2=" + busType2 + ", busType3=" + busType3 + ", busStopCode=" + busStopCode
                + ", dataFetchedOn=" + dataFetchedOn + ", arrivalStop=" + arrivalStop + ", numStops=" + numStops + "]";
    }

    

}
