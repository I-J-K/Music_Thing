/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package music_thing;

/**
 *
 * @author joshuakaplan
 */
public class Track {
    private String name;
    private SongType type;
    private double length;
    private int playCount;
    private String albumArt;
    private String path;

    public Track(String name, SongType type, double length, int playCount, String albumArt, String path) {
        this.name = name;
        this.type = type;
        this.length = length;
        this.playCount = playCount;
        this.albumArt = albumArt;
        this.path = path;
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
    
}
