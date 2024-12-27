package miniproject.TripPlannerProject.models;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;

@Component
public class LocationRequest {

    private String keyid = "N/A";
    private String olatitude = "N/A";
    private String olongitude = "N/A";
    private String dlatitude = "N/A";
    private String dlongtitude = "N/A";
    private String pref = "N/A";

    // prep for call directions in js
    private String oLatLng = "N/A";
    private String dLatLng = "N/A";
    private String dTimeInEpoch = "11111111";
    private String aTimeInEpoch = "11111111";

    private String oName = "N/A";
    private String dName = "N/A";
    private String oForA = "N/A";
    private String dForA = "N/A";

    // both of this can be null
    // NEEED VALIDATE CANNOT HAVE BOTH/ ITS EITHER OR
    private String depTime = "N/A";

    private String arrTime = "N/A";

    private List<String> transportModes;

    private String prefTrans = "N/A";

    public String getKeyid() {
        return keyid;
    }

    public void setKeyid(String keyid) {
        this.keyid = keyid;
    }

    public String getOlatitude() {
        return olatitude;
    }

    public void setOlatitude(String olatitude) {
        this.olatitude = olatitude;
    }

    public String getOlongitude() {
        return olongitude;
    }

    public void setOlongitude(String olongitude) {
        this.olongitude = olongitude;
    }

    public String getDlatitude() {
        return dlatitude;
    }

    public void setDlatitude(String dlatitude) {
        this.dlatitude = dlatitude;
    }

    public String getDlongtitude() {
        return dlongtitude;
    }

    public void setDlongtitude(String dlongtitude) {
        this.dlongtitude = dlongtitude;
    }

    public String getPref() {
        return pref;
    }

    public void setPref(String pref) {
        this.pref = pref;
    }

    public String getDepTime() {
        return depTime;
    }

    public void setDepTime(String depTime) {
        this.depTime = depTime;
    }

    public String getArrTime() {
        return arrTime;
    }

    public void setArrTime(String arrTime) {
        this.arrTime = arrTime;
    }

    public List<String> getTransportModes() {
        return transportModes;
    }

    public void setTransportModes(List<String> transportModes) {
        this.transportModes = transportModes;
    }

    public String getoLatLng() {
        return oLatLng;
    }

    public void setoLatLng(String oLatLng) {
        this.oLatLng = oLatLng;
    }

    public String getdLatLng() {
        return dLatLng;
    }

    public void setdLatLng(String dLatLng) {
        this.dLatLng = dLatLng;
    }

    public String getdTimeInEpoch() {
        return dTimeInEpoch;
    }

    public void setdTimeInEpoch(String dTimeInEpoch) {
        this.dTimeInEpoch = dTimeInEpoch;
    }

    public String getaTimeInEpoch() {
        return aTimeInEpoch;
    }

    public void setaTimeInEpoch(String aTimeInEpoch) {
        this.aTimeInEpoch = aTimeInEpoch;
    }

    public String getPrefTrans() {
        return prefTrans;
    }

    public void setPrefTrans(String prefTrans) {
        this.prefTrans = prefTrans;
    }

    public String getoName() {
        return oName;
    }

    public void setoName(String oName) {
        this.oName = oName;
    }

    public String getdName() {
        return dName;
    }

    public void setdName(String dName) {
        this.dName = dName;
    }

    public String getoForA() {
        return oForA;
    }

    public void setoForA(String oForA) {
        this.oForA = oForA;
    }

    public String getdForA() {
        return dForA;
    }

    public void setdForA(String dForA) {
        this.dForA = dForA;
    }

    @Override
    public String toString() {
        return "LocationRequest [keyid=" + keyid + ", olatitude=" + olatitude + ", olongitude=" + olongitude
                + ", dlatitude=" + dlatitude + ", dlongtitude=" + dlongtitude + ", pref=" + pref + ", oLatLng="
                + oLatLng + ", dLatLng=" + dLatLng + ", dTimeInEpoch=" + dTimeInEpoch + ", aTimeInEpoch=" + aTimeInEpoch
                + ", oName=" + oName + ", dName=" + dName + ", oForA=" + oForA + ", dForA=" + dForA + ", depTime="
                + depTime + ", arrTime=" + arrTime + ", transportModes=" + transportModes + ", prefTrans=" + prefTrans
                + "]";
    }

    public void prepForCall() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        // Assuming GMT+08:00 for input time
        ZoneOffset zoneOffset = ZoneOffset.ofHours(8); // GMT+08:00

        if (!this.depTime.isEmpty()) {
            LocalDateTime dLocalDateTime = LocalDateTime.parse(depTime, formatter);
            ZonedDateTime dZonedDateTime = dLocalDateTime.atOffset(zoneOffset).toZonedDateTime(); // Attach the zone
            Instant instantD = dZonedDateTime.toInstant();
            this.dTimeInEpoch = String.valueOf(instantD.toEpochMilli());
            System.out.println(depTime + " changed to " + this.dTimeInEpoch);

        } else if (!this.arrTime.isEmpty()) {
            LocalDateTime aLocalDateTime = LocalDateTime.parse(arrTime, formatter);
            ZonedDateTime aZonedDateTime = aLocalDateTime.atOffset(zoneOffset).toZonedDateTime(); // Attach the zone
            Instant instantA = aZonedDateTime.toInstant();
            this.aTimeInEpoch = String.valueOf(instantA.toEpochMilli());
            System.out.println(arrTime + " changed to " + this.aTimeInEpoch);

        }

        if (!this.transportModes.isEmpty()) {

            if (transportModes.size() == 2) {

                this.prefTrans = "both";
            } else if (transportModes.contains("bus")) {

                this.prefTrans = "bus";
            } else if (transportModes.contains("train")) {

                this.prefTrans = "rail";
            }

            System.out.println("this request preference is " + this.prefTrans);

        }

