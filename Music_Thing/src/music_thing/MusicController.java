/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package music_thing;

import java.io.File;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

/**
 *
 * @author joshuakaplan
 */
public class MusicController {
    
    private static MediaPlayer mp3player;
    private static Media mp3;
    private static boolean playing = false;
    private static Track currentTrack;
    
    public static void setSong(Track track){
        File file = new File("music/"+track.getPath());
        if(track!=currentTrack && mp3!=null)mp3player.stop();
        mp3 = new Media(file.toURI().toString());
        mp3player = new MediaPlayer(mp3);
    }
    
    
    
    public static void play(Track track){
        if(mp3==null || track!=currentTrack){
            setSong(track);
        }
        mp3player.play();
        playing = true;
        currentTrack = track;
    }
    
    public static void pause(){
        mp3player.pause();
        playing = false;
    }
    
    public static boolean getPlaying(){
        return playing;
    }

    public static Track getCurrentTrack() {
        return currentTrack;
    }
}
