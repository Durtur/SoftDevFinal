/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;

/**
 *
 * @author Símon Örn Reynisson <sor7@hi.is>
 */
public class Booking {
    private ArrayList<Flight> flights;
    private String bookingNo;
    private int noPassengers, carryOnBags, checkInBags;
    private String[] fullName, ssn;
    
    public Booking(ArrayList<Flight> flights, String bookingNo, int noPassengers, int carryOnBags, int checkInBags, String[] fullName, String[] ssn) {
    	this.flights = flights;
    	this.bookingNo = bookingNo;
    	this.noPassengers = noPassengers;
    	this.carryOnBags = carryOnBags;
    	this.checkInBags = checkInBags;
    	this.fullName = fullName;
    	this.ssn = ssn;
    }
    
    public ArrayList<Flight> getFlights() {
    	return flights;
    }
    
    public void setFlights(ArrayList<Flight> flights) {
    	this.flights = flights;
    }
    
    public String getBookingNo() {
    	return bookingNo;
    }
    
    public void setBookingNo(String bookingNo) {
    	this.bookingNo = bookingNo;
    }
    
    public int getNoPassengers() {
    	return noPassengers;
    }
    
    public void setNoPassengers(int noPassengers) {
    	this.noPassengers = noPassengers;
    }
    
    public int getCarryOnBags() {
    	return carryOnBags;
    }
    
    public void setCarryOnBags(int carryOnBags) {
    	this.carryOnBags = carryOnBags;
    }
    
    public int getCheckInBags() {
    	return checkInBags;
    }
    
    public void setCheckInBags(int checkInBags) {
    	this.checkInBags = checkInBags;
    }
    
    public String[] getFullName() {
    	return fullName;
    }
    
    public void setFulLName(String [] fullName) {
    	this.fullName = fullName;
    }
    
    public String[] getSsn() {
    	return ssn;
    }
    
    public void setSsn(String [] ssn) {
    	this.ssn = ssn;
    }
}
