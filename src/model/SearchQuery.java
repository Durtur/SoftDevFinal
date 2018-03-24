/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Date;

/**
 *
 * @author Símon Örn Reynisson <sor7@hi.is>
 */
public class SearchQuery {
    private Date firstDate, secondDate;
    private String departingFrom, arrivingTo, airline;
    private int passengerNo;
    
    public SearchQuery(Date firstDate, Date secondDate, String departingFrom, String arrivingTo, String airline, int passengerNo) {
    	this.firstDate = firstDate;
    	this.secondDate = secondDate;
    	this.departingFrom = departingFrom;
    	this.arrivingTo = arrivingTo;
    	this.airline = airline;
    	this.passengerNo = passengerNo;
    }
    
    public Date getFirstDate() {
    	return ;
    }
    
    public void setFirstDate(Date firstDate) {
    	this.firstDate = firstDate;
    }
    
    public Date getSecondDate() {
    	return secondDate;
    }
    
    public void setSecondDate(Date secondDate) {
    	this.secondDate = secondDate;
    }
    
    public String getDepartingFrom() {
    	return departingFrom;
    }
    
    public void setDepartingFrom(String departingFrom) {
    	this.departingFrom = departingFrom;
    }
    
    public String getArrivingTo() {
    	return arrivingTo;
    }
    
    public void setArrivingTo(String arrivingTo) {
    	this.arrivingTo = arrivingTo;
    }
    
    public String getAirline() {
    	return airline;
    }
    
    public void setAirline(String airline) {
    	this.airline = airline;
    }
    
    public int getPassengerNo() {
    	return passengerNo;
    }
    
    public void setPassengerNo(int passengerNo) {
    	this.passengerNo = passengerNo;
    }
}
