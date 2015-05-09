/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package music_thing;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author joshuakaplan
 */
public class EditTrackController implements Initializable {
    @FXML
    private TextField editName;
    @FXML
    private TextField editArtist;
    @FXML
    private TextField editComposer;
    @FXML
    private TextField editGenre;
    @FXML
    private TextField editAlbum;
    @FXML
    private TextField editRating;
    
    private Track track;
    private Stage dialogStage;
    private boolean okClicked = false;

    /**
     * Sets the stage of this dialog.
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Returns true if the user clicked OK, false otherwise.
     * @return
     */
    public boolean isOkClicked() {
        return okClicked;
    }

    /**
     * Called when the user clicks ok.
     */
    @FXML
    private void handleOk() {
        if (isInputValid()) {
            track.setName(editName.getText());
            track.setArtist(editArtist.getText());
            track.setComposer(editComposer.getText());
            track.setAlbum(editAlbum.getText());
            track.setGenre(editGenre.getText());
            String rating = editRating.getText();
            if(rating!=null && !rating.equals("")){
                track.setRating(new Double(editRating.getText()));
            }else{
                track.setRating(0.0);
            }
            
            okClicked = true;
            dialogStage.close();
        }
    }

    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    public void setTrack(Track track){
        this.track=track;
        editName.setText(track.getName());
        editArtist.setText(track.getArtist());
        editComposer.setText(track.getComposer());
        editGenre.setText(track.getGenre());
        editAlbum.setText(track.getAlbum());
        editRating.setText(track.getRating().toString());
    }
    
    private boolean isInputValid() {
        try{
            if(editRating.getText()!=null && !editRating.getText().equals(""))Double.parseDouble(editRating.getText());
            return true;
        }catch(Exception e){
            System.out.println(e);
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText("Rating is not a number");
            return false;
        }
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
}
