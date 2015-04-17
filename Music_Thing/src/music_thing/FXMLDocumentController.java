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
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.InputEvent;

/**
 *
 * @author csstudent
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private Button play;
    
    @FXML
    private TableView<Track> songList;
    
    @FXML
    private MenuItem fileImport;
    
    @FXML
    private MenuItem quit;
    
    @FXML
    private Label songTime;
    
    @FXML
    private Slider songTimeBar;
    
    @FXML
    private TableColumn<Track,String> songCol;
    
    @FXML
    private TableColumn<Track,String> artistCol;
    
    @FXML
    private TableColumn<Track,String> albumCol;
    
    @FXML
    private TableColumn<Track,String> genreCol;
    
    @FXML
    private TableColumn<Track,Double> ratingCol;
    
    private final MusicLibrary lib= new MusicLibrary();
    
    @FXML
    private void play(ActionEvent event) {
        if(!MusicController.getPlaying()){
            MusicController.play(lib.getSelectedTrack());
        }else if(MusicController.getPlaying() && lib.getSelectedTrack()!=MusicController.getCurrentTrack()){
            MusicController.play(lib.getSelectedTrack());
        }else{
            MusicController.pause();
        }
    }
    
    @FXML
    private void getFocusedTrack(InputEvent event) {
        lib.setTrack(songList.getFocusModel().getFocusedCell().getRow());
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        songCol.setCellValueFactory(
                new PropertyValueFactory("name"));
        artistCol.setCellValueFactory(
                new PropertyValueFactory("artist"));
        albumCol.setCellValueFactory(
                new PropertyValueFactory("album"));
        genreCol.setCellValueFactory(
                new PropertyValueFactory("genre"));
        ratingCol.setCellValueFactory(
                new PropertyValueFactory("rating"));
        
        songList.setItems(lib.getLibrary());
    }    
    
}
