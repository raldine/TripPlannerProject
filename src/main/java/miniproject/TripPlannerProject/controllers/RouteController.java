package miniproject.TripPlannerProject.controllers;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import miniproject.TripPlannerProject.models.BusDetails;
import miniproject.TripPlannerProject.models.LocationRequest;
import miniproject.TripPlannerProject.models.QuickView;
import miniproject.TripPlannerProject.models.ResultObj;
import miniproject.TripPlannerProject.models.TrainDetails;
import miniproject.TripPlannerProject.models.TrainService;
import miniproject.TripPlannerProject.services.DirectionsService;
import miniproject.TripPlannerProject.services.GetRepoService;
import miniproject.TripPlannerProject.services.RemoteUpdateService;
import miniproject.TripPlannerProject.services.UserService;

@Controller
@RequestMapping("/valid")
public class RouteController {

    @Value("${google.api.key}")
    private String googleKey;

    @Autowired
    private DirectionsService service;

    @Autowired
    private GetRepoService repoService;

    @Autowired
    private UserService userService;

    @Autowired
    private RemoteUpdateService remoteUpdateService;

    @GetMapping("/{username}")
    public String getIndex(
            @PathVariable(required = true) String username,
            HttpSession sess,
            Model model) {

        List<ObjectError> gloErrors;

        if (sess.getAttribute("username") != null) {
            System.out.println("HEYHEYHEY " + sess.getAttribute("username").toString());
        }

        if (sess.getAttribute("globalErrors") != null) {
            gloErrors = (List<ObjectError>) sess.getAttribute("globalErrors");
            model.addAttribute("globalErrors", gloErrors);
            sess.removeAttribute("globalErrors");
        }

        List<QuickView> dummylist = new LinkedList<>();
        model.addAttribute("hasRoutes", false);
        model.addAttribute("quickviews", dummylist);

        List<String> toPrint = userService.getUserRouteList(username);

        if (toPrint.size() != 0) {
            List<QuickView> itemsToTable = userService.getQuickViewfromRepo(username, toPrint);
            if (itemsToTable.size() != 0) {
                model.addAttribute("hasRoutes", true);
                model.addAttribute("quickviews", itemsToTable);
            }
        }
        // prep for new calculation
        sess.setAttribute("routeResult", "");
        sess.setAttribute("busInfos", "");
        sess.setAttribute("result", "");
        sess.setAttribute("request", "");
        sess.setAttribute("trainsInfo", "");
        sess.setAttribute("createdon", "");

        model.addAttribute("userLocation", new LocationRequest());
        model.addAttribute("key", googleKey); // for geocoding and autocompelte
        model.addAttribute("req", new LocationRequest());
        model.addAttribute("listOfSavedRoutes", toPrint);
        model.addAttribute("username", username);

        if (toPrint.size() != 0) {
            model.addAttribute("listOfSavedRoutes", toPrint);
        }

        return "userindex";
    }

    @PostMapping("/getDirections")
    public String getDirections(
            @Valid @ModelAttribute("req") LocationRequest newReq,
            BindingResult bindings,
            HttpSession sess,
            Model model) {

        String usernameToInsert = "";

        if (sess.getAttribute("username") != null) {
            System.out.println("HEYHEYHEY getDirections" + sess.getAttribute("username").toString());
            usernameToInsert = sess.getAttribute("username").toString();
        }

        if (newReq.getOlatitude().equals("N/A")) {
            ObjectError startLocationError = new ObjectError(
                    "globalError",
                    "Start Location cannot be blank");
            bindings.addError(startLocationError);

        }

        if (newReq.getDlongtitude().equals("N/A")) {
            ObjectError endLocationError = new ObjectError(
                    "globalError",
                    "End Location cannot be blank");
            bindings.addError(endLocationError);

        }

        if (newReq.getDepTime().isEmpty() && newReq.getArrTime().isEmpty()) {
            ObjectError timeEmptyError = new ObjectError(
                    "globalError",
                    "Please enter EITHER the departure time OR preferred arrival time.");
            bindings.addError(timeEmptyError);

        }

        if (!newReq.getDepTime().isEmpty() && !newReq.getArrTime().isEmpty()) {
            ObjectError moreThanOneTimeError = new ObjectError(
                    "globalError",
                    "Please enter EITHER the departure time OR preferred arrival time.");
            bindings.addError(moreThanOneTimeError);

        }

        if (bindings.hasGlobalErrors()) {
            model.addAttribute("globalErrors", bindings.getGlobalErrors());
            sess.setAttribute("globalErrors", bindings.getGlobalErrors());

            return "redirect:/valid/" + usernameToInsert;
        }

        newReq.prepForCall();

        model.addAttribute("oLatLng", newReq.getoLatLng());
        model.addAttribute("dLatLng", newReq.getdLatLng());
        model.addAttribute("keyid", newReq.getKeyid());

        if (newReq.getPrefTrans().equals("both")) {
            model.addAttribute("modeBothBusAndRail", true);
        } else if (newReq.getPrefTrans().equals("rail")) {
            model.addAttribute("modeRailOnly", true);
        }

        if (newReq.getPref().equals("Fewer Transfers")) {
            model.addAttribute("prefFewerTransfer", true);

        }

        if (!newReq.getdTimeInEpoch().equals("11111111")) {
            model.addAttribute("departureTime", Long.parseLong(newReq.getdTimeInEpoch()));
        }

        if (!newReq.getaTimeInEpoch().equals("11111111")) {
            model.addAttribute("arrivalTime", Long.parseLong(newReq.getaTimeInEpoch()));
        }

        model.addAttribute("username", usernameToInsert);
        model.addAttribute("key", googleKey);
        System.out.println("requested the following: " + newReq.toString());
        sess.setAttribute("currRequest", newReq);

        // SAVE REQUEST INTO REPO

        return "calculating";

    }

