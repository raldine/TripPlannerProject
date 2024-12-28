package miniproject.TripPlannerProject.models;

import org.springframework.stereotype.Component;

import jakarta.json.Json;
import jakarta.json.JsonObject;

@Component
public class QuickView {

    private String createdOn;
    private String originName;
    private String destName;
    private String prefTime;
    private String prefTravelOp;
    private String prefTransport;
    private String buses;
    private String trainCodeAndName;
    private String trainTowards;
    private String keyid;

    @Override
    public String toString() {
        return "QuickView [createdOn=" + createdOn + ", originName=" + originName + ", destName=" + destName
                + ", prefTime=" + prefTime + ", prefTravelOp=" + prefTravelOp + ", prefTransport=" + prefTransport
                + ", buses=" + buses + ", trainCodeAndName=" + trainCodeAndName + ", trainTowards=" + trainTowards
                + "]";
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getOriginName() {
        return originName;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }

    public String getDestName() {
        return destName;
    }

    public void setDestName(String destName) {
        this.destName = destName;
    }

    public String getPrefTime() {
        return prefTime;
    }

    public void setPrefTime(String prefTime) {
        this.prefTime = prefTime;
    }

    public String getPrefTravelOp() {
        return prefTravelOp;
    }

    public void setPrefTravelOp(String prefTravelOp) {
        this.prefTravelOp = prefTravelOp;
    }

    public String getPrefTransport() {
        return prefTransport;
    }

    public void setPrefTransport(String prefTransport) {
        this.prefTransport = prefTransport;
    }

    public String getBuses() {
        return buses;
    }

    public void setBuses(String buses) {
        this.buses = buses;
    }

    public String getTrainCodeAndName() {
        return trainCodeAndName;
    }

    public void setTrainCodeAndName(String trainCodeAndName) {
        this.trainCodeAndName = trainCodeAndName;
    }

    public String getTrainTowards() {
        return trainTowards;
    }

    public void setTrainTowards(String trainTowards) {
        this.trainTowards = trainTowards;
    }

    public String getKeyid() {
        return keyid;
    }

    public void setKeyid(String keyid) {
        this.keyid = keyid;
    }

    public JsonObject quickViewToJsonObject() {

        return Json.createObjectBuilder()
                .add("createdOn", createdOn)
                .add("originName", originName)
                .add("destName", destName)
                .add("prefTime", prefTime)
                .add("prefTravelOp", prefTravelOp)
                .add("prefTransport", prefTransport)
                .add("buses", buses)
                .add("trainCodeAndName", trainCodeAndName)
                .add("trainTowards", trainTowards)
                .add("keyid", keyid)
                .build();
    }

    public static QuickView fromJsonToQuickView(JsonObject jsonObject) {
        QuickView result = new QuickView();

        if (jsonObject.containsKey("createdOn")) {
            result.setCreatedOn(jsonObject.getString("createdOn"));
        } else {
            result.setCreatedOn("N/A");
        }

        if (jsonObject.containsKey("originName")) {
            result.setOriginName(jsonObject.getString("originName"));
        } else {
            result.setOriginName("N/A");
        }

        if (jsonObject.containsKey("destName")) {
            result.setDestName(jsonObject.getString("destName"));
        } else {
            result.setDestName("N/A");
        }
        if (jsonObject.containsKey("prefTime")) {
            result.setPrefTime(jsonObject.getString("prefTime"));
        } else {
            result.setPrefTime("N/A");
        }
        if (jsonObject.containsKey("prefTravelOp")) {
            result.setPrefTravelOp(jsonObject.getString("prefTravelOp"));
        } else {
            result.setPrefTravelOp("N/A");
        }
        if (jsonObject.containsKey("prefTransport")) {
            result.setPrefTransport(jsonObject.getString("prefTransport"));
        } else {
            result.setPrefTransport("N/A");
        }
        if (jsonObject.containsKey("buses")) {
            result.setBuses(jsonObject.getString("buses"));
        } else {
            result.setBuses("N/A");
        }
        if (jsonObject.containsKey("trainCodeAndName")) {
            result.setTrainCodeAndName(jsonObject.getString("trainCodeAndName"));
        } else {
            result.setTrainCodeAndName("N/A");
        }
        if (jsonObject.containsKey("trainTowards")) {
            result.setTrainTowards(jsonObject.getString("trainTowards"));
        } else {
            result.setTrainTowards("N/A");
        }
        if (jsonObject.containsKey("keyid")) {
            result.setKeyid(jsonObject.getString("keyid"));
        } else {
            result.setKeyid("N/A");
        }

        return result;
    }
}
