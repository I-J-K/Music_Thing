/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package music_thing;

import java.io.File;
import java.io.IOException;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Soundbank;
import javax.sound.midi.Synthesizer;

/**
 *
 * @author joshuakaplan
 * 
 * Only plays midi files.
 * 
 */
public class MidiPlayer extends MusicPlayer{
    
    private static Sequencer midiSequencer;
    private static Sequence midiSequence;
    private static Synthesizer midiSynthesizer;
    
    @Override
    public void seek(int seconds){
        midiSequencer.setMicrosecondPosition(seconds*1000000);
    }
    
    @Override
    public int getSongLength(){
        return (int) midiSequencer.getMicrosecondLength()/1000000;
    }
    
    @Override
    public void reset(){
        setSong(getCurrentTrack());
    }
    
    @Override
    public void setSong(Track track){
        stop();
        File file = new File("music/"+track.getPath());
        try{
            midiSequence = MidiSystem.getSequence(file);
            midiSequencer.setSequence(midiSequence);
        }catch(InvalidMidiDataException | IOException e){}
    }
    
    @Override
    public double getSongTime(){
        if(midiSequence!=null && midiSequencer!=null){
            return midiSequencer.getMicrosecondPosition()/1000000.0;
        }
        return 0.0;
    }
    
    @Override
    public void play(Track track, double volume){
        try{
            if(midiSequencer==null)midiSequencer = MidiSystem.getSequencer(false);
            if(midiSequence==null || track!=getCurrentTrack())setSong(track);
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
        setPlaying(true);
        setCurrentTrack(track);
        setVolume(volume);
    }
    
    @Override
    public void pause(){
        midiSequencer.stop();
        setPlaying(false);
    }
    
    @Override
    public void stop(){
        if(midiSequencer!=null)midiSequencer.close();
        if(midiSynthesizer!=null)midiSynthesizer.close();
        setPlaying(false);
    }
    
    @Override
    public void setVolume(double volume){
        if(getCurrentTrack()!=null){
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
