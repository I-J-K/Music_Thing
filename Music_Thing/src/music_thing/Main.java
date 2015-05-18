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
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author csstudent
 */
public class Main extends Application {
    private static Stage mainWindow;
    private static FXMLDocumentController mainController;
    
    @Override
    public void start(Stage stage) throws Exception {
        mainWindow = stage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("mainLibrary.fxml"));
        Parent root = loader.load();
        //Parent root = FXMLLoader.load(getClass().getResource("mainLibrary.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        ((FXMLDocumentController)(loader.getController())).setMain(this);
        mainController=loader.getController();
        stage.setMinWidth(600);
        stage.setMinHeight(400);
        
    }

    @Override
    public void stop(){
        if(mainController.getPlayer()!=null)mainController.getPlayer().stop();
        MusicLibrary.save();
    }
    
    public static Stage getMainWindow() {
        return mainWindow;
    }
    
    public static FXMLDocumentController getController(){
        return mainController;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    /**
    * Opens a dialog to edit details for the specified person. If the user
    * clicks OK, the changes are saved into the provided person object and
    * true is returned.
    * 
    * @param track the track object to be edited
    * @return true if the user clicked OK, false otherwise.
    */
    public boolean showTrackEditDialog(Track track) {
      try {
        // Load the fxml file and create a new stage for the popup
        FXMLLoader loader = new FXMLLoader(getClass().getResource("editTrack.fxml"));
        Parent page = loader.load();
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Edit Track");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(mainWindow);
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);
        dialogStage.setResizable(false);

        // Set the person into the controller
        EditTrackController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setTrack(track);

        // Show the dialog and wait until the user closes it
        dialogStage.showAndWait();

        return controller.isOkClicked();

      } catch (Exception e) {
        // Exception gets thrown if the fxml file could not be loaded
        return false;
      }
    }
}
