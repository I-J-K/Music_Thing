/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package music_thing;
import java.io.*;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
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
    private SimpleStringProperty name;
    private SimpleStringProperty artist;
    private SimpleStringProperty albumArtist;
    private SimpleStringProperty album;
    
    private SimpleStringProperty genre;
    private SimpleStringProperty composer;
    private SimpleStringProperty trackNumber;
    private SimpleDoubleProperty rating;
    private SimpleStringProperty year;
    
    private SongType type;
    private SimpleIntegerProperty length;
    private SimpleIntegerProperty playCount;
    private SimpleStringProperty albumArt;
    private SimpleStringProperty path;
    
    public Track(SongType type, String path){
        this.path.set(path);
        this.playCount.set(0);
        this.type = type;
        this.rating.set(0.0);
        if(type==SongType.MIDI || type==SongType.AIFF || type==SongType.MP4){
        
        }else{
            try{
                AudioFile f = AudioFileIO.read(new File("music/"+path));
                Tag tag = f.getTag();
                AudioHeader ah = f.getAudioHeader();

                this.name.set(tag.getFirst(FieldKey.TITLE));
                this.artist.set(tag.getFirst(FieldKey.ARTIST));
                this.albumArtist.set(tag.getFirst(FieldKey.ALBUM_ARTIST));
                this.album.set(tag.getFirst(FieldKey.ALBUM));
                this.genre.set(tag.getFirst(FieldKey.GENRE));
                this.composer.set(tag.getFirst(FieldKey.COMPOSER));
                this.trackNumber.set(tag.getFirst(FieldKey.TRACK));
                this.year.set(tag.getFirst(FieldKey.YEAR));
                this.length.set(f.getAudioHeader().getTrackLength());
            }catch (CannotReadException | IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException | KeyNotFoundException e){}  
        }
        
        if(this.name == null || this.name.equals("")){
                this.name.set(path.substring(0,path.lastIndexOf('.')));
        }
        
        if(this.length==null){
            this.length.set(0);
        }
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }
    
    public SimpleStringProperty nameProperty(){
        return name;
    }
    
    public SongType getType() {
        return type;
    }

    public void setType(SongType type) {
        this.type = type;
    }

    public int getLength() {
        return length.get();
    }

    public void setLength(int length) {
        this.length.set(length);
    }

    public int getPlayCount() {
        return playCount.get();
    }

    public void setPlayCount(int playCount) {
        this.playCount.set(playCount);
    }
    
    public SimpleIntegerProperty playCountProperty(){
        return playCount;
    }

    public String getAlbumArt() {
        return albumArt.get();
    }

    public void setAlbumArt(String albumArt) {
        this.albumArt.set(albumArt);
    }
    
    public SimpleStringProperty albumArtProperty(){
        return albumArt;
    }

    public String getPath() {
        return path.get();
    }

    public void setPath(String path) {
        this.path.set(path);
    }
    
    public SimpleStringProperty pathProperty(){
        return path;
    }
    
    public String getArtist() {
        return artist.get();
    }

    public void setArtist(String artist) {
        this.artist.set(artist);
    }
    
    public SimpleStringProperty artistProperty(){
        return artist;
    }

    public String getAlbum() {
        return album.get();
    }

    public void setAlbum(String album) {
        this.album.set(album);
    }
    
    public SimpleStringProperty albumProperty(){
        return album;
    }

    public String getGenre() {
        return genre.get();
    }

    public void setGenre(String genre) {
        this.genre.set(genre);
    }
    
    public SimpleStringProperty genreProperty(){
        return genre;
    }

    public Double getRating() {
        return rating.get();
    }

    public void setRating(Double rating) {
        this.rating.set(rating);
    }
    
    public SimpleDoubleProperty ratingProperty(){
        return rating;
    }

    public String getComposer() {
        return composer.get();
    }

    public void setComposer(String composer) {
        this.composer.set(composer);
    }
    
    public SimpleStringProperty composerProperty(){
        return composer;
    }

    public String getTrackNumber() {
        return trackNumber.get();
    }

    public void setTrackNumber(String trackNumber) {
        this.trackNumber.set(trackNumber);
    }    
    
    public SimpleStringProperty trackNumberProperty(){
        return trackNumber;
    }
    
    
    public void saveTags(){
        try{AudioFile f = AudioFileIO.read(new File("music/"+path));
            Tag tag = f.getTag();
            tag.setField(FieldKey.TITLE,name.get());
            tag.setField(FieldKey.ARTIST,artist.get());
            tag.setField(FieldKey.ALBUM,album.get());
            tag.setField(FieldKey.GENRE,genre.get());
            tag.setField(FieldKey.COMPOSER,composer.get());
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