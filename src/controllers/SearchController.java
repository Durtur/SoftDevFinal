/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import database.DatabaseManager;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import model.Flight;
import model.SearchQuery;

/**
 *
 * @author Símon Örn Reynisson <sor7@hi.is>
 */
public class SearchController {

    private DatabaseManager db;

    public SearchController() {
        db = new DatabaseManager();
    }

    Collection<? extends String> getAirports() {
        return new HashSet<String>(db.getAirports());
    }

    ArrayList<Flight> search(SearchQuery sq) {
        //One way
        ArrayList<Flight> flightsThere = db.getFlights(sq.getDepartingFrom(), sq.getArrivingTo(), sq.getFirstDate(), sq.getPassengerNo());
        flightsThere = matchWithDate(flightsThere, sq.getFirstDate());
        if (sq.getSecondDate() == null) {
            return flightsThere;
        }
        
        ArrayList<Flight> flightsBack = db.getFlights(sq.getArrivingTo(), sq.getDepartingFrom(), sq.getSecondDate(), sq.getPassengerNo());
        flightsBack=matchWithDate(flightsBack,sq.getSecondDate());
        

        /**
         * Some logic needed to combine the flights to and back
         */
        return null;
    }

    /**
     * Filters out Flights that are not the same day as the given date. 
     * @param flights A list of flights
     * @param date the date to match 
     * @return A new list with flights that match. 
     */
    private ArrayList<Flight> matchWithDate(ArrayList<Flight> flights, Date date) {
        if(date == null) return flights;
        ArrayList<Flight> matchingFlights = new ArrayList<Flight>();
        for (Flight f : flights) {
            if (isSameDay(f.getDepartureTime(), date)) {
                matchingFlights.add(f);
            }
        }
        return matchingFlights;
    }

    private boolean isSameDay(Date date1, Date date2) {
  
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    protected class PriceComparator implements Comparator {

        @Override
        public int compare(Object o1, Object o2) {
            Flight fl1, fl2;
            fl1 = (Flight) o1;
            fl2 = (Flight) o2;

            return fl1.getPrice() - fl2.getPrice();
        }

    }

}
