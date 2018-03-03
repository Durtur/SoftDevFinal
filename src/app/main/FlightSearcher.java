/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import controllers.SearchpageController;

/**
 *
 * @author Símon Örn Reynisson <sor7@hi.is>
 */
public class FlightSearcher extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Searchpage.fxml"));
        Parent root = (Parent)loader.load();
                
                
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
        SearchpageController controller = (SearchpageController)loader.getController();
        controller.setStage(stage);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
