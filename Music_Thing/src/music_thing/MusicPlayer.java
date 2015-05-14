/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package music_thing;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 *
 * @author joshuakaplan
 * 
 * Is the super class for ClipPlayer, MidiPlayer, and JavafxPlayer
 * 
 * We also need to implement a player for "ogg" files.
 * 
 */
public abstract class MusicPlayer {
    private static int playTime;
    private static boolean playing = false;
    private static Track currentTrack;
    private static IntegerProperty currentTime = new SimpleIntegerProperty();
    
    public void setPlaying(boolean bool){
        playing = bool;
    }
    
    public void setCurrentTrack(Track track){
        currentTrack = track;
    }
    
    public boolean getPlaying(){
        return playing;
    }

    public Track getCurrentTrack() {
        return currentTrack;
    }
    
    public void setCurrentTime(int time){
        currentTime.set(time);
    }
    
    public int getCurrentTime(){
        return currentTime.get();
    }
    
    public static IntegerProperty getTimeProperty(){
        return currentTime;
    }
    
    public abstract void seek(int seconds);
    
    public abstract void reset();
    
    public abstract void play(Track track, double volume);
    
    public abstract void pause();
    
    public abstract void stop();
    
    public abstract void setVolume(double volume);
    
    public abstract double getSongTime();
    
    public abstract void setSong(Track track);
    
    public abstract int getSongLength();
}
