/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package music_thing;

import com.sun.javafx.scene.control.skin.TableViewSkinBase;
import java.awt.Component;
import java.awt.HeadlessException;
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
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Slider;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.*;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import np.com.ngopal.control.*;

/**
 *
 * @author csstudent
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private Button play;
    @FXML
    private Button autoRepeat;
    @FXML
    private Button shuffleButton;
    @FXML
    private TableView<Track> songList;
    @FXML
    private MenuItem fileImport;
    @FXML
    private MenuItem quit;
    @FXML
    private Label songTime;
    @FXML
    private Label defaultTimeLabel;
    @FXML
    private Slider songVolumeBar;
    @FXML
    private TableColumn<Track, String> songCol;
    @FXML
    private TableColumn<Track, String> artistCol;
    @FXML
    private TableColumn<Track, String> albumCol;
    @FXML
    private TableColumn<Track, String> genreCol;
    @FXML
    private TableColumn<Track, Double> ratingCol;
    @FXML
    private TableColumn<Track, Integer> playcountCol;
    @FXML
    private TableColumn<Track, Integer> timeCol;
    @FXML
    private HBox pauseSymbol;
    @FXML
    private Polygon playSymbol;
    @FXML
    private MenuItem menuPlay;
    @FXML
    private Slider timeBar;
    @FXML
    private Label currentTimeLabel;
    @FXML
    private MenuItem playContextMenu;
    @FXML
    private CheckMenuItem repeatMenu;
    @FXML
    private CheckMenuItem shuffleMenu;
    @FXML
    private AutoFillTextBox searchField;
    
    private Timeline timer;
    private boolean wasPlaying;
    private Main main;
    private boolean autoRepeatOn = false;
    private boolean shuffleOn = false;
    
    private MusicPlayer player;
    private final JavafxPlayer jfxPlayer = new JavafxPlayer();
    private final MidiPlayer midiPlayer = new MidiPlayer();
    private final ClipPlayer clipPlayer = new ClipPlayer();
    
    public MusicPlayer getPlayer(){
        return player;
    }

    public TableView<Track> getSongList(){
        return songList;
    }
    
    @FXML
    private void onSliderPressed(MouseEvent event){
        if(player!=null && timeBar!=null){
            if(player.getPlaying()){
                wasPlaying=true;
            }
            player.pause();
            timer.stop();
        }
    }
    
    @FXML
    private void onSliderDragged(MouseEvent event){
        if(player!=null && timeBar!=null){
            player.seek((int) timeBar.getValue());
            player.setCurrentTime((int)timeBar.getValue());
            currentTimeLabel.setText(new TimeFormat(((Integer)player.getCurrentTime())).toString());
        }
    }
    
    @FXML
    private void onSliderReleased(MouseEvent event){
        if(player!=null && timeBar!=null){
            if(wasPlaying){
                player.seek((int) timeBar.getValue());
                player.setPlaying(false);
                player.setCurrentTime((int)(timeBar.getValue()));
                currentTimeLabel.setText(new TimeFormat(((Integer)player.getCurrentTime())).toString());
                play(event);
                wasPlaying=false;
            }
        }
    }    
    @FXML
    private void clickedTable(MouseEvent event){
        if (event.isPrimaryButtonDown() && event.getClickCount() > 1) {
            play(event);
        }
    }
    
    @FXML
    private void stopMusic(Event event) {
        if(player!=null)player.reset();
        if(timer!=null)timer.stop();
        pauseSymbol.setVisible(false);
        playSymbol.setVisible(true);
        menuPlay.setText("Play");
        player.setCurrentTime((int)(player.getSongTime()));
        currentTimeLabel.setText(new TimeFormat(((Integer)player.getCurrentTime())).toString());
    }
    
    @FXML
    private void nextSong(Event event){
        if(MusicLibrary.size()>0){
            if(MusicLibrary.getTrackNumber()<MusicLibrary.size()-1){
                if(!autoRepeatOn){
                    //if(MusicLibrary.isQueueEmpty()){
                        if(!shuffleOn){
                            MusicLibrary.setTrack(MusicLibrary.getTrackNumber()+1);
                            songList.getSelectionModel().clearAndSelect(MusicLibrary.getTrackNumber());
                            if(player!=null && player.getPlaying()==true)play(event);
                        }else{
                            MusicLibrary.setTrack((int)(Math.random()*MusicLibrary.size()));
                            songList.getSelectionModel().clearAndSelect(MusicLibrary.getTrackNumber());
                            if(player!=null && player.getPlaying()==true)play(event);
                        }
                    //}else{
                        
                    //}
                }else{
                    songList.getSelectionModel().clearAndSelect(MusicLibrary.getTrackNumber());
                    MusicLibrary.setTrack(MusicLibrary.getTrackNumber());
                    if(player!=null && player.getPlaying()==true){
                        stopMusic(event);
                        play(event);
                    }
                }
            }else{
                stopMusic(event);
            }
            songList.requestFocus();
        }
    }
    
    @FXML
    private void prevSong(Event event){
        if(MusicLibrary.size()>0){
            if(MusicLibrary.getTrackNumber()>0){
                MusicLibrary.setTrack(MusicLibrary.getTrackNumber()-1);
                songList.getSelectionModel().clearAndSelect(MusicLibrary.getTrackNumber());
                if(player!=null && player.getPlaying()==true)play(event);
            }else{
                stopMusic(event);
            }
            songList.requestFocus();
            //songList.scrollTo(player.getCurrentTrack());
        }
    }
    
    @FXML
    private void play(Event event) {
        if(MusicLibrary.size()>0){
            if(timer!=null)timer.stop();
            Track selectedTrack = MusicLibrary.getSelectedTrack(songList);
            if(player!=null && selectedTrack!=player.getCurrentTrack() || player==null){
                SongType type = selectedTrack.getType();
                if(player!=null)player.stop();
                if(type==SongType.MP3 || type==SongType.M4A || type==SongType.WAV  || type==SongType.AIFF || type==SongType.MP4){
                    player = jfxPlayer;
                }else if(type==SongType.MIDI){
                    player = midiPlayer;
                }else if(type==SongType.FLAC || type==SongType.AU || type==SongType.OGG || type==SongType.AAC){
                    player = clipPlayer;
                }
            }
            
            if(!player.getPlaying() || (player.getPlaying() && MusicLibrary.getSelectedTrack(songList)!=player.getCurrentTrack())){
                player.play(selectedTrack, songVolumeBar.getValue());
                pauseSymbol.setVisible(true);
                playSymbol.setVisible(false);
                menuPlay.setText("Pause");
                player.setCurrentTime((int)(player.getSongTime()));
                currentTimeLabel.setText(new TimeFormat(player.getCurrentTime()).toString());
                currentTimeLabel.setVisible(true);
                int length = selectedTrack.getLength();
                if(length==0){
                    length=player.getSongLength();
                    selectedTrack.setLength(length);//setLength(length);
                    refresh();
                }
                songTime.setText(new TimeFormat(length).toString());
                songTime.setVisible(true);
                timeBar.setMax(length);
                timeBar.setValue(player.getSongTime());
                timer.play();
            }else{
                player.pause();
                pauseSymbol.setVisible(false);
                playSymbol.setVisible(true);
                menuPlay.setText("Play");
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
    private void changeVolume(Event event){
        if(player!=null)player.setVolume(songVolumeBar.getValue());
    }
    
    @FXML
    private void deleteFile(Event event){
        try{
            HashSet<Track> toDelete = new HashSet(songList.getSelectionModel().getSelectedItems());
            if(toDelete.size()>0){
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Delete?");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to delete "+toDelete.size()+" tracks?");
                if(toDelete.size()==1)alert.setContentText("Are you sure you want to delete 1 track?");
                ((Stage) alert.getDialogPane().getScene().getWindow()).setAlwaysOnTop(true);
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    if(player!=null && toDelete.contains(player.getCurrentTrack())){
                        player.stop();
                        if(timer!=null)timer.stop();
                    }
                    for(Track track: toDelete){
                        MusicLibrary.removeTrack(track);
                        Files.delete(Paths.get("music/"+track.getPath()));
                    }
                    MusicLibrary.setTrack(songList.getFocusModel().getFocusedCell().getRow());
                }
            }
        }catch(Exception e){}
        MusicLibrary.save();
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
        event.setDropCompleted(false);
        if (event.getDragboard().hasFiles()) new Thread(new dragTask(event.getDragboard().getFiles(), event)).start();
        event.consume();
    }
     
    @FXML
    private void importFromMenu(ActionEvent event){
        new Thread(new Task<Void>(){
            @Override protected Void call() throws Exception{
                List<File> files = new ArrayList<File>();
                JFileChooser chooser = new JFileChooser(){
                    @Override
                    protected JDialog createDialog(Component parent) throws HeadlessException {
                        // intercept the dialog created by JFileChooser
                        JDialog dialog = super.createDialog(parent);
                        dialog.setAlwaysOnTop(true);  // set modality (or setModalityType)
                        return dialog;
                    }
                };
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "Supported Audio Files", "mp3", "mid", "m4a", "wav", "aiff", "flac");
                chooser.setFileFilter(filter);
                chooser.setMultiSelectionEnabled(true);
                chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    files.addAll(Arrays.asList(chooser.getSelectedFiles()));
                    boolean imported = false;
                    for(File file: files){
                        //importFile(file);
                        imported = importFile(file) || imported;
                    }
                    songList.sort();
                    MusicLibrary.save();
                    if(imported){
                        Platform.runLater(() -> {alertImportComplete(true);});
                    }else{
                        Platform.runLater(() -> {alertImportComplete(false);});
                    }
                }
                return null;
            }
        }).start();
        
    }
    
    @FXML
    private void rate0(ActionEvent event){
        MusicLibrary.getSelectedTrack(songList).setRating(0.0);
        refresh();
    }
    
    @FXML
    private void rate1(ActionEvent event){
        MusicLibrary.getSelectedTrack(songList).setRating(1.0);
        refresh();
    }
    
    @FXML
    private void rate2(ActionEvent event){
        MusicLibrary.getSelectedTrack(songList).setRating(2.0);
        refresh();
    }
    
    @FXML
    private void rate3(ActionEvent event){
        MusicLibrary.getSelectedTrack(songList).setRating(3.0);
        refresh();
    }
    
    @FXML
    private void rate4(ActionEvent event){
        MusicLibrary.getSelectedTrack(songList).setRating(4.0);
        refresh();
    }
    
    @FXML
    private void rate5(ActionEvent event){
        MusicLibrary.getSelectedTrack(songList).setRating(5.0);
        refresh();
    }
    
    public static void alertImportComplete(boolean imported){
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Finished Importing");
        alert.setHeaderText(null);
        if(imported){
            alert.setContentText("Import Complete");
        }else{
            alert.setContentText("Nothing Imported");
        }
        ((Stage) alert.getDialogPane().getScene().getWindow()).setAlwaysOnTop(true);
        alert.showAndWait();
    }
    
    public boolean importFile(File file){
        boolean imported = false;
        if(file.isFile()){
            File copyTo =  new File("music/"+file.getName());
            SongType type = null;
            String name = file.getName().toLowerCase();
            try{
                if(name.endsWith("mp3")){
                    type=SongType.MP3;
                }else if(name.endsWith("mid")){
                    type=SongType.MIDI;
                }else if(name.endsWith("m4a")){
                    type=SongType.M4A;
                }else if(name.endsWith("aac")){
                    type=SongType.AAC;
                }else if(name.endsWith("aiff")){
                    type=SongType.AIFF;
                }else if(name.endsWith("wav")){
                    type=SongType.WAV;
                }else if(name.endsWith("flac")){
                    type=SongType.FLAC;
                }else if(name.endsWith("au")){
                    type=SongType.AU;
                }else if(name.endsWith("ogg")){
                    type=SongType.OGG;
                }else if(name.endsWith("mp4")){
                    type=SongType.MP4;
                }
                if(type!=null){
                    if(!copyTo.exists())Files.copy(file.toPath(), copyTo.toPath());
                    Track newTrack = new Track(type, file.getName());
                    if(!MusicLibrary.getLibrary().contains(newTrack))MusicLibrary.addSong(newTrack);
                    imported = true;
                }
            }catch (Exception e){}
            
        }else if(file.isDirectory()){
            for(File thing : file.listFiles()){
                imported = importFile(thing) || imported;
            }
        }
        return imported;
    }
    
    /**
     * Called when the user clicks the edit button.
     * Opens a dialog to edit details for the selected track.
     */
    @FXML
    private void handleEditTrack() {
        if(MusicLibrary.size()>0){
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
              ((Stage) alert.getDialogPane().getScene().getWindow()).setAlwaysOnTop(true);
              alert.showAndWait();
            }
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
      songList.getColumns().get(0).setVisible(false);
      songList.getColumns().get(0).setVisible(true);
      songList.getSelectionModel().select(selectedIndex);
    }
    
    public void setMain(Main main){
        this.main = main;
    }
    
    @FXML
    public void toggleFulllscreen(ActionEvent event){
        Main.getMainWindow().setFullScreen(!Main.getMainWindow().isFullScreen());
    }
    
    @FXML
    private void autoRepeat(ActionEvent event){
        autoRepeatOn = !autoRepeatOn;
        if(autoRepeatOn){
            autoRepeat.setEffect(new Lighting());
            shuffleMenu.setSelected(false);
            shuffleOn = false;
            shuffleButton.setEffect(null);
            
        }else{
            autoRepeat.setEffect(null);
        }
        repeatMenu.setSelected(autoRepeatOn);
        songList.getSelectionModel().select(MusicLibrary.getTrackNumber());
        songList.requestFocus();
    }
    
    @FXML
    private void shuffle(ActionEvent event){
        shuffleOn = !shuffleOn;
        if(shuffleOn){
            repeatMenu.setSelected(false);
            autoRepeatOn = false;
            autoRepeat.setEffect(null);
            shuffleButton.setEffect(new Lighting());
        }else{
            shuffleButton.setEffect(null);
        }
        shuffleMenu.setSelected(shuffleOn);
        songList.getSelectionModel().select(MusicLibrary.getTrackNumber());
        songList.requestFocus();
    }
    
    private void tick(){
        player.setCurrentTime((int)(player.getSongTime()));
        if(player.getCurrentTime()>=player.getSongLength()){
            new Timeline(new KeyFrame(Duration.millis(1000), ae -> nextSong(null))).play();
            timer.stop();
            player.getCurrentTrack().setPlayCount(player.getCurrentTrack().getPlayCount()+1);
            refresh();
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        MusicLibrary.load();
        MusicLibrary.populateSearch();
        File music = new File("music");
        if(!music.exists())music.mkdir();
        File artwork = new File("artwork");
        if(!artwork.exists())artwork.mkdir();
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
        ratingCol.setCellFactory(new Callback<TableColumn<Track,Double>,TableCell<Track,Double>>() {
            @Override
            public TableCell<Track, Double> call(TableColumn<Track,Double> param){
                TableCell<Track, Double> cell = new TableCell<Track, Double>(){
                    @Override
                    public void updateItem(Double d, boolean empty){
                        if(d != null){
                            Rating rating = new Rating();
                            rating.setRating(d.intValue());
                            rating.getRatingProperty().addListener(new ChangeListener(){
                                @Override
                                public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                                    MusicLibrary.getLibrary().get(getIndex()).setRating(((Integer)newValue).doubleValue());
                                }
                            });
                            setGraphic(rating);
                            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                        }else{
                            setGraphic(null);
                        }
                    }
                };

                return cell;
            }
        });
        timeCol.setCellFactory(new Callback<TableColumn<Track,Integer>,TableCell<Track,Integer>>() {
            @Override
            public TableCell<Track, Integer> call(TableColumn<Track,Integer> param){
                TableCell<Track, Integer> cell = new TableCell<Track, Integer>(){
                    @Override
                    public void updateItem(Integer d, boolean empty){
                        if(d != null){
                            setText(new TimeFormat(d).toString());
                        }else{
                            setText(null);
                        }
                    }
                };

                return cell;
            }
        });
        MusicPlayer.getTimeProperty().addListener(new ChangeListener(){
                @Override
                public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                    timeBar.setValue(((Integer)newValue).doubleValue());
                    currentTimeLabel.setText(new TimeFormat(((Integer)newValue)).toString());
                }
        });
        MusicLibrary.getLibrary().addListener(new ListChangeListener(){
            @Override
            public void onChanged(ListChangeListener.Change c) {
                MusicLibrary.populateSearch();
            }
        });
        timer = new Timeline(new KeyFrame(
                    Duration.millis(100),
                    ae -> tick()));
        timer.setCycleCount(Animation.INDEFINITE);
        timeBar.setMin(0);
        songList.setItems(MusicLibrary.getLibrary());
        songList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        searchField.setData(FXCollections.observableArrayList(MusicLibrary.getSearcher().keySet()));
    }
}
