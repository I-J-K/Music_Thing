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
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Polygon;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

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
    private Slider songVolumeBar;
    
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
    private TableColumn<Track,Integer> playcountCol;
    
    @FXML
    private TableColumn<Track,Integer> timeCol;
    
    @FXML
    private HBox pauseSymbol;
    
    @FXML
    private Polygon playSymbol;
    
    @FXML
    private void play(ActionEvent event) {
        if(MusicLibrary.size()>0){
            if(!MusicController.getPlaying()){
                MusicController.play(MusicLibrary.getSelectedTrack(songList), songVolumeBar.getValue());
                pauseSymbol.setVisible(true);
                playSymbol.setVisible(false);
            }else if(MusicController.getPlaying() && MusicLibrary.getSelectedTrack(songList)!=MusicController.getCurrentTrack()){
                MusicController.play(MusicLibrary.getSelectedTrack(songList), songVolumeBar.getValue());
                pauseSymbol.setVisible(true);
                playSymbol.setVisible(false);
            }else{
                MusicController.pause();
                pauseSymbol.setVisible(false);
                playSymbol.setVisible(true);
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
    private void changeVolume(MouseEvent event){
        MusicController.setVolume(songVolumeBar.getValue());
    }
    
    @FXML
    private void deleteFile(ActionEvent event){
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Delete?");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            Platform.runLater(() -> {
            try{
                Track toDelete = MusicLibrary.getSelectedTrack(songList);
                if(MusicController.getCurrentTrack()==toDelete){
                    MusicController.stop();
                }
                Files.delete(Paths.get("music/"+toDelete.getPath()));
                MusicLibrary.removeTrack(toDelete);
                MusicLibrary.setTrack(songList.getFocusModel().getFocusedCell().getRow());
            }catch(Exception e){}
            MusicLibrary.save();
            
        });
        } else {}
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
                    SwingUtilities.invokeLater(() -> {importFile(file);});
                }
            }
            event.setDropCompleted(success);
            event.consume();
            if(success){
                SwingUtilities.invokeLater(() -> {Platform.runLater(FXMLDocumentController::alertImportComplete);});
            }
    }
     
    @FXML
    private void importFromMenu(ActionEvent event){
        SwingUtilities.invokeLater(() -> {
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "Supported Audio Files", "mp3", "mid", "m4a", "wav", "aiff");
            chooser.setFileFilter(filter);
            chooser.setMultiSelectionEnabled(true);
            chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            int result = chooser.showOpenDialog(null);
            if(result == JFileChooser.APPROVE_OPTION) {
                File[] files = chooser.getSelectedFiles();
                if(files!=null){
                    for(File file: java.util.Arrays.asList(files)) importFile(file);
                    Platform.runLater(FXMLDocumentController::alertImportComplete);
                }
            }
        }); 
    }
    
    public static void alertImportComplete(){
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Finished Importing");
        alert.setHeaderText(null);
        alert.setContentText("Import Complete");
        alert.showAndWait();
    }
    
    private void importFile(File file){
        if(file.isFile()){
            File copyTo =  new File("music/"+file.getName());
            try{
                if(!copyTo.exists()){
                    if(file.getName().toLowerCase().endsWith("mp3")){
                        Files.copy(file.toPath(), copyTo.toPath());
                        MusicLibrary.addSong(new Track(SongType.MP3, file.getName()));
                    }else if(file.getName().toLowerCase().endsWith("mid")){
                        Files.copy(file.toPath(), copyTo.toPath());
                        MusicLibrary.addSong(new Track(SongType.MIDI, file.getName()));
                    }else if(file.getName().toLowerCase().endsWith("m4a")){
                        Files.copy(file.toPath(), copyTo.toPath());
                        MusicLibrary.addSong(new Track(SongType.AAC, file.getName()));
                    }else if(file.getName().toLowerCase().endsWith("aiff")){
                        Files.copy(file.toPath(), copyTo.toPath());
                        MusicLibrary.addSong(new Track(SongType.AIFF, file.getName()));
                    }else if(file.getName().toLowerCase().endsWith("wav")){
                        Files.copy(file.toPath(), copyTo.toPath());
                        MusicLibrary.addSong(new Track(SongType.WAV, file.getName()));
                    }//else{
                        //add alert file not supported
                    //}
                }
            }catch (Exception e){}
            
        }else if(file.isDirectory()){
            for(File thing : file.listFiles()) importFile(thing);
        }
        Platform.runLater(MusicLibrary::save);
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
        playcountCol.setCellValueFactory(
                new PropertyValueFactory("playCount"));
        timeCol.setCellValueFactory(
                new PropertyValueFactory("length"));
        songList.setItems(MusicLibrary.getLibrary());
    }
}
