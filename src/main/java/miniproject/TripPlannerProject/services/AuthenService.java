package miniproject.TripPlannerProject.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import miniproject.TripPlannerProject.repositories.UserDetailsRepo;



@Service
public class AuthenService {

    @Autowired
    private UserDetailsRepo userepo;


    public String checkAndCreate(String username, String password){

        String reply = "";
        reply = userepo.addUserAccount(username, password);

        return reply;
    }

    public String checkLogin(String username, String password){
        String reply = "";


        reply = userepo.authenUserNPassword(username, password);
        return reply;
    }
    
}
