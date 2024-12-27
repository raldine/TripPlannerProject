package miniproject.TripPlannerProject.models;


import org.springframework.stereotype.Component;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Component
public class UsernamePassword {


    @NotEmpty(message = "Name cannot be empty")
    @Size(min = 5, max = 50, message = "Username must be between 5 to 32 chars")
    private String username;


    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$",
    message = "Password must be at least 6 characters long and include both letters and numbers only.")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UsernamePassword [username=" + username + ", password=" + password + "]";
    }

    

}
