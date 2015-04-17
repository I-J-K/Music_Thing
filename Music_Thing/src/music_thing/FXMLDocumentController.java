/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package music_thing;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
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
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

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
    
    private final MusicLibrary lib = MusicLibrary.load();
    
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
    private void importFromMenu(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open A Music File");
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("Supported Audio Files", "*.mp3"),
                new ExtensionFilter("All Files", "*.*")
        );
        File file = fileChooser.showOpenDialog(Music_Thing.getMainWindow());
        if(file!=null) importFile(file);
    }
    
    private void importFile(File file){
        File copyTo =  new File("music/"+file.getName());
        try{
            Files.copy(file.toPath(), copyTo.toPath());
            lib.addSong(new Track(SongType.MP3, file.getName()));
        }catch (Exception e){}
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
