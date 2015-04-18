/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package music_thing;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author joshuakaplan
 */
public class MusicLibrary implements java.io.Serializable{
    private static int track=0;
    
    private static ObservableList<Track> library = FXCollections.observableArrayList();
    
    private static ArrayList<Track> libraryList = new ArrayList(library.subList(0, library.size()));

    public static ObservableList<Track> getLibrary() {
        return library;
    }
    
    public static void setTrack(int index){
        track = index;
    }
    
    public static Track getSelectedTrack(){
        return library.get(track);
    }
    
    public static void addSong(Track track){
        library.add(track);
        save();
    }
    
    public static void removeTrack(Track track){
        library.remove(track);
        save();
    }
    
    public static void save(){
        try{
            libraryList = new ArrayList(library.subList(0, library.size()));
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
        }catch(IOException | ClassNotFoundException e){}
        library = FXCollections.observableList(libraryList);
    }
}