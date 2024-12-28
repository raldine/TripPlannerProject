package miniproject.TripPlannerProject.repositories;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import jakarta.json.JsonObject;

@Repository
public class UserDetailsRepo {

    private final String masterList = "masterList444#4@3";

    @Autowired
    @Qualifier("redis-0-object")
    private RedisTemplate<String, Object> template;

    public String addUserAccount(String username, String password) {
        String lowerUser = username.toLowerCase();
        String reply = "";

        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            return "Error: Username or password cannot be blank.";
        }

        HashOperations<String, String, Object> hashOps = template.opsForHash();

        String userList = masterList;

        if (!template.hasKey(userList)) {
            // If the list does not exist, create it and add the user
            hashOps.put(userList, lowerUser, password);
            System.out.println("User " + lowerUser + " added to the repository.");
            return "OK";
        } else

        // Check if the user already exists
        if (template.hasKey(userList)) {
            Set<String> allUsernames = hashOps.keys(userList);
            System.out.println("Repo side: all users list" + allUsernames.toString());

            System.out.println("compared to: " + lowerUser);

            boolean exists = allUsernames.contains(lowerUser);
            if (exists) {
                System.out.println("username list contains " + lowerUser);
                return "User already exists, use another username";
            } else {
                // If the user does not exist, add the new user to the repository
                hashOps.put(userList, lowerUser, password);
                System.out.println("User " + lowerUser + " added to the repository.");
                System.out.println("username list DOES NOT contains " + lowerUser);
                return "OK";
            }
        }

        return reply="failure on repo side";

    }

    public String authenUserNPassword(String username, String password) {

        String userList = masterList;
        String loweruser = username.toLowerCase();
        String reply = "Either Username does not exist or Password is wrong.";
        boolean userExists = false;
        boolean pwCorrect = false;

        System.out.println("authen user activated on repo side");
        HashOperations<String, String, Object> hashOps = template.opsForHash();

        if (template.hasKey(userList)) {

            Set<String> keys = hashOps.keys(userList);

            for (String key : keys) {
                if (loweruser.equals(key)) {
                    userExists = true;
                    System.out.println("user exists");
                }
            }

        }

        if (userExists == true) {
            String correctPw = hashOps.get(userList, loweruser).toString();
            if (password.equals(correctPw)) {
                pwCorrect = true;
                System.out.println("password correct");
            }
        }

        if (userExists == true && pwCorrect == true) {
            reply = "OK";
        }

        return reply;
    }

    public void updateUserRouteList(String username, String keyid) {
        // insert in this order => latest made is first, oldest is last

        String userrouteID = username + "@routelist";
        ListOperations<String, Object> listOps = template.opsForList();
        listOps.leftPush(userrouteID, keyid);
        System.out.println("userdeet repo: " + username + " added " + keyid);

    }

    public void removeIDfromUserRouteList(String username, String keyid){

        ListOperations<String, Object> listOps = template.opsForList();

        String userroutlistID = username+"@routelist";

        listOps.remove(userroutlistID, 0, keyid);
        System.out.println("user repo removed " + keyid);

    }

    public List<Object> getUserRouteList(String username) {

        String userrouteID = username + "@routelist";
        ListOperations<String, Object> listOps = template.opsForList();

        List<Object> result = listOps.range(userrouteID, 0, -1);

        return result;

    }


    public void putQuickView(String username, JsonObject qVInObject) {

        HashOperations<String, String, Object> hashOps = template.opsForHash();

        String identifier = username;
        String keyid = qVInObject.getString("keyid");
        String qvIdentifier = "quickv@" + keyid;

        hashOps.put(identifier, qvIdentifier, qVInObject.toString());
        System.out.println("success put quickview for user " + username + " " + keyid);

    }

    public List<Object> getQuickViewObjects(String username, List<String> idsOfUser) {

        HashOperations<String, String, Object> hashOps = template.opsForHash();

        List<Object> qVJsonObjects = new LinkedList<>(); // preserve insertion order

        if (idsOfUser.size() != 0) {
            // go in order of 0 - last of ids
            for (String id : idsOfUser) {

                String qvIdentifier = "quickv@" + id;
                Object qvRaw = hashOps.get(username, qvIdentifier);
                qVJsonObjects.add(qvRaw);

            }
        }

        return qVJsonObjects;

    }

    //REMOVE ALL

    public void removeAllWithUserKeyID(String username, String keyid){

        HashOperations<String, String, Object> hashOps = template.opsForHash();
        Set<String> keys = hashOps.keys(username); //all objects, multiple routes


        //remove result@
        String resultID = "result@"+keyid;
        if(keys.contains(resultID)){
            hashOps.delete(username, resultID);
            System.out.println("deleted " + username + " " + resultID);
        }


        //remove request@
        String requestId = "request@"+keyid;
        if(keys.contains(requestId)){
            hashOps.delete(username, requestId);
            System.out.println("deleted " + username + " " + requestId);
        }
        //remove createdon@
        String createID = "createdon@"+keyid;
        if(keys.contains(createID)){
            hashOps.delete(username, createID);
            System.out.println("deleted " + username + " " + createID);
        }

        //remove busdetail@
        String busdetailID = "busdetail@"+keyid;
        if(keys.contains(busdetailID)){
            hashOps.delete(username, busdetailID);
            System.out.println("deleted " + username + " " + busdetailID);
        }
        //remove traindetail@
        String trainID = "traindetail@"+keyid;
        if(keys.contains(trainID)){
            hashOps.delete(username, trainID);
            System.out.println("deleted " + username + " " + trainID);
        }

        //remove quickv@
        String qvID = "quickv@"+keyid;
        if(keys.contains(qvID)){
            hashOps.delete(username, qvID);
            System.out.println("deleted " + username + " " + qvID);
        }

    }

}
