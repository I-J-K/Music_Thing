/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package music_thing;
import java.io.*;
import org.jaudiotagger.*;
import org.jaudiotagger.audio.mp3.*;
import org.jaudiotagger.audio.*;
import org.jaudiotagger.tag.*;
import org.jaudiotagger.tag.FieldKey.*;
/**
 *
 * @author joshuakaplan
 */
public class Track implements java.io.Serializable{
    private String name;
    private String artist;
    private String album;
    private String genre;
    private String composer;
    private String trackNumber;
    private Double rating;
    
    private SongType type;
    private double length;
    private Integer playCount;
    private String albumArt;
    private String path;
    
    public Track(SongType type, String path){
        this.path = path;
        this.playCount = 0;
        this.type = type;
        this.name = path.substring(0,path.lastIndexOf('.'));
        if(type==SongType.MP3){
            try{
                AudioFile f = AudioFileIO.read(new File("music/"+path));
                Tag tag = f.getTag();
                AudioHeader ah = f.getAudioHeader();

                this.name = tag.getFirst(FieldKey.TITLE);
                this.artist = tag.getFirst(FieldKey.ARTIST);
                this.album = tag.getFirst(FieldKey.ALBUM);
                this.composer = tag.getFirst(FieldKey.COMPOSER);
                this.trackNumber = tag.getFirst(FieldKey.TRACK);
                this.length = f.getAudioHeader().getTrackLength();
            }catch (Exception e){}
            if(name == null){
                this.name = path.substring(0,path.lastIndexOf('.'));
            }      
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

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
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
    
}
