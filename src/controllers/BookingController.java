/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import database.DatabaseManager;
import java.util.ArrayList;
import model.Booking;
import model.Card;
import model.Flight;

/**
 *
 * @author Símon Örn Reynisson <sor7@hi.is>
 */
public class BookingController {
    private BookingController bookingController;
    private DatabaseManager db;
    private PaymentManager pm;
    private PaymentPageController ppc;
    
    public BookingController() {
        bookingController = new BookingController();
        db = new DatabaseManager();
        pm = new PaymentManager();
        ppc = new PaymentPageController();
    }
    
    public BookingController(BookingController bookingController, DatabaseManager db, PaymentManager pm, PaymentPageController ppc) {
        this.bookingController = bookingController;
        this.db = db;
        this.pm = pm;
        this.ppc = ppc;
    }
    
    /**
     * 
     * @param booking 
     */
    private void updateBookings(Booking booking) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private boolean pay(Card cardInfo) {
        return pm.processPayment(cardInfo);
    }
    
    public boolean book(ArrayList<Flight> flight, ArrayList<String> name, ArrayList<String> ssn) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
