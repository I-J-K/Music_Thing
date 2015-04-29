/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package music_thing;

import java.io.Serializable;

/**
 *
 * @author joshuakaplan
 */
public class TimeFormat implements Serializable{
    private int seconds;
    private int minutes;
    private int hours;
    
    public TimeFormat(int seconds){
        this.seconds = seconds;
        this.minutes = this.seconds/60;
        this.seconds = this.seconds%60;
        this.hours = this.minutes/60;
        this.minutes = this.minutes%60;
    }
    
    @Override
    public String toString(){
        String sHours = "" + hours;
        String sMinutes = "";
        String sSeconds = "";
        if(seconds<10)sSeconds += 0;
        if(minutes<10 && hours!=0)sMinutes += 0;
        sMinutes += minutes;
        sSeconds += seconds;
        if(hours!=0){
            return sHours+":"+sMinutes+":"+sSeconds;
        }
        return sMinutes+":"+sSeconds;
    }
}
