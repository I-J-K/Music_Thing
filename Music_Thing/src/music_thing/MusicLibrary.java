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
import java.util.TreeMap;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.control.TableView;

/**
 *
 * @author joshuakaplan
 */
public class MusicLibrary implements java.io.Serializable{
    private static int track=0;
    
    private static ObservableList<Track> library = FXCollections.observableArrayList();
    private static ObservableMap<String, Track> searcher = FXCollections.observableMap(new TreeMap<String,Track>());
    private static ArrayList<Track> libraryList;
    
    private static ArrayList<Track> queue;
    
    private static boolean queueIsEmpty;
    
    private static Track lastPlayed;

    public static ObservableList<Track> getLibrary() {
        return library;
    }
    
    public static void populateSearch(){
        searcher.clear();
        for(Track t : library){
            searcher.put(t.getName(), t);
        }
    }
    
    public static ObservableMap<String,Track> getSearcher(){
        return searcher;
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
        library.add(track);
    }
    
    public static void removeTrack(Track track){
        library.remove(track);
    }
    
    public static int size(){
        return library.size();
    }
    
    public static void save(){
        try{
            libraryList = new ArrayList(library);
            FileOutputStream fileOut = new FileOutputStream("library.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(libraryList);
            out.close();
            fileOut.close();
        }catch(Exception e){}
    }
    
    public static void load(){
        try{
            FileInputStream fileIn = new FileInputStream("library.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            libraryList = (ArrayList<Track>) in.readObject();
            in.close();
            fileIn.close();
            library = FXCollections.observableList(libraryList);
        }catch(Exception e){}
    }
    
    public static void addToQueue(Track toAdd, Integer position){
        if(queueIsEmpty)
            queueIsEmpty = false;
        queue.add(position, toAdd);
    }
    
    public static boolean isQueueEmpty(){
        return queueIsEmpty;
    }
    
    public static void updateQueue(){
        lastPlayed = queue.get(0);
        queue.remove(0);
    }
    
    public static Track getTopOfQueue(){
        return queue.get(0);
    }
}
