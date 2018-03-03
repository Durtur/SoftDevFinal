/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;

/**
 * FXML Controller class for the BookingPage.fxml. Handles GUI elements and text
 * inputs and forwards them to BookingController.
 *
 * @author Símon Örn Reynisson <sor7@hi.is>
 */
public class BookingPageController implements Initializable {

    BookingController book;
    @FXML
    private Pane headerPane;
    @FXML
    private BorderPane bookableFlights;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    /**
     * Testing stage. Puts the searchfields from the searchpage onto the booking
     * page, and populates a single entry in bookableFlights with the search input. 
     *
     */
    public void passFlightData(LocalDate departing, String departure, LocalDate returnDate, String arrival, GridPane allSearchFields) {
        allSearchFields.setLayoutX(20.0);
        allSearchFields.setLayoutY(10.0);
        headerPane.getChildren().add(allSearchFields);
      
        
        Button button = new Button();
        Label label = new Label();
        ArrayList<Label> labels = new ArrayList();
        label.setText("From " + departure + " to " + arrival + "\n" + departing.toString());
        labels.add(label);
        if (returnDate != null) {
        
            Label label2 = new Label();
            label2.setText("From " + arrival + " to " + departure + "\n" + returnDate.toString());
            labels.add(label2);
        }
 
        TilePane tp = new TilePane();
        tp.setHgap(50);
        for(Label l: labels){
            tp.getChildren().add(l);
        }
        tp.getChildren().add(button);
        button.setText("Book");
        button.getStyleClass().add("search_button");
        
        bookableFlights.setCenter(tp);
    }

}
