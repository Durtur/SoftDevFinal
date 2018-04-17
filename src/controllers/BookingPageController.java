/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
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
    PaymentPageController payment;
    
    @FXML
    private Pane headerPane;
    @FXML
    private FlowPane bookableFlights;
    @FXML
    private Button backButton;

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
        allSearchFields.setLayoutX(200.0);
        allSearchFields.setLayoutY(180.0);
        //allSearchFields.setPadding(new Insets(50, 50, 50, 50));
        //HBox box = new HBox();
        //box.getChildren().add(allSearchFields);
        //box.setLayoutX(100.0);
        //box.setPadding(new Insets(10, 10, 10, 10));
        
        headerPane.getChildren().add(allSearchFields);
        
        //headerPane.setPadding(new Insets(50, 50, 50, 50));
        

        ArrayList<Flight> flights = searchPage.getFoundFlights();
        Flight flight;
        int flightIndex = 0;

        for (int i = 0; i < flights.size(); i++) {

            flight = flights.get(i);
            Button button = new Button();
            Label label = new Label();
            ArrayList<Label> labels = new ArrayList();
            label.setText(flight.getFlightNumber() + " - " + flight.getAirline()
                    + "\n" + "From " + flight.getDepartureAirport() + " to " + flight.getArrivalAirport() + "\n" + flight.getDepartureTime()
                    + "\n" + flight.getDuration() + " minutes");
            label.setPrefWidth(350);
            //label.setPadding(new Insets(100, 0, 0, 0));
            labels.add(label);
            label.getStyleClass().add("flight_label");
            int price = flight.getPrice();
            if (!isOneWay) {
                i++;
                flight = flights.get(i);
                //Here we add a return FLIGHT object
                Label label2 = new Label();
                label2.setPrefWidth(350);
                label2.setText(flight.getFlightNumber() + " - " + flight.getAirline()
                        + "\n" + "From " + flight.getDepartureAirport() + " to " + flight.getArrivalAirport() + "\n" + flight.getDepartureTime()
                        + "\n" + flight.getDuration() + " minutes");
                label2.getStyleClass().add("flight_label");
                labels.add(label2);
                price += flight.getPrice();
            }
            price *= noPassengers;
            String passengersString = " passenger";
            if (noPassengers > 1) {
                passengersString = " passengers";
            }
            Label priceLabel = new Label();
            priceLabel.getStyleClass().add("price_label");
            priceLabel.setText(noPassengers + passengersString + " \n" + KronutoluParser.parse(price) + " kr");
            labels.add(priceLabel);

            HBox tp = new HBox();
            tp.getStyleClass().add("bookable_flight_row");
            tp.setPrefWidth(1300);
            tp.setSpacing(50);
            tp.setLayoutX(300.0);
            for (Label l : labels) {
                tp.getChildren().add(l);
            }
            tp.getChildren().add(button);
            button.setText("Book");
            //button.setLayoutY(50);
            button.getStyleClass().add("search_button");

            button.setOnAction(new EventHandler() {
                @Override
                public void handle(Event e) {
                    Button b = (Button) e.getSource();
                    int flightToBookIndex = bookingButtons.indexOf(b);
                    try {
                        goToPaymentPage(flightToBookIndex);
                    } catch (IOException ex) {
                        Logger.getLogger(BookingPageController.class.getName()).log(Level.SEVERE, null, ex);
                    }
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
    private void goToPaymentPage(int flightToBookIndex) throws IOException {

        try {

            ArrayList<Flight> flights = searchPage.getFoundFlights();
            ArrayList<Flight> twoFlightsArray = new ArrayList();
            
            Flight myFlightThere;
            Flight myFlightBack;
            
                  
            if(!searchPage.isOneWay()){
                myFlightThere = flights.get(flightToBookIndex*2);
                twoFlightsArray.add(myFlightThere);             
                myFlightBack = flights.get((flightToBookIndex*2)+1);
                twoFlightsArray.add(myFlightBack);
            } 
            
            if(searchPage.isOneWay()){
                myFlightThere = flights.get(flightToBookIndex);
                twoFlightsArray.add(myFlightThere);  
                myFlightBack = null;
                twoFlightsArray.add(myFlightBack);
            }
            
            int numPassengers = searchPage.getNumPassengers();

            //System.out.println("the flight is " + myFlightThere);
            //if(!searchPage.isOneWay()){
            //    System.out.println("the flight is " + myFlightBack);
            //}
            
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/PaymentPage.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            payment = (PaymentPageController) fxmlLoader.getController();
            payment.setFlightInfo(twoFlightsArray, numPassengers);
            stage.setTitle("Booking details");
            stage.setScene(new Scene(root, 985, 1400));
            
            stage.show();

        } catch (IOException e) {
            Logger.getLogger(BookingPageController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    void setSearchController(SearchpageController aThis) {
        searchPage = aThis;
    }

    @FXML
    private void goBack(ActionEvent event) {
        Parent root;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Searchpage.fxml"));
        try {
            root = (Parent) loader.load();
            Scene scene = new Scene(root);
            Stage stage = searchPage.getStage();
            stage.setScene(scene);
            stage.show();
            SearchpageController controller = (SearchpageController) loader.getController();
            controller.setStage(stage);
        } catch (IOException ex) {
            Logger.getLogger(BookingPageController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
