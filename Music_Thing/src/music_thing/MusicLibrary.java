/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package music_thing;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.function.Predicate;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/**
 *
 * @author joshuakaplan
 */
public class MusicLibrary implements java.io.Serializable{
    private static int track=0;
    
    private static ObservableList<Track> data = FXCollections.observableArrayList();
    private static FilteredList<Track> filteredData = new FilteredList<>(data, p -> true);
    private static SortedList<Track> library;
    private static TextField filterField;
    public static void setFilterField(TextField t){
        filterField = t;
    }
    public static void setUpFilter(){
        filterField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            filteredData.setPredicate((Track track) -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();
                if (track.getName()!=null && track.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches name.
                } else if (track.getArtist()!=null && track.getArtist().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches artist.
                } else if (track.getAlbum()!=null && track.getAlbum().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches album.
                } else if (track.getGenre()!=null && track.getGenre().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false; // Does not match.
            });
        });
        library = new SortedList<>(filteredData);
    }

    private static ArrayList<Track> libraryList;
    
    private static ArrayList<Track> queue = new ArrayList<Track>();
    
    private static boolean queueIsEmpty = true;
    
    private static int positionInQueue = -1;
    
    //This is for shuffle only.
    private static int lastPlayed;

    public static SortedList<Track> getLibrary() {
        return library;
    }

    public static void clearQueue() {
        queue.clear();
        positionInQueue = -1;
        queueIsEmpty = true;
    }
    
    public static int getTrackNumber(){
        return track;
    }
    
    public static void setTrack(int index){
        track = index;
    }
    
    public static Track getSelectedTrack(TableView songList){
        setTrack(songList.getFocusModel().getFocusedCell().getRow());
        return library.get(track);
    }
    
    public static void addSong(Track track){
        //library.add(track);
        data.add(track);
        filteredData = new FilteredList<>(data, p -> true);
        setUpFilter();
    }
    
    public static void removeTrack(Track track){
        //library.remove(track);
        data.remove(track);
        filteredData = new FilteredList<>(data, p -> true);
        setUpFilter();
    }
    
    public static int size(){
        return library.size();
    }
    
    public static void save(){
        try{
            libraryList = new ArrayList(data);
            FileOutputStream fileOut = new FileOutputStream("library.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(libraryList);
            out.close();
            fileOut.close();
            PrefController.save();
        }catch(Exception e){}
    }
    
    public static void load(){
        try{
            FileInputStream fileIn = new FileInputStream("library.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            libraryList = (ArrayList<Track>) in.readObject();
            in.close();
            fileIn.close();
            data = FXCollections.observableList(libraryList);
            filteredData = new FilteredList<>(data, p -> true);
            library = new SortedList<>(filteredData);
            PrefController.load();
        }catch(Exception e){}
    }
    
    public static void addToQueue(Track toAdd){
        if(queueIsEmpty)
            queueIsEmpty = false;
        queue.add(toAdd);
    }
    
    public static void addToTopOfQueue(Track toAdd){
        if(queueIsEmpty)
            queueIsEmpty = false;
        queue.add(positionInQueue+1, toAdd);
    }
    
    public static boolean isQueueEmpty(){
        return queueIsEmpty;
    }
    
    public static int getNextQueueItem(){
        if (positionInQueue >= queue.size()-1){
            return library.indexOf(queue.get(positionInQueue));
        }
        return library.indexOf(queue.get(positionInQueue + 1));
    }
    
    public static int getPrevQueueItem(){
        if(positionInQueue > 0){
            return library.indexOf(queue.get(positionInQueue - 1));
        }
        return library.indexOf(queue.get(positionInQueue));
    }
   
    public static void updateQueue(){
        positionInQueue++;
    }
    
    public static boolean atEndOfQueue(){
        if(positionInQueue == queue.size()-1)return true;
        return false;
    }
}
