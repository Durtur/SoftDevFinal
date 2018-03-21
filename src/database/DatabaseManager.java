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

/**
 * Connects to an existing database (Not ready).
 *
 * @author Símon Örn Reynisson <sor7@hi.is>
 */
public class DatabaseManager {

    private final String dbName = getClass().getResource("flightsearch.db").toString();

    /**
     * Connects to a database that has the same name as dbName variable.
     *
     * @return the database connection, or null if the connection failed.
     * @throws Exception
     */
    private Connection connect() throws Exception {
        Connection conn;
        try {
            // Reynum fyrst PostgreSQL
            Class.forName("org.postgresql.Driver");	// fyrir PostgreSQL
            java.util.Properties props = new java.util.Properties();
            conn = DriverManager.getConnection("jdbc:postgresql:" + dbName, props);
        } catch (Exception e) {
            // Höldum bara áfram og reynum SQLite
            try {
                Class.forName("org.sqlite.JDBC");		// fyrir SQLite
                conn = DriverManager.getConnection("jdbc:sqlite::resource:" + dbName );
            } catch (Exception e2) {
                // Höldum áfram og reynum ODBC
                conn = DriverManager.getConnection("jdbc:odbc:" + dbName);
            }
        }
        return conn;

        // Þegar hér er komið erum við með tengingu við gagnagrunn
        // (conn) sem er annaðhvort PostgreSQL, SQLite eða ODBC.
//		String ssn, lname;
//		double salary;
//		String stmt1 = "select Lname, Salary from EMPLOYEE where ssn = ?";
//		PreparedStatement p = conn.prepareStatement(stmt1);
//		System.out.print("Enter a Social Security Number: ");
//		Scanner scanner = new Scanner(System.in);
//		ssn = scanner.nextLine();
//		p.clearParameters();
//		p.setString(1,ssn);
//		ResultSet r = p.executeQuery();
//		while( r.next() )
//		{
//			lname = r.getString(1);
//			salary = r.getDouble(2);
//			System.out.println(lname+" "+salary);
//		}
//		r.close();
//		conn.close();
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
            PreparedStatement p = conn.prepareStatement("Select * from Airlines");
            ResultSet r = p.executeQuery();
            while (r.next()) {
              
                airports.add(r.getString(1));

            }

        } catch (Exception ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return airports;
    }

    public ArrayList<Flight> getFlights() {
        ArrayList<Flight> flights = new ArrayList();
        Flight currentFlight;
         SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date departureTime,arrivalTime;
        try {
            Connection conn = connect();
            PreparedStatement p = conn.prepareStatement("Select * from Flights");
            ResultSet r = p.executeQuery();
            while (r.next()) {
                currentFlight=new Flight();
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
    private static int lastIndex = 0;

    public static String rotateString(String s) {
        if (lastIndex == 5) {
            lastIndex = 0;
        }

        int charIndex = (int) s.charAt(lastIndex);
        charIndex++;
        if(charIndex>90)charIndex=65;
        StringBuilder stringbuilder = new StringBuilder(s);
        stringbuilder.setCharAt(lastIndex, (char) charIndex);
        lastIndex++;

        return stringbuilder.toString();

    }
    
    private void generateRandomFlights(){
         DatabaseManager db = new DatabaseManager();
        try {
            Connection conn = db.connect();
            ArrayList<Airline> airlines = db.getAirlines();
            ArrayList<String> airports = db.getAirports();

            String sql = "INSERT INTO Flights(airline,flightNumber,departureTime,arrivalTime,departureAirport,arrivalAirport,price,duration) VALUES(?,?,?,?,?,?,?,?)";
            PreparedStatement p = conn.prepareStatement(sql);
            String currentDestination, currentDeparture, airlinePrefix;
            String flightNo = "AKGLC";
            int[] prices = new int[]{12000, 24000, 36000, 13000, 130000, 200000, 56000, 51000, 48000, 66000, 92000, 450000};

            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            Date departureTime, arrivalTime;
            for (Airline a : airlines) {
                for (int j = 0; j < 10; j++) {

                    currentDeparture = airports.get((int) (Math.random() * airports.size()));
                    currentDestination = airports.get((int) (Math.random() * airports.size()));
                    flightNo = rotateString(flightNo);
                    airlinePrefix = a.getName().substring(0, 2).toUpperCase();

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
        ArrayList<Flight> airlines =db.getFlights();
        for(Flight f:airlines){
            System.out.println(f);
        }
       
    }
}
