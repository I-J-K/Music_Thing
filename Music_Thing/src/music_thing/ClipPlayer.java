/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package music_thing;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
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
    private static Clip clip;
    private static final FlacFormatConversionProvider flacStreamConverter = new FlacFormatConversionProvider();
    private static final FlacAudioFileReader flacReader = new FlacAudioFileReader();
    
    @Override
    public int getSongLength(){
        return (int)clip.getMicrosecondLength()/1000000;
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
            clip = AudioSystem.getClip();
            if(track.getType()==SongType.FLAC){
                clip.open(flacStreamConverter.getAudioInputStream(AudioFormat.Encoding.PCM_SIGNED, flacReader.getAudioInputStream(file)));
            }else if(track.getType()==SongType.OGG){
                AudioInputStream in= AudioSystem.getAudioInputStream(file);
                AudioInputStream din = null;
                if (in != null){
                    AudioFormat baseFormat = in.getFormat();
                    AudioFormat  decodedFormat = new AudioFormat(
                            AudioFormat.Encoding.PCM_SIGNED,
                            baseFormat.getSampleRate(),
                            16,
                            baseFormat.getChannels(),
                            baseFormat.getChannels() * 2,
                            baseFormat.getSampleRate(),
                            false);
                     // Get AudioInputStream that will be decoded by underlying VorbisSPI
                    din = AudioSystem.getAudioInputStream(decodedFormat, in);
                    clip.open(din);
                    in.close();
                }
            }
            else{
                clip.open(AudioSystem.getAudioInputStream(file));
            }
        }catch(Exception e){System.out.println(e);}
    }
    
    @Override
    public double getSongTime(){
        if(clip!=null){
            return clip.getMicrosecondPosition()/1000000.0;
        }
        return 0.0;
    }
    
    @Override
    public void play(Track track, double volume){
        if(clip==null || track!=getCurrentTrack())setSong(track);
        clip.start();
        setPlaying(true);
        setCurrentTrack(track);
        setVolume(volume);
    }
    
    @Override
    public void pause(){
        clip.stop();
        setPlaying(false);
    }
    
    @Override
    public void stop(){
        if(clip!=null){
            clip.stop();
            clip.close();
        }
        setPlaying(false);
    }
    
    @Override
    public void setVolume(double volume){
        if(getCurrentTrack()!=null){
            FloatControl c = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            c.setValue((float) volume*(c.getMaximum()-c.getMinimum())+c.getMinimum());
        }
    }
}
