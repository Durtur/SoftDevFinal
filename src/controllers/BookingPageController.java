/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import model.Flight;
import utils.KronutoluParser;

/**
 * FXML Controller class for the BookingPage.fxml. Handles GUI elements and text
 * inputs and forwards them to BookingController.
 *
 * @author Símon Örn Reynisson <sor7@hi.is>
 */
public class BookingPageController implements Initializable {

    ArrayList<Button> bookingButtons;
    SearchpageController searchPage;
    BookingController book;
    @FXML
    private Pane headerPane;
    @FXML
    private FlowPane bookableFlights;

//    private OnClickListener bookingButtonListener;
    /**
     * Initializes the controller class. This function is automatically called
     * when the controller is created.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        bookingButtons = new ArrayList();
    }

    /**
     * Testing stage. Puts the searchfields from the searchpage onto the booking
     * page, and populates a single entry in bookableFlights with the search
     * input.
     *
     */
    public void passFlightData(GridPane allSearchFields, boolean isOneWay, int noPassengers) {
        allSearchFields.setLayoutX(20.0);
        allSearchFields.setLayoutY(10.0);
        headerPane.getChildren().add(allSearchFields);

        ArrayList<Flight> flights = searchPage.getFoundFlights();
        Flight flight;
        int flightIndex = 0;

        for (int i = 0; i < flights.size(); i++) {

            flight = flights.get(i);
            Button button = new Button();
            Label label = new Label();
            ArrayList<Label> labels = new ArrayList();
            label.setText(flight.getFlightNumber() + " - " + flight.getAirline() + "\n" + "From " + flight.getDepartureAirport() + " to " + flight.getArrivalAirport() + "\n" + flight.getDepartureTime());
            label.setPrefWidth(500);
            labels.add(label);
            int price = flight.getPrice();
            if (!isOneWay) {
                i++;
                flight = flights.get(i);
                //Here we add a return FLIGHT object
                Label label2 = new Label();
                label2.setText("From " + flight.getDepartureAirport() + " to " + flight.getArrivalAirport() + "\n" + flight.getDepartureTime());
                labels.add(label2);
                price+=flight.getPrice();
            }
            price*=noPassengers;
            String passengersString = " passenger";
            if(noPassengers>1){
                passengersString = " passengers";
            }
            Label priceLabel = new Label();
            priceLabel.setText(noPassengers + passengersString+ " \n" + KronutoluParser.parse(price) + " kr");
            labels.add(priceLabel);

            HBox tp = new HBox();
            tp.setPrefWidth(800);
            tp.setSpacing(50);
            for (Label l : labels) {
                tp.getChildren().add(l);
            }
            tp.getChildren().add(button);
            button.setText("Book");
            button.getStyleClass().add("search_button");

            button.setOnAction(new EventHandler() {
                @Override
                public void handle(Event e) {
                    Button b = (Button) e.getSource();
                    int flightToBookIndex = bookingButtons.indexOf(b);
                    goToPaymentPage(flightToBookIndex);
                }

            });

            bookingButtons.add(button);
            bookableFlights.getChildren().add(tp);
        }

    }

    /**
     * This method should switch the view to a payment page, either by changing
     * the scene or by changing the stackpane. Get the flight chosen by using
     * the index parameter.
     */
    private void goToPaymentPage(int flightToBookIndex) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    void setSearchController(SearchpageController aThis) {
        searchPage = aThis;
    }

}
