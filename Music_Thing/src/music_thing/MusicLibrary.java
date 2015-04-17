/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package music_thing;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author joshuakaplan
 */
public class MusicLibrary {
    private int track=0;
    
    private final ObservableList<Track> library = FXCollections.observableArrayList(
            new Track("Strong in the real way",SongType.MP3,"strong.mp3"),
            new Track("Amalgam", SongType.MP3, "aivi & surasshu", "Steven Universe", "Cartoon", 5.0, "amalgam.mp3"),
            new Track("Opal", SongType.MP3, "aivi & surasshu", "Steven Universe", "Cartoon", 5.0, "opal.mp3")
    );

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
}
