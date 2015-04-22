/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package music_thing;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
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
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
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

    
    @FXML
    private void play(ActionEvent event) {
        if(MusicLibrary.size()>0){
            if(!MusicController.getPlaying()){
                MusicController.play(MusicLibrary.getSelectedTrack(songList));
            }else if(MusicController.getPlaying() && MusicLibrary.getSelectedTrack(songList)!=MusicController.getCurrentTrack()){
                MusicController.play(MusicLibrary.getSelectedTrack(songList));
            }else{
                MusicController.pause();
            }
            songList.getSelectionModel().select(MusicLibrary.getTrackNumber());
            songList.requestFocus();
        }
    }
    
    @FXML
    private void quit(ActionEvent event){
        Platform.exit();
    }
    
    @FXML
    private void deleteFile(ActionEvent event){
        try{
            Track toDelete = MusicLibrary.getSelectedTrack(songList);
            if(MusicController.getCurrentTrack()==toDelete){
                MusicController.stop();
            }
            Files.delete(Paths.get("music/"+toDelete.getPath()));
            MusicLibrary.removeTrack(toDelete);
            MusicLibrary.setTrack(songList.getFocusModel().getFocusedCell().getRow());
        }catch(Exception e){}
    }
    
    @FXML
    private void fileDragged(DragEvent event) {
        Dragboard db = event.getDragboard();
        if (db.hasFiles()) {
            event.acceptTransferModes(TransferMode.COPY);
        } else {
            event.consume();
        }
    }
    
    @FXML
    private void importFromDrag(DragEvent event){
        Dragboard db = event.getDragboard();
        boolean success = false;
        if (db.hasFiles()) {
            success = true;
            for (File file : db.getFiles()) {
                importFile(file);
            }
        }
        event.setDropCompleted(success);
        event.consume();
    }
    
    @FXML
    private void importFromMenu(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open A Music File");
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("Supported Audio Files", "*.mp3", "*.mid", "*.m4a", "*.wav", "*.aiff"),
                new ExtensionFilter("All Files", "*.*")
        );
        List<File> files = fileChooser.showOpenMultipleDialog(Music_Thing.getMainWindow());
        if(files!=null){
            for(File file: files) importFile(file);
        }
    }
    
    private void importFile(File file){
        File copyTo =  new File("music/"+file.getName());
        boolean copy = true;
        try{
            if(file.getName().toLowerCase().endsWith("mp3")){
                MusicLibrary.addSong(new Track(SongType.MP3, file.getName()));
            }else if(file.getName().toLowerCase().endsWith("mid")){
                MusicLibrary.addSong(new Track(SongType.MIDI, file.getName()));
            }else if(file.getName().toLowerCase().endsWith("m4a")){
                MusicLibrary.addSong(new Track(SongType.AAC, file.getName()));
            }else if(file.getName().toLowerCase().endsWith("aiff")){
                MusicLibrary.addSong(new Track(SongType.AIFF, file.getName()));
            }else if(file.getName().toLowerCase().endsWith("wav")){
                MusicLibrary.addSong(new Track(SongType.WAV, file.getName()));
            }else{
                copy=false;
                //add alert file not supported
            }
            if(copy && !copyTo.exists())Files.copy(file.toPath(), copyTo.toPath());
        }catch (Exception e){}
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        MusicLibrary.load();
        File music = new File("music");
        if(!music.exists())music.mkdir();
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
        
        songList.setItems(MusicLibrary.getLibrary());
    }    
    
}
