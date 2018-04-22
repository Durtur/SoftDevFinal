/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 *
 * @author unnur
 */
public class TestController {
    
    @FXML
    private Label flightInfo;
    @FXML
    private TextField fullNameInput;
    @FXML
    private TextField ssnInput;
    @FXML
    private TextField cardNumberInput;
    @FXML
    private TextField expiryDateInput;
    @FXML
    private TextField securityNumberInput;
    @FXML
    private ChoiceBox checkedBagsInput;
    @FXML
    private ChoiceBox carryOnBagsInput;
    @FXML
    private Button confirm;
}
