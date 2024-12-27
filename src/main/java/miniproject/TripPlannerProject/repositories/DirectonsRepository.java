package miniproject.TripPlannerProject.repositories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import jakarta.json.JsonArray;
import jakarta.json.JsonObject;

@Repository
public class DirectonsRepository {

    @Autowired
    @Qualifier("redis-0-object")
    private RedisTemplate<String, Object> template;

    public void insertAllBusOnly(String username, String keyid, JsonObject req, JsonObject result,
            JsonArray busDet, String createdTime) {

        String identifier = username;

        String reqIdentifier = "request@" + keyid;
        String resultIdentifier = "result@" + keyid;
        String busDIdentifier = "busdetail@" + keyid;
        String createdOnIdentifier = "createdon@" + keyid;

        Map<String, Object> allDetails = new HashMap<>();

        allDetails.put(reqIdentifier, req.toString());
        allDetails.put(resultIdentifier, result.toString());
        allDetails.put(busDIdentifier, busDet.toString());
        allDetails.put(createdOnIdentifier, createdTime);

        HashOperations<String, String, Object> hashOps = template.opsForHash();

        hashOps.putAll(identifier, allDetails);
        System.out.println("successfully put in repo under " + username + " | " + keyid);

    }

    public void insertAllTrainOnly(String username, String keyid, JsonObject req, JsonObject result,
            JsonArray trainArray, String createdTime) {

        String identifier = username;

        String reqIdentifier = "request@" + keyid;
        String resultIdentifier = "result@" + keyid;
        String trainDIdentifier = "traindetail@" + keyid;
        String createdOnIdentifier = "createdon@" + keyid;

        Map<String, Object> allDetails = new HashMap<>();

        allDetails.put(reqIdentifier, req.toString());
        allDetails.put(resultIdentifier, result.toString());
        
        allDetails.put(trainDIdentifier, trainArray.toString());
        allDetails.put(createdOnIdentifier, createdTime);

        HashOperations<String, String, Object> hashOps = template.opsForHash();

        hashOps.putAll(identifier, allDetails);
        System.out.println("successfully put in repo under " + username + " | " + keyid);

    }

    public void insertAllBusTrain(String username, String keyid, JsonObject req, JsonObject result, JsonArray busArray,
            JsonArray trainArray, String createdTime) {

        String identifier = username;

        String reqIdentifier = "request@" + keyid;
        String resultIdentifier = "result@" + keyid;
        String busDIdentifier = "busdetail@" + keyid;
        String trainDIdentifier = "traindetail@" + keyid;
        String createdOnIdentifier = "createdon@" + keyid;

        Map<String, Object> allDetails = new HashMap<>();

        allDetails.put(reqIdentifier, req.toString());
        allDetails.put(resultIdentifier, result.toString());
        allDetails.put(busDIdentifier, busArray.toString());
        allDetails.put(trainDIdentifier, trainArray.toString());
        allDetails.put(createdOnIdentifier, createdTime);

        HashOperations<String, String, Object> hashOps = template.opsForHash();

        hashOps.putAll(identifier, allDetails);
        System.out.println("successfully put in repo under " + username + " | " + keyid);

    }

    public Map<String, Object> getAllDetails(String username, String keyid) {

        HashOperations<String, String, Object> hashOps = template.opsForHash();

        Map<String, Object> usersAll = new HashMap<>();

        if (template.hasKey(username)) {
            usersAll = hashOps.entries(username);
        }

        // only related to keyid
        String keyIdentifier = "@" + keyid;
        Map<String, Object> finalMap = new HashMap<>();
        Set<String> keys = usersAll.keySet();

        for (String key : keys) {
            if (key.endsWith(keyIdentifier)) {
                Object temp = usersAll.get(key);
                finalMap.put(key, temp);
            }

        }

        return finalMap;

    }

    public void updateExistingBusInfo(String username, String keyid, JsonArray updatedBusArray){

        HashOperations<String, String, Object> hashOps = template.opsForHash();

        String identifier = username;
        String busDIdentifier = "busdetail@" + keyid;

        hashOps.put(identifier, busDIdentifier, updatedBusArray.toString());
        System.out.println("from repo, updated " + username + " busdetails" + "with keyid " + keyid);
    }

    public void updateTrainInfo(String username, String keyid, JsonArray updatedTrainArray){

        HashOperations<String, String, Object> hashOps = template.opsForHash();

        String identifier = username;
        String trainDIdentifier = "traindetail@" + keyid;

        hashOps.put(identifier, trainDIdentifier, updatedTrainArray.toString());
        System.out.println("from repo, updated " + username + " traindetails");
    }

}

