/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package music_thing;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;

/**
 *
 * @author csstudent
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private Button play;
    
    @FXML
    private TableView songList;
    
    @FXML
    private MenuItem close;
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
    }
    
    
    @FXML
    private void play(ActionEvent event) {
        if(!MusicController.getPlaying()){
            MusicController.play("file:///Volumes/JOSH/Music_Thing/Music_Thing/src/music_thing/amalgam.mp3");
        }else{
            MusicController.pause();
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
