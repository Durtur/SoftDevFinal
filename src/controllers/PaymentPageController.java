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
        System.out.println("Name: " + name + ". Ssn: " + ssn + ". CheckBags: " + cb1 + ". CarryBags: " + cb2
                            + ". Card: " + card + ". Expiry: " + expiry + ". Security: " + security);
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
    
}
