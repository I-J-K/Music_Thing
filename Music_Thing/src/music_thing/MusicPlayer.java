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
public abstract class MusicPlayer {
    private static boolean playing = false;
    private static Track currentTrack;
    
    
    public static void setPlaying(boolean bool){
        playing = bool;
    }
    
    public static void setCurrentTrack(Track track){
        currentTrack = track;
    }
    
    public static boolean getPlaying(){
        return playing;
    }

    public static Track getCurrentTrack() {
        return currentTrack;
    }
    
    public abstract void reset();
    
    public abstract void play(Track track, double volume);
    
    public abstract void pause();
    
    public abstract void stop();
    
    public abstract void setVolume(double volume);
    
    public abstract double getCurrentTime();
    
    public abstract void setSong(Track track);
}
