package miniproject.TripPlannerProject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import miniproject.TripPlannerProject.models.UsernamePassword;
import miniproject.TripPlannerProject.services.UserService;

@Controller
@RequestMapping
public class IndexController {

    @Autowired
    private UserService userService;

    @GetMapping("/newAccount")
    public String getCreateForm(
            Model model,
            HttpSession sess) {
        if (sess.getAttribute("username") != null) {
            if (!sess.getAttribute("username").equals("")) {
                // show warning logout?
                sess.removeAttribute("username");
            }
        }

        UsernamePassword newThing = new UsernamePassword();

        model.addAttribute("userDeet", newThing);

        return "create";
    }

    @GetMapping(path = { "/", "/login" })
    public String getLogin(
            Model model,
            HttpSession sess) {
        if (sess.getAttribute("username") != null) {
            if (!sess.getAttribute("username").equals("")) {
                // show warning logout?
                sess.removeAttribute("username");
            }
        }

        UsernamePassword newThing = new UsernamePassword();

        model.addAttribute("userDeet", newThing);
        model.addAttribute("unAuthorised", false);// show hide msg
        model.addAttribute("unAuthorMessage", "");

        return "index";
    }

    @PostMapping("/newAccount/submit")
    public String createAccount(
            @Valid @ModelAttribute("userDeet") UsernamePassword newDetails,
            BindingResult bindings,
            Model model,
            HttpSession sess) {

        String username = newDetails.getUsername().toLowerCase();
        String reply = userService.callToCreateAcc(newDetails);// "OK" or "User already exists, use another username"
        System.out.println("controller: " + reply);

        if (bindings.hasErrors()) {
            model.addAttribute("userDeet", newDetails); // passback keyed details

            return "create";

        }

        if (reply.equals("User already exists, use another username.") || reply.equals("Error")) {

            FieldError err = new FieldError("userDeet", "username", "User already exists, use another username.");
            bindings.addError(err);

            return "create";
        } else {

            // else login success
            sess.setAttribute("username", username);
        }

        return "redirect:/valid/" + username;
    }

    @PostMapping("/login/submit")
    public String initLogin(
            @Valid @ModelAttribute("userDeet") UsernamePassword newDetails,
            BindingResult bindings,
            Model model,
            HttpSession sess) {

        String username = newDetails.getUsername().toLowerCase();
        String reply = userService.callToCheckLogin(newDetails);// "OK" or "Either Username does not exist or Password
                                                                // is wrong"
        System.out.println("INDEXCONTROLLER SIDE REPLY FOR LOGIN SUBMIT" + reply);

        if (newDetails.getUsername().isBlank()) {

            FieldError err1 = new FieldError("userDeet", "username", "Username cannot be left blank");

            bindings.addError(err1);

            return "index";

        }
        if (bindings.hasErrors()) {
        model.addAttribute("userDeet", newDetails);
        System.out.println("return because invalidation");
        return "index";
        }

        if (reply.equals("Either Username does not exist or Password is wrong.")) {

            model.addAttribute("userDeet", new UsernamePassword()); // passback keyed details
            model.addAttribute("unAuthorised", true);// show message
            model.addAttribute("unAuthorMessage",
                    "Either Username does not exist or Password is wrong. Please try again.");

            return "index";
        }

        // else login success
        sess.setAttribute("username", username);

        return "redirect:/valid/" + username;
    }

    @GetMapping("/logoff")
    public String getCreateForm(
            HttpSession sess,
            Model model) {
        if (sess.getAttribute("username") != null) {
            if (!sess.getAttribute("username").equals("")) {

                sess.removeAttribute("username");

            }
        }

        sess.invalidate();

        return "redirect:/";
    }

    @GetMapping("/unauthorised")
    public String getUnAuthorised(
        HttpSession sess
    ){
        sess.invalidate();

        return "unauthorised";

    }

}
