/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import database.DatabaseManager;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import java.util.ArrayList;
import javafx.scene.image.Image;
import model.Offer;

/**
 *
 * @author Símon Örn Reynisson <sor7@hi.is>
 */
public class OfferManager {

    private DatabaseManager db;

    public OfferManager() {
        db = new DatabaseManager();
    }

    public OfferManager(DatabaseManager db) {
        this.db = db;
    }

    /**
     *
     * @return An ArrayList with offers from DatabaseManager
     */
    public ArrayList<Offer> getOffers() {
        int goodHeight,goodWidth;
        goodHeight=575;
        goodWidth=1000;
        ArrayList<Offer> offers = new ArrayList();
        ArrayList<String[]> offerStrings = db.getOffers();
        Image currentImg;
        InputStream is = null;
        for (String[] arr : offerStrings) {
            try {
//                double requestedWidth,
//             double requestedHeight,
//             boolean preserveRatio,
//             boolean smooth,
//             boolean backgroundLoading
                currentImg = new Image(OfferManager.class.getClassLoader().getResourceAsStream("styles/img/" + arr[2]),goodWidth,goodHeight,true,false);
                
                offers.add(new Offer(currentImg, arr[1], arr[0],db.findCheapest("Keflavík",arr[0])));
            } catch (Exception ex) {

            }

        }
        return offers;
    }
}
