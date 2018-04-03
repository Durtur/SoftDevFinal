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
            } catch (Exception e2) {
                // Höldum áfram og reynum ODBC
                //conn = DriverManager.getConnection("jdbc:odbc:" + dbName);
            }
        return conn;
        }
    //}

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
                System.out.println(currentFlight);

            }
        } catch (Exception ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return flights;

    }
    private static int lastIndex = 0;

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
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        DatabaseManager db = new DatabaseManager();
        // db.generateRandomFlights();

    }

}
