package controllers;

import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Flight;
import model.Offer;
import model.SearchQuery;
import utils.KronutoluParser;
import view.AutoCompleteTextField;

/**
 * FXML Controller class
 *
 * @author Símon Örn Reynisson <sor7@hi.is>
 */
public class SearchpageController implements Initializable {

    //Data variables
    ArrayList<Flight> foundFlights;
    Stage stage;  //Refers to the current window
    SearchController search; //This will do the searching. 
    AutoCompleteTextField departureField, arrivalField;
    OfferManager offerManager;
    
    @FXML
    private DatePicker firstDate;
    @FXML
    private DatePicker secondDate;
    @FXML
    private TextField departingFrom;
    @FXML
    private TextField arrivingTo;
    @FXML
    private CheckBox isOneWay;
    @FXML
    private GridPane offersGrid;
    @FXML
    private Button searchButton;
    @FXML
    private GridPane allSearchFields;
    @FXML
    private ComboBox<String> numPassengersCombo;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        addDateListeners();
        offerManager = new OfferManager();
        search = new SearchController();
        
        isOneWay.selectedProperty().bind(Bindings.createBooleanBinding(() -> {
            return secondDate.getValue() == null;

        }, secondDate.valueProperty()));
        isOneWay.getStyleClass().add("check_box");
        
        startAutoComplete();
        populateComboBox();
        updateAndShowOffers();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    public Stage getStage() {
        return stage;
    }
    
    public int getNumPassengers() {
        return Integer.valueOf(numPassengersCombo.getSelectionModel().getSelectedItem());
    }
    
    public boolean isOneWay() {
        return isOneWay.isSelected();
    }
    
    private void startAutoComplete() {
        HashSet<String> autoCompletes = new HashSet<String>(search.getAirports());
        departureField = (AutoCompleteTextField) departingFrom;
        arrivalField = (AutoCompleteTextField) arrivingTo;
        arrivalField.getEntries().addAll(autoCompletes);
        departureField.getEntries().addAll(autoCompletes);
    }

    /**
     * Adds a listener to the second date picker to make dates that are before
     * the date chosen in the first one unpickable, and vice versa.
     */
    private void addDateListeners() {
        //This prevents the user from choosing a return date that's sooner than
        //the outgoing departure date. 
        final Callback<DatePicker, DateCell> dayCellFactoryLatterPicker
                = new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        if (firstDate.getValue() != null && item.isBefore(
                                firstDate.getValue().plusDays(1))) {
                            setDisable(true);
                            setStyle("-fx-background-color: #eeeeee;");
                        }
                    }
                };
            }
        };

        final Callback<DatePicker, DateCell> dayCellFactoryPriorPicker
                = new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        if (secondDate.getValue() != null && item.isAfter(
                                secondDate.getValue().plusDays(-1))) {
                            setDisable(true);
                            setStyle("-fx-background-color: #eeeeee;");
                        }
                    }
                };
            }
        };
        secondDate.setDayCellFactory(dayCellFactoryLatterPicker);
        firstDate.setDayCellFactory(dayCellFactoryPriorPicker);

        //Sets the date for the second picker as 7 days later as the first one. 
//        firstDate.valueProperty().addListener((ov, oldValue, newValue) -> {
//            secondDate.setValue(newValue.plusDays(7));
//        });
    }

    /**
     * Changes the window to the booking site, and passes the info for the
     * flights found to the controller for the booking site.
     *
     * @param event Irrelevant
     */
    @FXML
    private void searchHandler(ActionEvent event) {
        
        Date firstDateD, secondDateD;

        firstDateD = parseDateFromLocalDate(firstDate.getValue());
        secondDateD = parseDateFromLocalDate(secondDate.getValue());

        SearchQuery sq = new SearchQuery(firstDateD, secondDateD, 
                departingFrom.getText(), arrivingTo.getText(), null, 
                Integer.valueOf(numPassengersCombo.getSelectionModel()
                        .getSelectedItem()));

        foundFlights = search.search(sq);

        Parent root;
        //This opens the booking page
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/BookingPage.fxml"));
            root = (Parent) loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            BookingPageController controller = (BookingPageController) loader.getController();
            controller.setSearchController(this);
            controller.passFlightData(allSearchFields, isOneWay.isSelected(), sq.getPassengerNo());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Flight> getFoundFlights() {
        return foundFlights;
    }

    private void populateComboBox() {
        ObservableList<String> arr = FXCollections.observableArrayList();
        for (int i = 1; i < 6; i++) {
            arr.add(String.valueOf(i));
        }
        numPassengersCombo.getItems().addAll(arr);
        numPassengersCombo.getSelectionModel().selectFirst();
    }

    private Date parseDateFromLocalDate(LocalDate local) {
        if (local == null) {
            return null;
        }
        LocalDateTime localDateTime = local.atStartOfDay();
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        Instant instant = Instant.from(zonedDateTime);
        return Date.from(instant);
    }

    public void updateAndShowOffers() {
        ArrayList<Offer> offers = offerManager.getOffers();
        ImageView currentView;
        Label text, price;
        Hyperlink header;
        StackPane sp;
        int rowIndex = 0;
        for (Offer offer : offers) {
            sp = new StackPane();
            currentView = new ImageView(offer.getImage());
            text = new Label(offer.getText());
            price = new Label("From " + KronutoluParser.parse(offer.getPrice()) + " kr");
            header = new Hyperlink(offer.getDestination());
            
            header.setOnAction(new EventHandler(){
                @Override
                public void handle(Event event) {
                    Hyperlink thisOne = (Hyperlink) event.getSource();
                    departingFrom.setText("Keflavík");
                    arrivingTo.setText(thisOne.getText());
                    firstDate.setValue(null);
                    secondDate.setValue(null);
                    searchButton.fire();
                    arrivalField.hidePopup();            
                }
            });
            
            text.setWrapText(true);
            header.setPrefWidth(100 + (offer.getDestination().length() * 42));
            text.setPrefWidth(100 + (offer.getDestination().length() * 42));
            StackPane.setAlignment(header, Pos.TOP_LEFT);
            StackPane.setAlignment(text, Pos.TOP_LEFT);
            StackPane.setAlignment(price, Pos.BOTTOM_RIGHT);
            text.getStyleClass().add("offer_text");
            header.getStyleClass().add("offer_header");
            price.getStyleClass().add("offer_price");
            sp.getChildren().addAll(currentView, text, header, price);

            offersGrid.getChildren().add(sp);
            GridPane.setRowIndex(sp, rowIndex);
            rowIndex++;
        }

        offersGrid.setPrefHeight(offers.size() * offers.get(0).getImage().getHeight());
        AnchorPane offerParent = (AnchorPane) offersGrid.getParent();
        offerParent.setPrefHeight(offers.size() * offers.get(0).getImage().getHeight());
    }
}
