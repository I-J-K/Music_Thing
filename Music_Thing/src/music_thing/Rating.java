/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package music_thing;

import java.io.IOException;
import java.util.ArrayList;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 *
 * @author joshuakaplan
 */
public class Rating extends HBox{
    private final ArrayList<Label> stars;
    private IntegerProperty rating;
    public static Color unColor = Color.rgb(255, 255, 255, .5);
    public static Color selColor = Color.WHITE;
    
    public Rating() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("rating.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        stars = new ArrayList<>();
        for(int i = 0; i<5; i++){
            Label t = new Label("⋆");
            t.setFont(Font.font(31));
            t.setTextFill(unColor);
            t.setOnMouseClicked(new EventHandler(){
                @Override
                public void handle(Event event) {
                    setRating(stars.indexOf(t)+1);
                }
            });
            stars.add(t);
            getChildren().add(t);
        }
        setAlignment(Pos.CENTER_LEFT);
        this.setPadding(new Insets(-15,0,-10,0));
        rating = new SimpleIntegerProperty();
    }
    
    public void setMax(int max){
        if(stars.size()>max){
            while(stars.size()>max){
                getChildren().remove(stars.remove(stars.size()-1));
                stars.remove(stars.size()-1);
            }
        }else if(stars.size()<max){
            while(stars.size()<max){
                Label t = new Label("⋆");
                t.setTextFill(unColor);
                stars.add(t);
                getChildren().add(t); 
            }
        }
    }
    
    public void setRating(int rating){
        if(rating>=0 && rating<=stars.size()){
            for(int i=0; i<rating; i++){
                stars.get(i).setTextFill(selColor);
            }
            for(int i=rating; i<stars.size(); i++){
                stars.get(i).setTextFill(unColor);
            }
        }
        this.rating.set(rating);
    }
    
    public int getRating(){
        return rating.get();
    }
    
    public IntegerProperty getRatingProperty(){
        return rating;
    }

    public static Color getUnColor() {
        return unColor;
    }

    public static void setUnColor(Color unColor) {
        Rating.unColor = unColor;
    }

    public static Color getSelColor() {
        return selColor;
    }

    public static void setSelColor(Color selColor) {
        Rating.selColor = selColor;
    }
}
