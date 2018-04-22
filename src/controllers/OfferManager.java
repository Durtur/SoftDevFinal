package controllers;

import database.DatabaseManager;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
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
                currentImg = new Image(OfferManager.class.getClassLoader().getResourceAsStream("styles/img/" + arr[2]),goodWidth,goodHeight,true,false);
                offers.add(new Offer(currentImg, arr[1], arr[0],db.findCheapest("Keflavík",arr[0])));
            } catch (Exception ex) {
                Logger.getLogger(BookingPageController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return offers;
    }
}
