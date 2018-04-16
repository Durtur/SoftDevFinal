/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Airline;
import model.Booking;
import model.Flight;
import model.SearchQuery;

/**
 * Connects to an existing database (Not ready).
 *
 * @author Símon Örn Reynisson <sor7@hi.is>
 */
public class DatabaseManager {

//    private final String dbName = getClass().getResource("flightsearch.db").toString();
    private final String dbName = "flightsearch.db";
    private static int lastIndex = 0;

    /**
     * Connects to a database that has the same name as dbName variable.
     *
     * @return the database connection, or null if the connection failed.
     * @throws Exception
     */
    private Connection connect() throws Exception {
        Connection conn = null;
        /*
        try {
            // Reynum fyrst PostgreSQL
            Class.forName("org.postgresql.Driver");	// fyrir PostgreSQL
            java.util.Properties props = new java.util.Properties();
            conn = DriverManager.getConnection("jdbc:postgresql:" + dbName, props);
        } catch (Exception e) {*/
        // Höldum bara áfram og reynum SQLite
        try {
            Class.forName("org.sqlite.JDBC");		// fyrir SQLite
            // conn = DriverManager.getConnection("jdbc:sqlite::resource:" + dbName );
            conn = DriverManager.getConnection("jdbc:sqlite:" + dbName);
        } catch (Exception e) {
            // Höldum áfram og reynum ODBC
            //conn = DriverManager.getConnection("jdbc:odbc:" + dbName);
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, e);
        }
        return conn;
    }

    public ArrayList<String[]> getOffers() {
        ArrayList<String[]> offers = new ArrayList();

        try {
            Connection conn = connect();
            PreparedStatement p = conn.prepareStatement("Select * from Offers");
            ResultSet r = p.executeQuery();
            while (r.next()) {
                offers.add(new String[]{r.getString(1), r.getString(2), r.getString(3)});

            }

        } catch (Exception ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return offers;
    }

    /**
     * *
     * Gets Airlines from the database.
     *
     * @return ArrayList of airlines. Null if an error occurs.
     */
    public ArrayList<Airline> getAirlines() {
        ArrayList<Airline> airlines = new ArrayList();
        Airline currentAirline;
        try {
            Connection conn = connect();
            PreparedStatement p = conn.prepareStatement("Select * from Airlines");
            ResultSet r = p.executeQuery();
            while (r.next()) {
                currentAirline = new Airline(r.getString(1), r.getInt(2));
                airlines.add(currentAirline);

            }

        } catch (Exception ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return airlines;
    }

    public ArrayList<String> getAirports() {
        ArrayList<String> airports = new ArrayList();

        try {
            Connection conn = connect();
            PreparedStatement p = conn.prepareStatement("Select * from Airports");
            ResultSet r = p.executeQuery();
            while (r.next()) {

                airports.add(r.getString(1));

            }

        } catch (Exception ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return airports;
    }

    public ArrayList<Flight> getFlights(String departingFrom, String arrivingTo, Date departureTime, int passengerNo) {
        ArrayList<Flight> flights = new ArrayList();
        Flight currentFlight;
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");

        try {
            Connection conn = connect();
            PreparedStatement p;
            String sql;
            if (arrivingTo.equals("")) {
                System.out.println("Spinning the Globe");
                sql = "Select * from Flights WHERE departureAirport=? AND FreeSeats>?";
                p = conn.prepareStatement(sql);
                p.setString(1, departingFrom);
                p.setInt(2, passengerNo);
            } else {
                sql = "Select * from Flights WHERE departureAirport=? AND arrivalAirport=? AND FreeSeats>?";
                p = conn.prepareStatement(sql);
                p.setString(1, departingFrom);
                p.setString(2, arrivingTo);
                p.setInt(3, passengerNo);
            }

            ResultSet r = p.executeQuery();
            while (r.next()) {
                currentFlight = new Flight();
                currentFlight.setAirline(r.getString(1));
                currentFlight.setFlightNumber(r.getString(2));
                currentFlight.setDepartureTime(df.parse(r.getString(3)));
                currentFlight.setArrivalTime(df.parse(r.getString(4)));
                currentFlight.setDepartureAirport(r.getString(5));
                currentFlight.setArrivalAirport(r.getString(6));
                currentFlight.setPrice(r.getInt(7));
                currentFlight.setDuration(r.getInt(8));
                flights.add(currentFlight);

            }
        } catch (Exception ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return flights;

    }

    private String getNextAvailableBookingNo(String flightNumber, Connection conn) {
        String nextBookingNumber = null;
        int lastCapitalLetterAscii = 90;
        try {
            
            String sql = "Select min(bookingNo) from Bookings where flightNumber=?";
            PreparedStatement p = conn.prepareStatement(sql);
            p.setString(1, flightNumber);
            ResultSet r = p.executeQuery();
            while (r.next()) {
                String bookingNo = r.getString(1);
                if (bookingNo == null) {
                    nextBookingNumber = "AFEJL";
                    break;
                }
                int i = bookingNo.length() - 1;
                while (((int) bookingNo.charAt(i) == lastCapitalLetterAscii)) {
                    if (i == 0) {
                        break;
                    }
                    i--;
                }
                int charIndex = (int) bookingNo.charAt(i);
                charIndex++;
                StringBuilder morph = new StringBuilder(bookingNo);
                morph.setCharAt(i, (char) charIndex);
                nextBookingNumber = morph.toString();

            }

        } catch (Exception ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return nextBookingNumber;
    }

    private void reserveFlightSeats(String flightNo, int numPassengers, Connection conn) {
        try {
            String sql = "update Flights set freeSeats=freeSeats-? where flightNumber=?";
            PreparedStatement p = conn.prepareStatement(sql);
            p.setInt(1, numPassengers);
            p.setString(2, flightNo);

            p.executeUpdate();

        } catch (Exception ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public String addBooking(Booking booking) {
        try {
            Connection conn = connect();
            ArrayList<Flight> flightsToBook = booking.getFlights();
            String finalBooking = "";
            for (Flight f : flightsToBook) {
                String sql = "INSERT INTO Bookings(flightNumber,bookingNo,noPassengers,fullName,ssn,carryOnBags,checkInBags) VALUES(?,?,?,?,?,?,?)";
                PreparedStatement p = conn.prepareStatement(sql);
                String bookingNo = getNextAvailableBookingNo(f.getFlightNumber(), conn);
                finalBooking+=bookingNo;
                int i;
                for (i = 0; i < booking.getSsn().length; i++) {
                    System.out.println(i);
                    p.setString(1, f.getFlightNumber());
                    p.setString(2, bookingNo);
                    p.setInt(3, booking.getNoPassengers());
                    p.setString(4, booking.getFullName()[i]);
                    p.setString(5, booking.getSsn()[i]);
                    p.setInt(6, booking.getCarryOnBags());
                    p.setInt(7, booking.getCheckInBags());
                    p.executeUpdate();
                }
                reserveFlightSeats(f.getFlightNumber(), i , conn);
            }
            return finalBooking;

        } catch (Exception ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            return "error";
        }

    }

    /**
     * This method is not in use. It can be used to populate the database with
     * random flights.
     */
    private void generateRandomFlights() {
        DatabaseManager db = new DatabaseManager();
        try {
            Connection conn = db.connect();
            ArrayList<Airline> airlines = db.getAirlines();
            ArrayList<String> airports = db.getAirports();

            String sql = "INSERT INTO Flights(airline,flightNumber,departureTime,arrivalTime,departureAirport,arrivalAirport,price,duration) VALUES(?,?,?,?,?,?,?,?)";
            PreparedStatement p = conn.prepareStatement(sql);
            String currentDestination, currentDeparture, airlinePrefix;
            int flightNo = 111;
            int[] prices = new int[]{12000, 24000, 36000, 13000, 130000, 200000, 56000, 51000, 48000, 66000, 92000, 450000};

            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            Date departureTime, arrivalTime;
            for (Airline a : airlines) {
                flightNo = 111;
                airlinePrefix = a.getName().substring(0, 2).toUpperCase();
                for (int j = 0; j < 600; j++) {
                    flightNo++;

                    currentDeparture = airports.get((int) (Math.random() * airports.size()));
                    currentDestination = airports.get((int) (Math.random() * airports.size()));

                    departureTime = new Date();
                    arrivalTime = new Date();
                    for (int i = 0; i < 6; i++) {
                        departureTime.setTime(departureTime.getTime() + 300000000 + (long) (Math.random() * 1000000000));
                    }

                    arrivalTime.setTime(departureTime.getTime() + 60 * 60000 + ((long) (Math.random() * 6) * 60000 * 60));

                    int flightDuration = (int) (arrivalTime.getTime() - departureTime.getTime()) / 60000;

                    while (currentDestination.equals(currentDeparture)) {
                        currentDestination = airports.get((int) (Math.random() * airports.size()));
                    }

                    p.setString(1, a.getName());
                    p.setString(2, airlinePrefix + flightNo);
                    p.setString(3, df.format(departureTime));
                    p.setString(4, df.format(arrivalTime));
                    p.setString(5, currentDeparture);
                    p.setString(6, currentDestination);
                    p.setInt(7, prices[(int) (Math.random() * prices.length)]);
                    p.setInt(8, flightDuration);

                    p.executeUpdate();

                }

            }
//            
//            p.setString(1, name);
//            p.setDouble(2, capacity);
//            p.executeUpdate();
//            

        } catch (Exception ex) {
            Logger.getLogger(DatabaseManager.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String findCheapest(String departing, String arriving) {
        System.out.println(arriving);
        Connection conn;
        try {
            conn = connect();
            PreparedStatement p;
            String sql;

            sql = "Select min(price) from Flights WHERE departureAirport=? AND arrivalAirport=? AND FreeSeats>?";
            p = conn.prepareStatement(sql);
            p.setString(1, departing);
            p.setString(2, arriving);
            p.setInt(3, 0);

            ResultSet r = p.executeQuery();
            while (r.next()) {
                System.out.println(r.getString(1));
                return r.getString(1);

            }

        } catch (Exception ex) {
            Logger.getLogger(DatabaseManager.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return "not available";

    }

    public static void main(String[] args) {
        DatabaseManager db = new DatabaseManager();

        ArrayList<Flight> testFlights = new ArrayList();
        Flight testFlight1 = new Flight();
        testFlight1.setFlightNumber("WO700");
        Flight testFlight2 = new Flight();
        testFlight2.setFlightNumber("WO701");
        testFlights.add(testFlight1);
        testFlights.add(testFlight2);
   
        Booking testBook = new Booking(testFlights, "", 2, 2, 2, new String[]{"Joe", "Mary"}, new String[]{"000124125", "00515"});
        db.addBooking(testBook);

        //db.generateRandomFlights();
    }

}
