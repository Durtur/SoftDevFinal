package test;

import java.util.ArrayList;

public class OffersMock {

    private ArrayList<String[]> offers;

    private void setUp() {
        offers = new ArrayList();
        String headerString = "a";
        int pathNum = 0;
        for (int i = 0; i < 10; i++) {
            headerString += "a";
            pathNum++;
            offers.add(new String[]{headerString, "offer" + pathNum + ".jgp"});
        }
    }
    
    //simulates the situation where there are offers available
    public ArrayList<String[]> getOffers() {
        return offers;
    }
    
    //simulates the situation where there are no offers
    public ArrayList<String[]> noOffers() {
        return new ArrayList();
    }
    
    
    //simulates the situation where the db cannot be accessed
    public ArrayList<String[]> noDBConnection() throws Exception {
        throw new Exception();
       
    }
}



