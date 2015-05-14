/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package music_thing;

import java.io.File;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/**
 *
 * @author joshuakaplan
 * 
 * Plays mp3, m4a, wav, and aiff files... this is all basic file types.
 */
public class JavafxPlayer extends MusicPlayer{
    private static MediaPlayer mp3player;
    private static Media mp3;
    
    @Override
    public void seek(int seconds){
        mp3player.seek(Duration.seconds(seconds));
    }
    
    @Override
    public int getSongLength(){
        return (int) mp3.getDuration().toSeconds();
    }
    
    @Override
    public void reset(){
        setSong(getCurrentTrack());
    }
    
    @Override
    public void setSong(Track track){
        stop();
        File file = new File("music/"+track.getPath());
        mp3 = new Media(file.toURI().toString());
        mp3player = new MediaPlayer(mp3);
    }
    
    @Override
    public double getSongTime(){
        if(mp3player!=null && mp3!=null){
            return mp3player.getCurrentTime().toSeconds();
        }
        return 0.0;
    }
    
    @Override
    public void play(Track track, double volume){
        if(mp3==null || track!=getCurrentTrack()){
            setSong(track);
        }
        mp3player.play();

        setPlaying(true);
        setCurrentTrack(track);
        setVolume(volume);
    }
    
    @Override
    public void pause(){
        mp3player.pause();
        setPlaying(false);
    }
    
    @Override
    public void stop(){
        if(mp3player!=null){
            if(mp3player.getStatus()!=MediaPlayer.Status.STOPPED) mp3player.stop();
            mp3player.dispose();
        }
        setPlaying(false);
    }
    
    @Override
    public void setVolume(double volume){
        if(getCurrentTrack()!=null){
            mp3player.setVolume(volume);
        }
    }
}
