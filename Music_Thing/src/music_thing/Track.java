/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package music_thing;
import java.io.*;
import javafx.beans.property.DoubleProperty;
import org.jaudiotagger.*;
import org.jaudiotagger.audio.mp3.*;
import org.jaudiotagger.audio.flac.*;


import org.jaudiotagger.audio.*;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.*;
//import org.jaudiotagger.ogg.util.*;
import org.jaudiotagger.tag.flac.FlacTag;
import org.jaudiotagger.tag.FieldKey.*;
/**
 *
 * @author joshuakaplan
 */
public class Track implements java.io.Serializable{
    private String name;
    private String artist;
    private String albumArtist;
    private String album;
    
    private String genre;
    private String composer;
    private String trackNumber;
    private Double rating;
    private String year;
    
    private SongType type;
    private TimeFormat length;
    private Integer playCount;
    private String albumArt;
    private String path;
    
    public Track(SongType type, String path){
        this.path = path;
        this.playCount = 0;
        this.type = type;
        this.rating = 0.0;
        try{
            AudioFile f = AudioFileIO.read(new File("music/"+path));
            Tag tag = f.getTag();
            AudioHeader ah = f.getAudioHeader();

            this.name = tag.getFirst(FieldKey.TITLE);
            this.artist = tag.getFirst(FieldKey.ARTIST);
            this.albumArtist = tag.getFirst(FieldKey.ALBUM_ARTIST);
            this.album = tag.getFirst(FieldKey.ALBUM);
            this.genre = tag.getFirst(FieldKey.GENRE);
            this.composer = tag.getFirst(FieldKey.COMPOSER);
            this.trackNumber = tag.getFirst(FieldKey.TRACK);
            this.year = tag.getFirst(FieldKey.YEAR);            
            this.length = new TimeFormat(f.getAudioHeader().getTrackLength());
        }catch (CannotReadException | IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException | KeyNotFoundException e){}  
    
        
        if(this.name == null || this.name.equals("")){
                this.name = path.substring(0,path.lastIndexOf('.'));
        }
        
        if(this.length==null){
            this.length=new TimeFormat(0);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SongType getType() {
        return type;
    }

    public void setType(SongType type) {
        this.type = type;
    }

    public TimeFormat getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = new TimeFormat(length);
    }

    public int getPlayCount() {
        return playCount;
    }

    public void setPlayCount(int playCount) {
        this.playCount = playCount;
    }

    public String getAlbumArt() {
        return albumArt;
    }

    public void setAlbumArt(String albumArt) {
        this.albumArt = albumArt;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getComposer() {
        return composer;
    }

    public void setComposer(String composer) {
        this.composer = composer;
    }

    public String getTrackNumber() {
        return trackNumber;
    }

    public void setTrackNumber(String trackNumber) {
        this.trackNumber = trackNumber;
    }    
    
    public void saveTags(){
        try{AudioFile f = AudioFileIO.read(new File("music/"+path));
            Tag tag = f.getTag();
            tag.setField(FieldKey.TITLE,name);
            tag.setField(FieldKey.ARTIST,artist);
            tag.setField(FieldKey.ALBUM,album);
            tag.setField(FieldKey.GENRE,genre);
            tag.setField(FieldKey.COMPOSER,composer);
            AudioFileIO.write(f);
        }catch(Exception e){}        
    }
    
    @Override
    public boolean equals(Object o){
        if(o!=null && ((Track)o).getPath()!=null){
            return this.getPath().equals(((Track)o).getPath());
        }
        return false;
    }
}