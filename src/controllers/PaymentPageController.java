/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author unnur
 */
public class PaymentPageController implements Initializable {
    
    ObservableList carryBags = FXCollections.observableArrayList();
    ObservableList checkBags = FXCollections.observableArrayList();
    
    BookingPageController booking;

    @FXML
    private TextField fullNameInput;
    @FXML
    private TextField ssnInput;
    @FXML
    private ChoiceBox<String> checkedBagsInput;
    @FXML
    private ChoiceBox<String> carryOnBagsInput;
    @FXML
    private TextField cardNumberInput;
    @FXML
    private TextField expiryDateInput;
    @FXML
    private TextField securityNumberInput;
    @FXML
    private Label flightInfo;
    @FXML
    private Button confirm;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        populateBoxes();
        
    }    
    
    @FXML
    private void confirmBooking() {
        String name = fullNameInput.getText();
        String ssn = ssnInput.getText();
        String cb1 = checkedBagsInput.getValue();
        String cb2 = carryOnBagsInput.getValue();
        String card = cardNumberInput.getText();
        String expiry = expiryDateInput.getText();
        String security = securityNumberInput.getText();
        
        if (validateCard(card) != true) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setTitle("Invalid card number");
            alert.setContentText("Please enter valid card number");
            alert.show();
        }
        
        if (validateSecurity(security) != true) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setTitle("Invalid security number");
            alert.setContentText("Please enter valid security number");
            alert.show();
        }
        
        if (validateCheckBags(cb1) != true) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setHeaderText(null);
            //alert.setTitle("Please select check-in bags");
            alert.setContentText("Please select check-in bags");
            alert.show();
        }
        
        if (validateCarryBags(cb2) != true) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setHeaderText(null);
            //alert.setTitle("Please select carry-on bags");
            alert.setContentText("Please select carry-on bags");
            alert.show();
        }
        
        if (validateSsn(ssn) != true) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setTitle("Invalid ssn");
            alert.setContentText("Please enter valid ssn");
            alert.show();
        }
        
        
    }
    
    private void populateBoxes() {
        carryBags.removeAll(carryBags);
        checkBags.removeAll(checkBags);
        String a = "1";
        String b = "2";
        String c = "3";
        carryBags.addAll(a, b, c);
        checkBags.addAll(a, b, c);
        checkedBagsInput.getItems().addAll(checkBags);
        carryOnBagsInput.getItems().addAll(carryBags);
    }
    
    @FXML
    public void setFlightInfo(String currentFlight) {
        flightInfo.setText(currentFlight);
    }
    
    private boolean validateCard(String card) {
        boolean correct;
        correct = cardNumberInput.getText().length() == 16;
        return correct;
    }
    
    private boolean validateSecurity(String security) {
        boolean correct;
        correct = securityNumberInput.getText().length() == 3;
        return correct;
    }
    
    private boolean validateCheckBags(String checkBags) {
        boolean correct;
        correct = checkedBagsInput.getValue() != null;
        return correct;
    }
    
    private boolean validateCarryBags(String carryBags) {
        boolean correct;
        correct = carryOnBagsInput.getValue() != null;
        return correct;
    }
    
    private boolean validateSsn(String ssn) {
        boolean correct;
        correct = ssnInput.getText().length() == 10;
        return correct;
    }
    
}
