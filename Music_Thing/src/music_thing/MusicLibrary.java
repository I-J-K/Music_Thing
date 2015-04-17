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
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author joshuakaplan
 */
public class MusicLibrary implements java.io.Serializable{
    private int track=0;
    
    private transient ObservableList<Track> library = FXCollections.observableArrayList();
    
    private ArrayList<Track> libraryList = new ArrayList(library.subList(0, library.size()));
    
    public MusicLibrary() {
    }

    public ObservableList<Track> getLibrary() {
        return library;
    }
    
    public void setTrack(int index){
        track = index;
    }
    
    public Track getSelectedTrack(){
        return library.get(track);
    }
    
    public void addSong(Track track){
        library.add(track);
        save();
    }
    
    public void removeTrack(Track track){
        library.remove(track);
        save();
    }
    
    public void save(){
        try{
            libraryList = new ArrayList(library.subList(0, library.size()));
            FileOutputStream fileOut = new FileOutputStream("library.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this);
            out.close();
            fileOut.close();
        }catch(Exception e){}
    }
    
    public static MusicLibrary load(){
        MusicLibrary toReturn = null;
        try{
            FileInputStream fileIn = new FileInputStream("library.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            toReturn = (MusicLibrary) in.readObject();
            in.close();
            fileIn.close();
        }catch(IOException | ClassNotFoundException e){
            toReturn = new MusicLibrary();
            System.out.println(e);
        }
        toReturn.library = FXCollections.observableList(toReturn.libraryList);
        return toReturn;
    }
}