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
    
    private static boolean playing = false;
    private static Track currentTrack;
    
    private static MediaPlayer mp3player;
    private static Media mp3;
    
    private static Sequencer midiSequencer;
    private static Sequence midiSequence;
    private static Synthesizer midiSynthesizer;
    
    public static void setSong(Track track){
        stop();
        File file = new File("music/"+track.getPath());
        SongType type = track.getType();
        if(type==SongType.MP3 || type==SongType.AAC || type==SongType.WAV || type==SongType.AIFF){
            mp3 = new Media(file.toURI().toString());
            mp3player = new MediaPlayer(mp3);
        }else if(type==SongType.MIDI){
            try{
                midiSequence = MidiSystem.getSequence(file);
                midiSequencer.setSequence(midiSequence);
            }catch(InvalidMidiDataException | IOException e){System.out.println(e);}
        }
    }
    
    public static double getCurrentTime(){
        SongType type = currentTrack.getType();
        if(type==SongType.MP3 || type==SongType.AAC || type==SongType.WAV || type==SongType.AIFF){
            if(mp3player!=null && mp3!=null){
                return mp3player.getCurrentTime().toSeconds();
            }
        }else if(type==SongType.MIDI){
            if(midiSequence!=null && midiSequencer!=null){
                return midiSequencer.getMicrosecondPosition()/1000000.0;
            }
        }
        return 0.0;
    }
    
    public static void play(Track track, double volume){
        SongType type = track.getType();
        if(type==SongType.MP3 || type==SongType.AAC || type==SongType.WAV || type==SongType.AIFF){
            if(mp3==null || track!=currentTrack){
                setSong(track);
            }
            mp3player.play();
        }else if(type==SongType.MIDI){
            try{
                if(midiSequencer==null)midiSequencer = MidiSystem.getSequencer(false);
                if(midiSequence==null || track!=currentTrack)setSong(track);
                midiSynthesizer = MidiSystem.getSynthesizer();
                Soundbank soundbank = midiSynthesizer.getDefaultSoundbank();
                if(soundbank == null) {
                   midiSequencer.getTransmitter().setReceiver(MidiSystem.getReceiver());
                }
                else {
                   midiSynthesizer.loadAllInstruments(soundbank);
                   midiSequencer.getTransmitter().setReceiver(midiSynthesizer.getReceiver());
                }
                midiSynthesizer.open();
                midiSequencer.open();
                midiSequencer.start();
            }catch(Exception e){}
        }
        playing = true;
        currentTrack = track;
        setVolume(volume);
    }
    
    public static void pause(){
        SongType type = currentTrack.getType();
        if(type==SongType.MP3 || type==SongType.AAC || type==SongType.WAV || type==SongType.AIFF){
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
    
    public static void stop(){
        if(midiSequencer!=null)midiSequencer.close();
        if(midiSynthesizer!=null)midiSynthesizer.close();
        if(mp3player!=null){
            if(mp3player.getStatus()!=MediaPlayer.Status.STOPPED) mp3player.stop();
            mp3player.dispose();
        }
        playing=false;
    }
    
    public static void setVolume(double volume){
        if(currentTrack!=null){
            SongType type = currentTrack.getType();
            if(type==SongType.MP3 || type==SongType.AAC || type==SongType.WAV || type==SongType.AIFF){
                mp3player.setVolume(volume);
            }else if(type==SongType.MIDI){
                try{
                    if(midiSynthesizer.getDefaultSoundbank()==null) {
                        for ( int i = 0; i < 16; i++ ) {
                           midiSynthesizer.getReceiver().send(new ShortMessage(ShortMessage.CONTROL_CHANGE, i, 7, (int)(volume*127)), -1 );
                        }
                     }
                     else {
                        for(MidiChannel c : midiSynthesizer.getChannels()) {
                           if(c != null)c.controlChange(7, (int)(volume*127));
                        }
                     }
                }catch(Exception e){}
            }
        }
    }
}