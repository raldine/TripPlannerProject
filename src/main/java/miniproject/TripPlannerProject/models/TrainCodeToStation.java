package miniproject.TripPlannerProject.models;


import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class TrainCodeToStation {

    // private Map<String, String> mapCodeToStation = new HashMap<>();
    private Map<String, String> NSline = new HashMap<>();
    private Map<String, String> EWline = new HashMap<>();
    private Map<String, String> CGline = new HashMap<>(); // airport line
    private Map<String, String> NEline = new HashMap<>();
    private Map<String, String> CCline = new HashMap<>();
    private Map<String, String> DTline = new HashMap<>();
    private Map<String, String> BPline = new HashMap<>(); // bukit panjng lrt
    private Map<String, String> SLRTline = new HashMap<>(); // sengkang lrt
    private Map<String, String> PLRTline = new HashMap<>(); // punggol lrt
    private Map<String, String> TELine = new HashMap<>(); // thomson east coast line (brown)

    public TrainCodeToStation() {

        // North South Line
        this.NSline.put("Jurong East", "NS1");
        this.NSline.put("Bukit Batok", "NS2");
        this.NSline.put("Bukit Gombak", "NS3");
        this.NSline.put("Choa Chu Kang", "NS4");
        this.NSline.put("Yew Tee", "NS5");
        this.NSline.put("Kranji", "NS7");
        this.NSline.put("Marsiling", "NS8");
        this.NSline.put("Woodlands", "NS9");
        this.NSline.put("Admiralty", "NS10");
        this.NSline.put("Sembawang", "NS11");
        this.NSline.put("Canberra", "NS12");
        this.NSline.put("Yishun", "NS13");
        this.NSline.put("Khatib", "NS14");
        this.NSline.put("Yio Chu Kang", "NS15");
        this.NSline.put("Ang Mo Kio", "NS16");
        this.NSline.put("Bishan", "NS17");
        this.NSline.put("Braddell", "NS18");
        this.NSline.put("Toa Payoh", "NS19");
        this.NSline.put("Novena", "NS20");
        this.NSline.put("Newton", "NS21");
        this.NSline.put("Orchard", "NS22");
        this.NSline.put("Somerset", "NS23");
        this.NSline.put("Dhoby Ghaut", "NS24");
        this.NSline.put("City Hall", "NS25");
        this.NSline.put("Raffles Place", "NS26");
        this.NSline.put("Marina Bay", "NS27");
        this.NSline.put("Marina South Pier", "NS28"); // +1

        // East West Line
        this.EWline.put("Pasir Ris", "EW1");
        this.EWline.put("Tampines", "EW2");
        this.EWline.put("Simei", "EW3");
        this.EWline.put("Tanah Merah", "EW4");
        this.EWline.put("Bedok", "EW5");
        this.EWline.put("Kembangan", "EW6");
        this.EWline.put("Eunos", "EW7");
        this.EWline.put("Paya Lebar", "EW8");
        this.EWline.put("Aljunied", "EW9");
        this.EWline.put("Kallang", "EW10");
        this.EWline.put("Lavender", "EW11");
        this.EWline.put("Bugis", "EW12");
        this.EWline.put("City Hall", "EW13");
        this.EWline.put("Raffles Place", "EW14");
        this.EWline.put("Tanjong Pagar", "EW15");
        this.EWline.put("Outram Park", "EW16");
        this.EWline.put("Tiong Bahru", "EW17");
        this.EWline.put("Redhill", "EW18");
        this.EWline.put("Queenstown", "EW19");
        this.EWline.put("Commonwealth", "EW20");
        this.EWline.put("Buona Vista", "EW21");
        this.EWline.put("Dover", "EW22");
        this.EWline.put("Clementi", "EW23");
        this.EWline.put("Jurong East", "EW24");
        this.EWline.put("Chinese Garden", "EW25");
        this.EWline.put("Lakeside", "EW26");
        this.EWline.put("Boon Lay", "EW27");
        this.EWline.put("Pioneer", "EW28");
        this.EWline.put("Joo Koon", "EW29");
        this.EWline.put("Gul Circle", "EW30");
        this.EWline.put("Tuas Crescent", "EW31");
        this.EWline.put("Tuas West Road", "EW32");
        this.EWline.put("Tuas Link", "EW33");

        // changi airport line
        this.CGline.put("Expo", "CG1");
        this.CGline.put("Changi Airport", "CG2");
        this.CGline.put("Tanah Merah", "EW4");

        // North east line
        this.NEline.put("HarbourFront", "NE1");
        this.NEline.put("Outram Park", "NE3");
        this.NEline.put("Chinatown", "NE4");
        this.NEline.put("Clarke Quay", "NE5");
        this.NEline.put("Dhoby Ghaut", "NE6");
        this.NEline.put("Little India", "NE7");
        this.NEline.put("Farrer Park", "NE8");
        this.NEline.put("Boon Keng", "NE9");
        this.NEline.put("Potong Pasir", "NE10");
        this.NEline.put("Woodleigh", "NE11");
        this.NEline.put("Serangoon", "NE12");
        this.NEline.put("Kovan", "NE13");
        this.NEline.put("Hougang", "NE14");
        this.NEline.put("Buangkok", "NE15");
        this.NEline.put("Sengkang", "NE16");
        this.NEline.put("Punggol", "NE17");
        this.NEline.put("Punggol Coast", "NE18");

        // CC Line
        this.CCline.put("Dhoby Ghaut", "CC1");
        this.CCline.put("Bras Basah", "CC2");
        this.CCline.put("Esplanade", "CC3");
        this.CCline.put("Promenade", "CC4");
        this.CCline.put("Nicoll Highway", "CC5");
        this.CCline.put("Stadium", "CC6");
        this.CCline.put("Mountbatten", "CC7");
        this.CCline.put("Dakota", "CC8");
        this.CCline.put("Paya Lebar", "CC9");
        this.CCline.put("MacPherson", "CC10");
        this.CCline.put("Tai Seng", "CC11");
        this.CCline.put("Bartley", "CC12");
        this.CCline.put("Serangoon", "CC13");
        this.CCline.put("Lorong Chuan", "CC14");
        this.CCline.put("Bishan", "CC15");
        this.CCline.put("Marymount", "CC16");
        this.CCline.put("Caldecott", "CC17");
        this.CCline.put("Botanic Gardens", "CC19");
        this.CCline.put("Farrer Road", "CC20");
        this.CCline.put("Holland Village", "CC21");
        this.CCline.put("Buona Vista", "CC22");
        this.CCline.put("One-North", "CC23");
        this.CCline.put("Kent Ridge", "CC24");
        this.CCline.put("Haw Par Villa", "CC25");
        this.CCline.put("Pasir Panjang", "CC26");
        this.CCline.put("Labrador Park", "CC27");
        this.CCline.put("Telok Blangah", "CC28");
        this.CCline.put("HarbourFront", "CC29");
        this.CCline.put("Bayfront", "CE1");
        this.CCline.put("Marina Bay", "CE2");

        // DTL
        this.DTline.put("Bukit Panjang", "DT1");
        this.DTline.put("Cashew", "DT2");
        this.DTline.put("Hillview", "DT3");
        this.DTline.put("Beauty World", "DT5");
        this.DTline.put("King Albert Park", "DT6");
        this.DTline.put("Sixth Avenue", "DT7");
        this.DTline.put("Tan Kah Kee", "DT8");
        this.DTline.put("Botanic Gardens", "DT9");
        this.DTline.put("Stevens", "DT10");
        this.DTline.put("Newton", "DT11");
        this.DTline.put("Little India", "DT12");
        this.DTline.put("Rochor", "DT13");
        this.DTline.put("Bugis", "DT14");
        this.DTline.put("Promenade", "DT15");
        this.DTline.put("Bayfront", "DT16");
        this.DTline.put("Downtown", "DT17");
        this.DTline.put("Telok Ayer", "DT18");
        this.DTline.put("Chinatown", "DT19");
        this.DTline.put("Fort Canning", "DT20");
        this.DTline.put("Bencoolen", "DT21");
        this.DTline.put("Jalan Besar", "DT22");
        this.DTline.put("Bendemeer", "DT23");
        this.DTline.put("Geylang Bahru", "DT24");
        this.DTline.put("Mattar", "DT25");
        this.DTline.put("MacPherson", "DT26");
        this.DTline.put("Ubi", "DT27");
        this.DTline.put("Kaki Bukit", "DT28");
        this.DTline.put("Bedok North", "DT29");
        this.DTline.put("Bedok Reservoir", "DT30");
        this.DTline.put("Tampines West", "DT31");
        this.DTline.put("Tampines", "DT32");
        this.DTline.put("Tampines East", "DT33");
        this.DTline.put("Upper Changi", "DT34");
        this.DTline.put("Expo", "DT35");

        // BP lrt
        this.BPline.put("Choa Chu Kang", "BP1");
        this.BPline.put("South View", "BP2");
        this.BPline.put("Keat Hong", "BP3");
        this.BPline.put("Teck Whye", "BP4");
        this.BPline.put("Phoenix", "BP5");
        this.BPline.put("Bukit Panjang", "BP6");
        this.BPline.put("Petir", "BP7");
        this.BPline.put("Pending", "BP8");
        this.BPline.put("Bangkit", "BP9");
        this.BPline.put("Fajar", "BP10");
        this.BPline.put("Segar", "BP11");
        this.BPline.put("Jelapang", "BP12");
        this.BPline.put("Senja", "BP13");

        // sengkang lrt
        this.SLRTline.put("Sengkang", "STC");
        this.SLRTline.put("Compassvale", "SE1");
        this.SLRTline.put("Rumbia", "SE2");
        this.SLRTline.put("Bakau", "SE3");
        this.SLRTline.put("Kangkar", "SE4");
        this.SLRTline.put("Ranggung", "SE5");
        this.SLRTline.put("Cheng Lim", "SW1");
        this.SLRTline.put("Farmway", "SW2");
        this.SLRTline.put("Kupang", "SW3");
        this.SLRTline.put("Thanggam", "SW4");
        this.SLRTline.put("Fernvale", "SW5");
        this.SLRTline.put("Layar", "SW6");
        this.SLRTline.put("Tongkang", "SW7");
        this.SLRTline.put("Renjong", "SW8");

        // punggol lrt
        this.PLRTline.put("Punggol", "PTC");
        this.PLRTline.put("Cove", "PE1");
        this.PLRTline.put("Meridian", "PE2");
        this.PLRTline.put("Coral Edge", "PE3");
        this.PLRTline.put("Riviera", "PE4");
        this.PLRTline.put("Kadaloor", "PE5");
        this.PLRTline.put("Oasis", "PE6");
        this.PLRTline.put("Damai", "PE7");
        this.PLRTline.put("Sam Kee", "PW1");
        this.PLRTline.put("Teck Lee", "PW2");
        this.PLRTline.put("Punggol Point", "PW3");
        this.PLRTline.put("Samudera", "PW4");
        this.PLRTline.put("Nibong", "PW5");
        this.PLRTline.put("Sumang", "PW6");
        this.PLRTline.put("Soo Teck", "PW7");

        // TEL
        this.TELine.put("Woodlands North", "TE1");
        this.TELine.put("Woodlands", "TE2");
        this.TELine.put("Woodlands South", "TE3");
        this.TELine.put("Springleaf", "TE4");
        this.TELine.put("Lentor", "TE5");
        this.TELine.put("Mayflower", "TE6");
        this.TELine.put("Bright Hill", "TE7");
        this.TELine.put("Upper Thomson", "TE8");
        this.TELine.put("Caldecott", "TE9");
        this.TELine.put("Stevens", "TE11");
        this.TELine.put("Napier", "TE12");
        this.TELine.put("Orchard Boulevard", "TE13");
        this.TELine.put("Orchard", "TE14");
        this.TELine.put("Great World", "TE15");
        this.TELine.put("Havelock", "TE16");
        this.TELine.put("Outram Park", "TE17");
        this.TELine.put("Maxwell", "TE18");
        this.TELine.put("Shenton Way", "TE19");
        this.TELine.put("Marina Bay", "TE20");
        this.TELine.put("Gardens by the Bay", "TE22");
        this.TELine.put("Tanjong Rhu", "TE23");
        this.TELine.put("Katong Park", "TE24");
        this.TELine.put("Tanjong Katong", "TE25");
        this.TELine.put("Marine Parade", "TE26");
        this.TELine.put("Marine Terrace", "TE27");
        this.TELine.put("Siglap", "TE28");
        this.TELine.put("Bayshore", "TE29");

    }

    public String returnCode(String stationName, String lineName) {

        String result = "";

        if (lineName.equals("North South Line")) {

            result = this.NSline.get(stationName);

        } else if (lineName.equals("East West Line")) {

            result = this.EWline.get(stationName);

        } else if (lineName.equals("North East Line")) {
            
            result = this.NEline.get(stationName);

        } else if (lineName.equals("Changi Airport Line")) {

            result = this.CGline.get(stationName);

        } else if (lineName.equals("Downtown Line")) {

            result = this.DTline.get(stationName);

        } else if (lineName.equals("Circle Line")) {

            result = this.CCline.get(stationName);

        } else if (lineName.equals("Thomson East Coast Line")) {

            result = this.TELine.get(stationName);

        } else if (lineName.equals("Bukit Panjang LRT")) {

            result = this.BPline.get(stationName);

        } else if (lineName.equals("Sengkang LRT")) {

            result = this.SLRTline.get(stationName);

        } else if (lineName.equals("Punggol LRT")) {

            result = this.PLRTline.get(stationName);

        } else {

            result = stationName + " not found";
            System.out.println(result);
        } 

        return result;

    }

    public static String returnTrainLineForAPI(String trainCode){
        
        String result = "Not found";

        if(trainCode.startsWith(("SE")) || 
        trainCode.startsWith(("SW")) ||
        trainCode.startsWith(("ST"))){

            result = "SLRT";
        } else if (trainCode.startsWith("NS")) {
            
            result = "NSL";
        } else if (trainCode.startsWith("CC")) {
            
            result = "CCL";
        } else if (trainCode.startsWith("CE")) {
            
            result = "CEL";
        }  else if (trainCode.startsWith("CG")) {
            
            result = "CGL";
        }  else if (trainCode.startsWith("DT")) {
            
            result = "DTL";
        }  else if (trainCode.startsWith("EW")) {
            
            result = "EWL";
        }  else if (trainCode.startsWith("NE")) {
            
            result = "NEL";
        }  else if (trainCode.startsWith("BP")) {
            
            result = "BPL";
        }  else if (trainCode.startsWith(("PE")) || 
        trainCode.startsWith(("PW")) ||
        trainCode.startsWith(("PT"))) {
            
            result = "PLRT";
        }  else if (trainCode.startsWith("TE")) {
            
            result = "TEL";
        }



        return result;
    }

}
