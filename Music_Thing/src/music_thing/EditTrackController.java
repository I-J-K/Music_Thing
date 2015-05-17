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
    private Rating editRating;
    
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
        track.setName(editName.getText());
        track.setArtist(editArtist.getText());
        track.setComposer(editComposer.getText());
        track.setAlbum(editAlbum.getText());
        track.setGenre(editGenre.getText());
        track.setRating(new Double(editRating.getRating()));
        track.saveTags();
        okClicked = true;
        dialogStage.close();
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
    }

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
}