    @PostMapping("/sendJsonResult")
    public String getResult(
            @RequestBody String payload,
            @RequestHeader("keyID") String keyID,
            HttpSession sess) throws IOException {

        LocationRequest current = (LocationRequest) sess.getAttribute("currRequest");
        // TO PASS USERNAME
        String username = sess.getAttribute("username").toString();

        if (payload != null) {
            // System.out.println(payload);
            System.out.println("FROM CONTROLLER the key id is " + keyID);

            ResultObj toPassService = new ResultObj();
            toPassService.setKeyid(keyID);
            toPassService.setResultObj(payload);
            // get username

            service.processResultObj(username, toPassService, current);

        }

        return "redirect:/valid/" + username + "/route/" + keyID;

    }

    @GetMapping("/{username}/route/{id}")
    public String getRetrieve(
            @PathVariable(required = true) String id,
            @PathVariable(required = true) String username,
            HttpSession sess,
            Model model) {

        model.addAttribute("key", googleKey);
        model.addAttribute("hasBus", false);
        model.addAttribute("hasTrain", false);
        model.addAttribute("hasDisrupt", false);
        model.addAttribute("routeid", id); // to generate update link
        model.addAttribute("username", username); // to generate update link

        service.retrieveTrainServiceStatus();

        Map<String, Object> allObjects = repoService.retrieveAll(username, id);
        if (!allObjects.isEmpty()) {

            Set<String> keys = allObjects.keySet();
            for (String key : keys) {

                if (key.equals("request")) {
                    LocationRequest request = (LocationRequest) allObjects.get(key);
                    model.addAttribute("request", request);
                }

                if (key.equals("result")) {
                    ResultObj result = (ResultObj) allObjects.get(key);

                    model.addAttribute("result", result);
                    model.addAttribute("routeResult", result.getResultObj());
                }

                if (key.equals("busdeet")) {
                    List<BusDetails> busesInfo = (List<BusDetails>) allObjects.get(key);
                    if (busesInfo.size() != 0) {
                        String dataFetchOn = busesInfo.get(0).getDataFetchedOn();
                        model.addAttribute("hasBus", true);
                        model.addAttribute("busInfos", busesInfo);
                        model.addAttribute("busDateFetch", dataFetchOn);
                        sess.setAttribute("busInfos", busesInfo);
                    }

                }

                if (key.equals("traindeet")) {
                    List<TrainDetails> trainsInfo = (List<TrainDetails>) allObjects.get(key);
                    if (trainsInfo.size() != 0) {
                        String trainDataFetchOn = trainsInfo.get(0).getDataFetchedOn();
                        System.out.println("retreive side: " + trainsInfo.toString());
                        model.addAttribute("hasTrain", true);

                        model.addAttribute("trainDataFetchOn", trainDataFetchOn);

                        TrainService result = service.retrieveTrainServiceStatus();
                        System.out.println("from controller side >>> " + result.toString());
                        trainsInfo = service.modTrainDetailsServiceStatus(result, trainsInfo);
                        model.addAttribute("trainsInfo", trainsInfo);
                        sess.setAttribute("trainsInfo", trainsInfo);

                        if (result.getStatusInString().equals("Disrupted")
                                || result.getStatusInString().equals("Recovered")) {
                            model.addAttribute("hasDisrupt", true);
                            model.addAttribute("MessageFromService", result.getMessages());
                        }

                    }

                }

                if (key.equals("createdon")) {
                    model.addAttribute("createdon", allObjects.get(key).toString());

                }
            }

        }

        return "retrieve";
    }

    @GetMapping("/{username}/route/{id}/updateBus")
    public String triggerUpdateBus(
            @PathVariable(required = true) String id,
            @PathVariable(required = true) String username,
            HttpSession sess) {
        if (sess.getAttribute("busInfos") != null) {
            List<BusDetails> oldToMod = (List<BusDetails>) sess.getAttribute("busInfos");
            System.out.println("TRIGGGERREDDDD");
            remoteUpdateService.updateBusDetailInRepo(username, id, oldToMod);

        }

        return "redirect:/valid/" + username + "/route/" + id + "#bus-container";

    }

    @GetMapping("/{username}/route/{id}/updateTrain")
    public String triggerUpdateTrain(
            @PathVariable(required = true) String id,
            @PathVariable(required = true) String username,
            HttpSession sess) {
        if (sess.getAttribute("trainsInfo") != null) {
            List<TrainDetails> oldToMod = (List<TrainDetails>) sess.getAttribute("trainsInfo");

            remoteUpdateService.updateTrainDetailInRepo(username, id, oldToMod);

        }

        return "redirect:/valid/" + username + "/route/" + id + "#train-container";

    }

    @GetMapping("/delete")
    public String deleteItem(
            @RequestParam String id,
            @RequestParam String username) {

        System.out.println("Deleting item with ID: " + id + " for user: " + username);

        userService.removeIDfromUserList(username, id);
        userService.removeAllItemsfromUser(username, id);

        return "redirect:/valid/" + username;
    }

}
