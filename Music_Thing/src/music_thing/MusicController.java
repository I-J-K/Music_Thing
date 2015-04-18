/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package music_thing;

import java.io.File;
import java.io.IOException;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javax.sound.midi.*;
/**
 *
 * @author joshuakaplan
 */
public class MusicController {
    
    private static MediaPlayer mp3player;
    private static Media mp3;
    private static boolean playing = false;
    private static Track currentTrack;
    private static Sequencer midiSequencer;
    private static Sequence midiSequence;
    
    public static void setSong(Track track){
        stop(track);
        File file = new File("music/"+track.getPath());
        SongType type = track.getType();
        if(type==SongType.MP3){
            mp3 = new Media(file.toURI().toString());
            mp3player = new MediaPlayer(mp3);
        }else if(type==SongType.MIDI){
            try{
                midiSequence = MidiSystem.getSequence(file);
                midiSequencer.setSequence(midiSequence);
            }catch(InvalidMidiDataException | IOException e){}
        }
    }
    
    
    
    public static void play(Track track){
        SongType type = track.getType();
        if(type==SongType.MP3){
            if(mp3==null || track!=currentTrack){
                setSong(track);
            }
            mp3player.play();
        }else if(type==SongType.MIDI){
            try{
                if(midiSequencer==null)midiSequencer = MidiSystem.getSequencer();
                if(midiSequence==null || track!=currentTrack)setSong(track);
                midiSequencer.open();
                midiSequencer.start();
            }catch(Exception e){}
        }
        playing = true;
        currentTrack = track;
    }
    
    public static void pause(){
        SongType type = currentTrack.getType();
        if(type==SongType.MP3){
            mp3player.pause();
        }else if(type==SongType.MIDI){
            midiSequencer.stop();
        }
        playing = false;
    }
    
    public static boolean getPlaying(){
        return playing;
    }

    public static Track getCurrentTrack() {
        return currentTrack;
    }
    
    public static void stop(Track track){
        if(midiSequencer!=null)midiSequencer.close();
        if(mp3player!=null)mp3player.stop();
    }
}