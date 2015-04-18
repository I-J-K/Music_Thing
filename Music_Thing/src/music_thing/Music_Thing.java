/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package music_thing;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author csstudent
 */
public class Music_Thing extends Application {
    private static Stage mainWindow;
    
    @Override
    public void start(Stage stage) throws Exception {
        mainWindow = stage;
        Parent root = FXMLLoader.load(getClass().getResource("firstTest.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop(){
        MusicController.stop();
        MusicLibrary.save();
    }
    
    public static Stage getMainWindow() {
        return mainWindow;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
