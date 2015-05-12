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
 * Just an enum, just the types we want to have supported, so that you can't 
 * give any type you want. 
 * 
 * Used in FXMLDocumentController, in the play method, and is stored in every
 * track.
 * 
 */
public enum SongType implements java.io.Serializable{
    MP3,WAV,MIDI,AIFF,FLAC,OGG,M4A,AAC,AU
}
