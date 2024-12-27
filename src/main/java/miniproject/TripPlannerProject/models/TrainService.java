package miniproject.TripPlannerProject.models;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class TrainService {

    private int status = 0;
    private String statusInString = "No status";
    private List<String> affectedLines = new LinkedList<>();
    private Map<String, List<String>> affectedLinesAndDirections = new HashMap();
    private List<String> messages = new LinkedList<>();

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<String> getAffectedLines() {
        return affectedLines;
    }

    public void setAffectedLines(List<String> affectedLines) {
        this.affectedLines = affectedLines;
    }

 
    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }



    public String getStatusInString() {
        return statusInString;
    }

    public void setStatusInString(String statusInString) {
        this.statusInString = statusInString;
    }


    public Map<String, List<String>> getAffectedLinesAndDirections() {
        return affectedLinesAndDirections;
    }

    public void setAffectedLinesAndDirections(Map<String, List<String>> affectedLinesAndDirections) {
        this.affectedLinesAndDirections = affectedLinesAndDirections;
    }

    @Override
    public String toString() {
        return "TrainService [status=" + status + ", statusInString=" + statusInString + ", affectedLines="
                + affectedLines + ", affectedLinesAndDirections=" + affectedLinesAndDirections + ", messages="
                + messages + "]";
    }



}
