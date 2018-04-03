/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import database.DatabaseManager;
import java.util.ArrayList;

/**
 *
 * @author Símon Örn Reynisson <sor7@hi.is>
 */
public class OfferManager {
    private DatabaseManager db;
    
    public OfferManager(DatabaseManager db) {
        this.db = db;
    }
    
    /**
     * 
     * @return An ArrayList with offers from DatabaseManager
     */
    public ArrayList<String> getOffers() {
        //hasn't been implemented in DatabaseManager yet, therefore placement return
        //return db.getOffers();
        return new ArrayList<String>();
    }
}
