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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
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
import javafx.scene.control.SelectionMode;
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
    private TableColumn<Track,TimeFormat> timeCol;
    @FXML
    private HBox pauseSymbol;
    @FXML
    private Polygon playSymbol;
    
    private Main main;
    
    private MusicPlayer player;
    private final JavafxPlayer jfxPlayer = new JavafxPlayer();
    private final MidiPlayer midiPlayer = new MidiPlayer();
    private final ClipPlayer clipPlayer = new ClipPlayer();
    
    @FXML
    private void clickedTable(MouseEvent event){
        if (event.isPrimaryButtonDown() && event.getClickCount() > 1) {
            play(event);
        }
    }
    
    @FXML
    private void stopMusic(MouseEvent event) {
        if(player!=null)player.reset();
        pauseSymbol.setVisible(false);
        playSymbol.setVisible(true);
        songList.requestFocus();
    }
    
    @FXML
    private void nextSong(MouseEvent event){
        if(MusicLibrary.getTrackNumber()<MusicLibrary.size()-1){
            songList.getSelectionModel().clearAndSelect(MusicLibrary.getTrackNumber()+1);
            MusicLibrary.setTrack(MusicLibrary.getTrackNumber()+1);
            if(player!=null && player.getPlaying()==true)play(event);
        }else{
            stopMusic(event);
        }
        songList.requestFocus();
    }
    
    @FXML
    private void prevSong(MouseEvent event){
        if(MusicLibrary.getTrackNumber()>0){
            songList.getSelectionModel().clearAndSelect(MusicLibrary.getTrackNumber()-1);
            MusicLibrary.setTrack(MusicLibrary.getTrackNumber()-1);
            if(player!=null && player.getPlaying()==true)play(event);
        }else{
            stopMusic(event);
        }
        songList.requestFocus();
    }
    
    @FXML
    private void play(MouseEvent event) {
        if(MusicLibrary.size()>0){
            Track selectedTrack = MusicLibrary.getSelectedTrack(songList);
            if(player!=null && selectedTrack!=player.getCurrentTrack() || player==null){
                SongType type = selectedTrack.getType();
                if(player!=null)player.stop();
                if(type==SongType.MP3 || type==SongType.AAC || type==SongType.WAV || type==SongType.AIFF){
                    player = jfxPlayer;
                }else if(type==SongType.MIDI){
                    player = midiPlayer;
                }else if(type==SongType.FLAC){
                    player = clipPlayer;
                }
            }
            if(!player.getPlaying()){
                player.play(selectedTrack, songVolumeBar.getValue());
                pauseSymbol.setVisible(true);
                playSymbol.setVisible(false);
            }else if(player.getPlaying() && MusicLibrary.getSelectedTrack(songList)!=player.getCurrentTrack()){
                player.play(selectedTrack, songVolumeBar.getValue());
                pauseSymbol.setVisible(true);
                playSymbol.setVisible(false);
            }else{
                player.pause();
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
        if(player!=null)player.setVolume(songVolumeBar.getValue());
    }
    
    @FXML
    private void deleteFile(ActionEvent event){
        Platform.runLater(() -> {
            try{
                HashSet<Track> toDelete = new HashSet(songList.getSelectionModel().getSelectedItems());
                if(toDelete.size()>0){
                    Alert alert = new Alert(AlertType.CONFIRMATION);
                    alert.setTitle("Delete?");
                    alert.setHeaderText(null);
                    alert.setContentText("Are you sure you want to delete "+toDelete.size()+" tracks?");
                    if(toDelete.size()==1)alert.setContentText("Are you sure you want to delete 1 track?");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK){
                        if(player!=null && toDelete.contains(player.getCurrentTrack())){
                            player.stop();
                        }
                        for(Track track: toDelete){
                            Files.delete(Paths.get("music/"+track.getPath()));
                            MusicLibrary.removeTrack(track);
                        }
                        MusicLibrary.setTrack(songList.getFocusModel().getFocusedCell().getRow());
                    }
                }
            }catch(Exception e){}
            MusicLibrary.save();
        });
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
                    //SwingUtilities.invokeLater(() -> {importFile(file);});
                     Platform.runLater(() -> {importFile(file);});
                }
            }
            event.setDropCompleted(success);
            event.consume();
            if(success){
                //SwingUtilities.invokeLater(() -> {
                    Platform.runLater(songList::sort);
                    Platform.runLater(MusicLibrary::save);
                    Platform.runLater(FXMLDocumentController::alertImportComplete);
                //});
            }
    }
     
    @FXML
    private void importFromMenu(ActionEvent event){
        List<File> files = new ArrayList<File>();
        SwingUtilities.invokeLater(() -> {
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "Supported Audio Files", "mp3", "mid", "m4a", "wav", "aiff", "flac");
            chooser.setFileFilter(filter);
            chooser.setMultiSelectionEnabled(true);
            chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            int result = chooser.showOpenDialog(null);
            if(result == JFileChooser.APPROVE_OPTION) {
                //File[] files = chooser.getSelectedFiles();
                files.addAll(Arrays.asList(chooser.getSelectedFiles()));
            }
            Platform.runLater(() ->{
                for(File file: files) importFile(file);
                songList.sort();
                MusicLibrary.save();
                alertImportComplete();
            });
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
                    }else if(file.getName().toLowerCase().endsWith("flac")){
                        Files.copy(file.toPath(), copyTo.toPath());
                        MusicLibrary.addSong(new Track(SongType.FLAC, file.getName()));
                    }
                }
            }catch (Exception e){}
            
        }else if(file.isDirectory()){
            for(File thing : file.listFiles()) importFile(thing);
        }
        
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
        songList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }
    
    /**
     * Called when the user clicks the edit button.
     * Opens a dialog to edit details for the selected track.
     */
    @FXML
    private void handleEditTrack() {
        Track track = MusicLibrary.getSelectedTrack(songList);
        if (track != null) {
          boolean okClicked = main.showTrackEditDialog(track);
          if (okClicked) {
            refresh();
          }

        } else {
          // Nothing selected.
          Alert alert = new Alert(AlertType.WARNING);
          alert.initOwner(Main.getMainWindow());
          alert.setTitle("No Selection");
          alert.setHeaderText("No Track Selected");
          alert.setContentText("Please select a track in the table.");

          alert.showAndWait();
        }
    }

    /**
     * Refreshes the table. This is only necessary if an item that is already in
     * the table is changed. New and deleted items are refreshed automatically.
     * 
     * This is a workaround because otherwise we would need to use property
     * bindings in the model class and add a *property() method for each
     * property. Maybe this will not be necessary in future versions of JavaFX
     */
    private void refresh() {
      int selectedIndex = songList.getSelectionModel().getSelectedIndex();
      //selectedIndex = MusicLibrary.getTrackNumber();
      songList.setItems(null);
      songList.layout();
      songList.setItems(MusicLibrary.getLibrary());
      // Must set the selected index again (see http://javafx-jira.kenai.com/browse/RT-26291)
      songList.getSelectionModel().select(selectedIndex);
    }
    
    public void setMain(Main main){
        this.main = main;
    }
}
