/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package music_thing;

import java.io.File;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import org.kc7bfi.jflac.sound.spi.FlacAudioFileReader;
import org.kc7bfi.jflac.sound.spi.FlacFormatConversionProvider;

/**
 *
 * @author joshuakaplan
 * 
 * Sound from built into java... now only for FLAC, but can also play
 * others... Josh will find them. 
 * 
 */
public class ClipPlayer extends MusicPlayer{
    private static Clip flacPlayer;
    private static final FlacFormatConversionProvider flacStreamConverter = new FlacFormatConversionProvider();
    private static final FlacAudioFileReader flacReader = new FlacAudioFileReader();
    
    @Override
    public void reset(){
        setSong(getCurrentTrack());
    }
    
    @Override
    public void setSong(Track track){
        stop();
        File file = new File("music/"+track.getPath());
        try{
            flacPlayer = AudioSystem.getClip();
            flacPlayer.open(flacStreamConverter.getAudioInputStream(AudioFormat.Encoding.PCM_SIGNED, flacReader.getAudioInputStream(file)));
        }catch(Exception e){System.out.println(e);}
    }
    
    @Override
    public double getSongTime(){
        if(flacPlayer!=null){
            return flacPlayer.getMicrosecondPosition()/1000000.0;
        }
        return 0.0;
    }
    
    @Override
    public void play(Track track, double volume){
        if(flacPlayer==null || track!=getCurrentTrack())setSong(track);
        flacPlayer.start();
        setPlaying(true);
        setCurrentTrack(track);
        setVolume(volume);
    }
    
    @Override
    public void pause(){
        flacPlayer.stop();
        setPlaying(false);
    }
    
    @Override
    public void stop(){
        if(flacPlayer!=null){
            flacPlayer.stop();
            flacPlayer.close();
        }
        setPlaying(false);
    }
    
    @Override
    public void setVolume(double volume){
        if(getCurrentTrack()!=null){
            FloatControl c = (FloatControl) flacPlayer.getControl(FloatControl.Type.MASTER_GAIN);
            c.setValue((float) volume*(c.getMaximum()-c.getMinimum())+c.getMinimum());
        }
    }
}
