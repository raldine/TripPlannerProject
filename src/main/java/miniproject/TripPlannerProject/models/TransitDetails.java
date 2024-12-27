package miniproject.TripPlannerProject.models;


import org.springframework.stereotype.Component;

@Component
public class TransitDetails {

    private String vehicleType;
    private String transportProvider;
    private String transportName;
    private String departureStopName;
    private String arrivalStopName;
    private String depatureTime;
    private String departLat;
    private String departLng;
    private String numStops;
    private String trainTowards = "N/A";

    // private String arrivalStopName;

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getTransportProvider() {
        return transportProvider;
    }

    public void setTransportProvider(String transportProvider) {
        this.transportProvider = transportProvider;
    }

    public String getTransportName() {
        return transportName;
    }

    public void setTransportName(String transportName) {
        this.transportName = transportName;
    }

    public String getDepartureStopName() {
        return departureStopName;
    }

    public void setDepartureStopName(String departureStopName) {
        this.departureStopName = departureStopName;
    }



    public String getDepatureTime() {
        return depatureTime;
    }

    public void setDepatureTime(String depatureTime) {
        this.depatureTime = depatureTime;
    }

    public String getDepartLat() {
        return departLat;
    }

    public void setDepartLat(String departLat) {
        this.departLat = departLat;
    }

    public String getDepartLng() {
        return departLng;
    }

    public void setDepartLng(String departLng) {
        this.departLng = departLng;
    }

    public String getArrivalStopName() {
        return arrivalStopName;
    }

    public void setArrivalStopName(String arrivalStopName) {
        this.arrivalStopName = arrivalStopName;
    }

    public String getNumStops() {
        return numStops;
    }

    public void setNumStops(String numStops) {
        this.numStops = numStops;
    }

    //only train steps
    public String getTrainTowards() {
        return trainTowards;
    }

    public void setTrainTowards(String trainTowards) {
        this.trainTowards = trainTowards;
    }

    @Override
    public String toString() {
        return "TransitDetails [vehicleType=" + vehicleType + ", transportProvider=" + transportProvider
                + ", transportName=" + transportName + ", departureStopName=" + departureStopName + ", arrivalStopName="
                + arrivalStopName + ", depatureTime=" + depatureTime + ", departLat=" + departLat + ", departLng="
                + departLng + ", numStops=" + numStops + ", trainTowards=" + trainTowards + "]";
    }

 

    



    


    
}
