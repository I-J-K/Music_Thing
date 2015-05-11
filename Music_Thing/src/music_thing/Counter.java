/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package music_thing;
/**
 *
 * @author isabella
 */
public class Counter {
    private int playTime;
    public final long oneSecond = 1000;
    
    public void count(){
        try{
            synchronized(this){
                while(MusicPlayer.getPlaying()){
                    this.wait(oneSecond);
                    playTime++;
                } 
            } 
        }catch(InterruptedException i){}
    }
    
}