        String formatOrigin = this.olatitude + "," + this.olongitude;
        System.out.println("origin : " + formatOrigin);
        this.oLatLng = formatOrigin;

        String formatDestin = this.dlatitude + "," + this.dlongtitude;
        System.out.println(
                "destination: " + formatDestin);
        this.dLatLng = formatDestin;

        this.keyid = genUUID();
        System.out.println("this request is tagged to " + this.keyid);

    }

    public String genUUID() {

        String randomID = UUID.randomUUID().toString().substring(0, 8);

        return randomID;
    }

    public JsonObject LRtoJsonObject() {
        JsonArrayBuilder transportModesBuilder = Json.createArrayBuilder();
        if (transportModes != null) {
            for (String mode : transportModes) {
                transportModesBuilder.add(mode);
            }
        }

        return Json.createObjectBuilder()
                .add("keyid", keyid)
                .add("olatitude", olatitude)
                .add("olongitude", olongitude)
                .add("dlatitude", dlatitude)
                .add("dlongtitude", dlongtitude)
                .add("pref", pref)
                .add("oLatLng", oLatLng)
                .add("dLatLng", dLatLng)
                .add("dTimeInEpoch", dTimeInEpoch)
                .add("aTimeInEpoch", aTimeInEpoch)
                .add("oName", oName)
                .add("dName", dName)
                .add("oForA", oForA)
                .add("dForA", dForA)
                .add("depTime", depTime)
                .add("arrTime", arrTime)
                .add("transportModes", transportModesBuilder)
                .add("prefTrans", prefTrans)
                .build();
    }



    public static LocationRequest fromJsonObjectToLR(JsonObject jsonObject) {
        LocationRequest locationRequest = new LocationRequest();
    
        if (jsonObject.containsKey("keyid")) {
            locationRequest.setKeyid(jsonObject.getString("keyid"));
        } else {
            locationRequest.setKeyid("N/A");
        }
    
        if (jsonObject.containsKey("olatitude")) {
            locationRequest.setOlatitude(jsonObject.getString("olatitude"));
        } else {
            locationRequest.setOlatitude("N/A");
        }
    
        if (jsonObject.containsKey("olongitude")) {
            locationRequest.setOlongitude(jsonObject.getString("olongitude"));
        } else {
            locationRequest.setOlongitude("N/A");
        }
    
        if (jsonObject.containsKey("dlatitude")) {
            locationRequest.setDlatitude(jsonObject.getString("dlatitude"));
        } else {
            locationRequest.setDlatitude("N/A");
        }
    
        if (jsonObject.containsKey("dlongtitude")) {
            locationRequest.setDlongtitude(jsonObject.getString("dlongtitude"));
        } else {
            locationRequest.setDlongtitude("N/A");
        }
    
        if (jsonObject.containsKey("pref")) {
            locationRequest.setPref(jsonObject.getString("pref"));
        } else {
            locationRequest.setPref("N/A");
        }
    
        if (jsonObject.containsKey("oLatLng")) {
            locationRequest.setoLatLng(jsonObject.getString("oLatLng"));
        } else {
            locationRequest.setoLatLng("N/A");
        }
    
        if (jsonObject.containsKey("dLatLng")) {
            locationRequest.setdLatLng(jsonObject.getString("dLatLng"));
        } else {
            locationRequest.setdLatLng("N/A");
        }
    
        if (jsonObject.containsKey("dTimeInEpoch")) {
            locationRequest.setdTimeInEpoch(jsonObject.getString("dTimeInEpoch"));
        } else {
            locationRequest.setdTimeInEpoch("11111111");
        }
    
        if (jsonObject.containsKey("aTimeInEpoch")) {
            locationRequest.setaTimeInEpoch(jsonObject.getString("aTimeInEpoch"));
        } else {
            locationRequest.setaTimeInEpoch("11111111");
        }
    
        if (jsonObject.containsKey("oName")) {
            locationRequest.setoName(jsonObject.getString("oName"));
        } else {
            locationRequest.setoName("N/A");
        }
    
        if (jsonObject.containsKey("dName")) {
            locationRequest.setdName(jsonObject.getString("dName"));
        } else {
            locationRequest.setdName("N/A");
        }
    
        if (jsonObject.containsKey("oForA")) {
            locationRequest.setoForA(jsonObject.getString("oForA"));
        } else {
            locationRequest.setoForA("N/A");
        }
    
        if (jsonObject.containsKey("dForA")) {
            locationRequest.setdForA(jsonObject.getString("dForA"));
        } else {
            locationRequest.setdForA("N/A");
        }
    
        if (jsonObject.containsKey("depTime")) {
            locationRequest.setDepTime(jsonObject.getString("depTime"));
        } else {
            locationRequest.setDepTime("N/A");
        }
    
        if (jsonObject.containsKey("arrTime")) {
            locationRequest.setArrTime(jsonObject.getString("arrTime"));
        } else {
            locationRequest.setArrTime("N/A");
        }
    
        if (jsonObject.containsKey("prefTrans")) {
            locationRequest.setPrefTrans(jsonObject.getString("prefTrans"));
        } else {
            locationRequest.setPrefTrans("N/A");
        }
    
        if (jsonObject.containsKey("transportModes")) {
            List<String> modes = new LinkedList<>();
            JsonArray temp = jsonObject.getJsonArray("transportModes");
    
            for (int i = 0; i < temp.size(); i++) {
                modes.add(temp.getString(i));
            }
            locationRequest.setTransportModes(modes);
        }
    
        return locationRequest;
    }
    



}
