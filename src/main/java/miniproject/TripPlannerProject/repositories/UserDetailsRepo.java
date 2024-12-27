package miniproject.TripPlannerProject.repositories;


import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@Repository
public class UserDetailsRepo {

    private final String masterList = "masterList444#4@3";

    @Autowired
    @Qualifier("redis-0-object")
    private RedisTemplate<String, Object> template;

    public String addUserAccount(String username, String password) {

        String userList = masterList;
        String loweruser = username.toLowerCase();
        String reply = "";

        HashOperations<String, String, Object> hashOps = template.opsForHash();
        System.out.println("addy user activated on repo side");

        if (!template.hasKey(userList)) {
    

             hashOps.put(userList, loweruser, password);
             reply = "Putted " + loweruser + "into repo";

        } else
        
        if (template.hasKey(userList)) {

            Set<String> keys = hashOps.keys(masterList);

            for (String key : keys) {
                if (loweruser.equals(key)) {
                    reply = "User already exists, use another username";
                } 
            }

             // pass check
             hashOps.put(userList, loweruser, password);
             reply = "Putted " + loweruser + "into repo";

        }

        return reply;
    }

    public String authenUserNPassword(String username, String password) {

        String userList = masterList;
        String loweruser = username.toLowerCase();
        String reply = "Either Username does not exist or Password is wrong";
        boolean userExists = false;
        boolean pwCorrect = false;

        System.out.println("authen user activated on repo side");
        HashOperations<String, String, Object> hashOps = template.opsForHash();
        
        if (template.hasKey(userList)) {

            Set<String> keys = hashOps.keys(userList);

            for (String key : keys) {
                if (loweruser.equals(key)){
                    userExists = true;
                    System.out.println("user exists");
                } 
            }

        }

        if(userExists==true){
            String correctPw = (String)hashOps.get(userList, loweruser);
            if(password.equals(correctPw)){
                pwCorrect = true;
                System.out.println("password correct");
            }
        }

        if(userExists==true && pwCorrect==true){
            reply = "OK";
        }

        return reply;
    }

    public void updateUserRouteList(String username, String keyid){
        //insert in this order => latest made is first, oldest is last

        String userrouteID = username+"@routelist";
        ListOperations<String, Object> listOps = template.opsForList();
        listOps.leftPush(userrouteID, keyid);
        System.out.println("userdeet repo: " +  username + " added " + keyid);



    }

    public List<Object> getUserRouteList(String username){

        String userrouteID = username+"@routelist";
        ListOperations<String, Object> listOps = template.opsForList();
    

        List<Object> result = listOps.range(userrouteID, 0, -1);

        return result;


    }















}
