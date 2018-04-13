/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.ResourceBundle;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.TextAlignment;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import model.Flight;

/**
 * FXML Controller class
 *
 * @author unnur
 */
public class PaymentPageController implements Initializable {
    
    ObservableList carryBagsP1 = FXCollections.observableArrayList();
    ObservableList checkBagsP1 = FXCollections.observableArrayList();
    
    BookingPageController booking;
    
    public Flight currFlight;
    
    private static String username = "softdevtest2018";
    private static String password = "throunhugbunadar";
    private static String recipient = "softdevtest2018@gmail.com";
    
    private String from = username;
    private String pass = password;
    private String[] to = {recipient};
    private String subject;
    private String body; 
    
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
        cardNumberInput.setPromptText("Enter 16 digits no spaces");
        expiryDateInput.setPromptText("Enter 4 digits no spaces");
        ssnInput.setPromptText("Enter 10 digits no spaces");
        
        //disables confirm button until all TextField are entered 
        BooleanBinding booleanBind = fullNameInput.textProperty().isEmpty()
                            .or(ssnInput.textProperty().isEmpty())
                                      .or(cardNumberInput.textProperty().isEmpty())
                                        .or(expiryDateInput.textProperty().isEmpty())
                                            .or(securityNumberInput.textProperty().isEmpty());
        confirm.disableProperty().bind(booleanBind);
        
        /*carryBagsP1.addListChangeListener(new ListChangeListener() {
            @Override
            public void onChanged(ListChangeListener.Change c) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });*/
        carryBagsP1.addListener((ListChangeListener)(c -> {
            System.out.println("Bags changed");/* ... */}));
    }    
    
    /**
     * checks if inputs are valid and sends confirmation email if so 
     * 
     */
    @FXML
    private void confirmBooking() {
        String name = fullNameInput.getText();
        String ssn = ssnInput.getText();
        String cb1 = checkedBagsInput.getValue();
        String cb2 = carryOnBagsInput.getValue();
        String card = cardNumberInput.getText();
        String expiry = expiryDateInput.getText();
        String security = securityNumberInput.getText();
        
        if (validateName(name) != true) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setHeaderText(null);
            //alert.setTitle("Invalid ssn");
            alert.setContentText("Please enter a name");
            alert.show();
            return;
        }
        
        if (validateSsn(ssn) != true) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setTitle("Invalid ssn");
            alert.setContentText("Please enter valid ssn");
            alert.show();
            return;
        }
        
        if (validateCheckBags(cb1) != true) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setHeaderText(null);
            //alert.setTitle("Please select check-in bags");
            alert.setContentText("Please select check-in bags");
            alert.show();
            return;
        }
        
        if (validateCarryBags(cb2) != true) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setHeaderText(null);
            //alert.setTitle("Please select carry-on bags");
            alert.setContentText("Please select carry-on bags");
            alert.show();
            return;
        }
        
        if (validateCard(card) != true) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setTitle("Invalid card number");
            alert.setContentText("Please enter valid card number");
            alert.show();
            return;
        }
        
        if (validateExpiry(expiry) != true) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setTitle("Invalid expiry date");
            alert.setContentText("Please enter a valid expiry date");
            alert.show();
            return;
        }
        
        if (validateSecurity(security) != true) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setTitle("Invalid security number");
            alert.setContentText("Please enter valid security number");
            alert.show();
            return;
        } 
        
        else {
            sendFromGMail(from, pass, to, subject, body);
        }
    }
    
    
    
    /**
     * adds numbers into choice boxes for bags
     */
    private void populateBoxes() {
        carryBagsP1.removeAll(carryBagsP1);
        checkBagsP1.removeAll(checkBagsP1);
        int carryBagPrice = 0;
        int checkBagPrice = 0;
        for (int i = 0; i < 5; i++) {
            String a = "" + i + " - " + carryBagPrice + " kr";
            String b = "" + i + " - " + checkBagPrice + " kr";
            carryBagsP1.add(a);
            checkBagsP1.add(b);
            carryBagPrice += 1000;
            checkBagPrice += 1500;
        }
        /*
        String z = "0";
        String a = "1";
        String b = "2";
        String c = "3";
        String d = "4";
        carryBagsP1.addAll(z, a, b, c, d);
        checkBagsP1.addAll(z, a, b, c, d);*/
        checkedBagsInput.getItems().addAll(checkBagsP1);
        carryOnBagsInput.getItems().addAll(carryBagsP1);
    }
    
    @FXML
    public void setFlightInfo(Flight currentFlight) {
        SimpleDateFormat ft = 
        new SimpleDateFormat ("E dd MMM yyyy 'at' HH:mm");
        currFlight = currentFlight;
        flightInfo.setText("Flight " + currFlight.getFlightNumber() + " from " + currFlight.getDepartureAirport()
            + " to " + currFlight.getArrivalAirport() + " on " + ft.format(currFlight.getDepartureTime()) + "\nPrice "
            + currFlight.getPrice() + " kr");
        
        subject = "Booking information of your trip to " + currFlight.getArrivalAirport();
        
        body = "Thanks for using FlightCo! Here's some information about your flight: \n"+ currFlight.getFlightNumber() + " - " + currFlight.getAirline()
                    + "\n" + "From " + currFlight.getDepartureAirport() + " to " + currFlight.getArrivalAirport() + "\n" + ft.format(currFlight.getDepartureTime())
                    + "\n" + currFlight.getDuration() + " minutes \n"
                + "________________ \n"
                + "You have booked: " + carryOnBagsInput.getValue() + " carry on bags, and: " + checkedBagsInput.getValue() + " checked bags.";
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
    
    private boolean validateName(String name) {
        boolean correct;
        correct = !"".equals(fullNameInput.getText());
        return correct;
    }
    
    private boolean validateExpiry(String expiry) {
        boolean correct;
        correct = expiryDateInput.getText().length() == 4;
        return correct;
    }

    /**
     * sends an email to the person who booked
     * @param from
     * @param pass
     * @param to
     * @param subject
     * @param body 
     */
    private static void sendFromGMail(String from, String pass, String[] to, String subject, String body) {
        Properties props = System.getProperties();
        String host = "smtp.gmail.com";
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", from);
        props.put("mail.smtp.password", pass);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        
        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);
        
        try {
            message.setFrom(new InternetAddress(from));
            InternetAddress[] toAddress;
            toAddress = new InternetAddress[to.length];
            
            for( int i = 0; i < to.length; i++ ) {
                toAddress[i] = new InternetAddress(to[i]);
            }
            
            for( int i = 0; i < toAddress.length; i++) {
                message.addRecipient(Message.RecipientType.TO, toAddress[i]);
            }
                   
            message.setSubject(subject);
            message.setText(body);
            Transport transport = session.getTransport("smtp");
            transport.connect(host, from, pass);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        
        } catch (AddressException ae) {
            ae.printStackTrace();
        } catch (MessagingException me) {
            me.printStackTrace();
        }
    }
    
}
