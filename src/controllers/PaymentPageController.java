/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import database.DatabaseManager;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import model.Booking;
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
    DatabaseManager db; 
    
    public Flight currFlight;
    public Flight currFlightBack;
    int numPassengers;
    
    
    private static String username = "softdevtest2018";
    private static String password = "throunhugbunadar";
    private String recipient;
    
    private String from = username;
    private String pass = password;
    private ArrayList<String> recip = new ArrayList<String>();
    private String subject;
    private String body; 
    
    SimpleDateFormat ft = new SimpleDateFormat ("E dd MMM yyyy 'at' HH:mm");
    
    ArrayList<TextField> allSsn,allNameFields;
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
    private TextField emailInput1;
    @FXML
    private Label flightInfo;
    @FXML
    private Button confirm;
    @FXML
    private Label confirmBook;
    @FXML
    private Label emailSending;
    @FXML
    private Button butES;
    @FXML
    private Label labelYourBook;
    @FXML
    private Label labelFullName;
    @FXML
    private Label labelSsn;
    @FXML
    private Label labelChecked;
    @FXML
    private Label labelCarryOn;
    @FXML
    private Label labelCardNum;
    @FXML
    private Label labelExpiry;
    @FXML
    private Label labelSecurity;
    @FXML
    private Label labelEmail;
    
    private ResourceBundle bundle;
    private Locale locale;
    
    @FXML
    private VBox additionalPassengers;
    @FXML
    private Label additionalPassengersLabel;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        db = new DatabaseManager();
        
        allNameFields = new ArrayList();
        allSsn=new ArrayList();
        allSsn.add(ssnInput);
        allNameFields.add(fullNameInput);
        
        populateBoxes();
        cardNumberInput.setPromptText("Enter 16 digits no spaces");
        expiryDateInput.setPromptText("Enter 4 digits no spaces");
        ssnInput.setPromptText("Enter 10 digits no spaces");
        
        // Positioning confirmation text that appears after pressing "Book"
        confirmBook.setMaxWidth(Double.MAX_VALUE);
        AnchorPane.setLeftAnchor(confirmBook, 0.0);
        AnchorPane.setRightAnchor(confirmBook, 0.0);
        confirmBook.setAlignment(Pos.CENTER);
        confirmBook.setAlignment(Pos.CENTER);
        
        emailSending.setMaxWidth(Double.MAX_VALUE);
        AnchorPane.setLeftAnchor(emailSending, 0.0);
        AnchorPane.setRightAnchor(emailSending, 0.0);
        emailSending.setAlignment(Pos.CENTER);
        emailSending.setAlignment(Pos.CENTER);
        
        //disables confirm button until all TextFields are entered 
        BooleanBinding booleanBind = fullNameInput.textProperty().isEmpty()
                            .or(ssnInput.textProperty().isEmpty())
                                      .or(cardNumberInput.textProperty().isEmpty())
                                        .or(expiryDateInput.textProperty().isEmpty())
                                            .or(securityNumberInput.textProperty().isEmpty())
                                                .or(emailInput1.textProperty().isEmpty());
        confirm.disableProperty().bind(booleanBind);
        
        /*carryBagsP1.addListChangeListener(new ListChangeListener() {
            @Override
            public void onChanged(ListChangeListener.Change c) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });*/
        
    }    
    
    
    private void addPassengerFields(int num) {
        HBox currentRow;
        TextField nextName, nextSsn;
        for (int i = 1; i < num; i++) {
            currentRow = new HBox();
            nextName = new TextField();
            nextSsn = new TextField();
            nextName.setPromptText("Name of passenger " + (i+1));
            nextSsn.setPromptText("Social security number of passenger " + (i+1));
            
            allNameFields.add(nextName);
            allSsn.add(nextSsn);
            currentRow.getChildren().addAll(nextName, nextSsn);
            additionalPassengers.getChildren().add(currentRow); 
        }
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
            alert.setContentText("Please select check-in bags");
            alert.show();
            return;
        }
        
        if (validateCarryBags(cb2) != true) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setHeaderText(null);
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
            carryBagsP1.addListener((ListChangeListener)(c -> {
                
            System.out.println("Bags changed");/* ... */}));
            
        /**
         * creates the booking
         */
        String[] namesToBook = new String[numPassengers];
        String[] ssnToBook = new String[numPassengers];
        
        for (int i = 0; i < allSsn.size(); i++) {
            namesToBook[i] = allNameFields.get(i).getText();
            ssnToBook[i] = allSsn.get(i).getText();
        }
        
        ArrayList<Flight> flightsToBook = new ArrayList();
        
        flightsToBook.add(currFlight);
        if (currFlightBack != null) flightsToBook.add(currFlightBack);
        
                                    //(ArrayList<Flight> flights, String bookingNo, int noPassengers, int carryOnBags, int checkInBags, String[] fullName, String[] ssn)
        Booking booking = new Booking(flightsToBook, null, numPassengers, numberOfCarryOnBags(), numberOfCheckInBags(), namesToBook, ssnToBook);
    
        String bookingNumbers = db.addBooking(booking);
            
            subject = "Booking information of your trip to " + currFlight.getArrivalAirport();
        
            
                body = "Thanks for using FlightCo! Here's some information about your flight: \n"
                        + "________________ \n" 
                        + "Booking number: " + bookingNumbers.substring(0,5) + "\n"
                        + "Flight " + currFlight.getFlightNumber() + " - " + currFlight.getAirline()
                        + "\n" + "From " + currFlight.getDepartureAirport() + " to " + currFlight.getArrivalAirport() + "\n" + "On " + ft.format(currFlight.getDepartureTime())
                        + "\n" + "Duration " + currFlight.getDuration() + " minutes"
                        + "\n" + "Arrival on " + ft.format(currFlight.getArrivalTime()) + "\n"
                        + "________________ \n";
                if (currFlightBack != null) {
                     body=body+  "Booking number: " + bookingNumbers.substring(5,10)+ "\n"
                        + "Return Flight " + currFlightBack.getFlightNumber() + " - " + currFlightBack.getAirline()
                        + "\n" + "From " + currFlightBack.getDepartureAirport() + " to " + currFlightBack.getArrivalAirport() + "\n" + "On " + ft.format(currFlightBack.getDepartureTime())
                        + "\n" + "Duration " + currFlightBack.getDuration() + " minutes"
                        + "\n" + "Arrival on " + ft.format(currFlightBack.getArrivalTime()) + "\n"
                    + "________________ \n"
                    + "You have booked: " + numberOfCarryOnBags() + " carry on bags " + "(+" + getPriceOfCOB() + " kr), and " + numberOfCheckInBags() + " checked bags "+ "(+" + getPriceOfCB() +  " kr). \n"
                    + "Total price: " + ((currFlight.getPrice()*numPassengers) + (currFlightBack.getPrice()*numPassengers)+ getPriceOfCOB() + getPriceOfCB()) +" kr. \n"   
                    + "________________ \n"
                    + "Passenger/s: " + numPassengers +"\n";
                 
                }
                else {
                    body=body+"________________ \n"
                    + "You have booked: " + numberOfCarryOnBags() + " carry on bags " + "(+" + getPriceOfCOB() + " kr), and " + numberOfCheckInBags() + " checked bags "+ "(+" + getPriceOfCB() +  " kr). \n"
                    + "Total price: " + ((currFlight.getPrice()*numPassengers) + getPriceOfCOB() + getPriceOfCB()) +" kr. \n"   
                    + "________________ \n"
                    + "Passenger/s: " + numPassengers +"\n";
                 
                }
            for (int i = 0; i < allNameFields.size(); i++) {
                  body = body + allNameFields.get(i).getText() + " ssn: " + allSsn.get(i).getText() + "\n"; 
            }
            
            confirmBook.setText("Booking confirmed!");
            emailSending.setText("An email has been sent to " + emailInput1.getText());
            sendFromGMail(from, pass, recip, subject, body);
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
        checkedBagsInput.getItems().addAll(checkBagsP1);
        carryOnBagsInput.getItems().addAll(carryBagsP1);
    }
    
     /**
     *   Methods to get number of carry-on bags and the price
     */
    private int numberOfCarryOnBags(){
        int numCOB = Integer.parseInt(carryOnBagsInput.getValue().substring(0, 1));

        return numCOB;
    }
    private int getPriceOfCOB() {  
        String priceCOB = carryOnBagsInput.getValue();
        if ("0".equals(priceCOB.substring(0, 1))) priceCOB = "000000000";
        
        int price = Integer.parseInt(priceCOB.substring(4, 8));       
        
        return price;
    }
    
    /**
     *  Methods to get number of check-in bags and the price
     */
    private int numberOfCheckInBags(){
        int numCB = Integer.parseInt(checkedBagsInput.getValue().substring(0, 1));

        return numCB;
    }
    
    private int getPriceOfCB(){
        String priceCB = checkedBagsInput.getValue();
        if("0".equals(priceCB.substring(0,1))) priceCB = "000000000";
   
        int price = Integer.parseInt(priceCB.substring(4, 8));
      
        return price;
    }
    
    
    public void setFlightInfo(ArrayList<Flight> twoFlightsArray, int passengers) {
        if (passengers > 1) {
            addPassengerFields(passengers);
        }
        else {
            additionalPassengersLabel.setVisible(false);
        }
        currFlight = twoFlightsArray.get(0);
        currFlightBack = twoFlightsArray.get(1);
        numPassengers = passengers;

        if (currFlightBack != null) {
            flightInfo.setText("Outbound Flight: " + currFlight.getFlightNumber() + " from " + currFlight.getDepartureAirport()
                    + " to " + currFlight.getArrivalAirport() + " on " + ft.format(currFlight.getDepartureTime()) + "\nPrice "
                    + (currFlight.getPrice()*numPassengers) + " kr" + "\n"
                    + "\n"
                    + "Return Flight: " + currFlightBack.getFlightNumber() + " from " + currFlightBack.getDepartureAirport()
                    + " to " + currFlightBack.getArrivalAirport() + " on " + ft.format(currFlightBack.getDepartureTime()) + "\nPrice "
                    + (currFlightBack.getPrice()*numPassengers) + " kr");
        }

        if (currFlightBack == null) {
            flightInfo.setText("Flight " + currFlight.getFlightNumber() + " from " + currFlight.getDepartureAirport()
                + " to " + currFlight.getArrivalAirport() + " on " + ft.format(currFlight.getDepartureTime()) + "\nPrice "
                + (currFlight.getPrice()*numPassengers) + " kr");
        }
    
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
    private void sendFromGMail(String from, String pass, ArrayList recip, String subject, String body) {
        recipient = emailInput1.getText();
        recip.add(recipient);
        
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
            toAddress = new InternetAddress[recip.size()];
            
            for( int i = 0; i < recip.size(); i++ ) {
                toAddress[i] = new InternetAddress((String) recip.get(i));
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
    
    @FXML private void btnES(ActionEvent event) {
        loadLang("es");
    }
    
    private void loadLang(String lang) {
        locale = new Locale(lang);
        bundle = ResourceBundle.getBundle("utils.lang", locale);
        labelYourBook.setText(bundle.getString("labelYourBook"));
        labelFullName.setText(bundle.getString("labelFullName"));
        labelSsn.setText(bundle.getString("labelSsn"));
        labelChecked.setText(bundle.getString("labelChecked"));
        labelCarryOn.setText(bundle.getString("labelCarryOn"));
        labelCardNum.setText(bundle.getString("labelCardNum"));
        labelExpiry.setText(bundle.getString("labelExpiry"));
        labelSecurity.setText(bundle.getString("labelSecurity"));
        labelEmail.setText(bundle.getString("labelEmail"));
        confirm.setText(bundle.getString("confirm"));
    }
    
}
