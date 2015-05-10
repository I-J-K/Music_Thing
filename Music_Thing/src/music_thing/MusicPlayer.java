/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package music_thing;

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
    public final long oneSecond = 1000;
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
    
    public static void setPlayTime(int time){
        playTime = time;
    }
    
    public static int getPlayTime(){
        return playTime;
    }
    
    public abstract void reset();
    
    public abstract void play(Track track, double volume);
    
    public abstract void pause();
    
    public abstract void stop();
    
    public abstract void setVolume(double volume);
    
    public abstract double getCurrentTime();
    
    public abstract void setSong(Track track);
    
    public void count(){
        try{
            synchronized(this){
                while(getPlaying()){
                    this.wait(oneSecond);
                    playTime++;
                } 
            } 
        }catch(InterruptedException i){}
    }
}
