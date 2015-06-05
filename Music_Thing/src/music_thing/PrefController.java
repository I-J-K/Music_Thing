/*
 * Copyright (C) 2015 csstudent
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package music_thing;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author csstudent
 */
public class PrefController implements Initializable, Serializable {
    private Stage dialogStage;
    private boolean okClicked = false;
    @FXML
    private ColorPicker bc1;
    @FXML
    private ColorPicker bc2;
    @FXML
    private ColorPicker tc;
    @FXML
    private ColorPicker sc;
    @FXML
    private ColorPicker rc1;
    @FXML
    private ColorPicker rc2;
    
    private static Color backgroundColor1=Color.BLACK;
    private static Color backgroundColor2=Color.DIMGRAY;
    private static Color selectionColor=Color.DARKGRAY;
    private static Color textColor=Color.WHITE;
    private static Color ratingsColor1=Color.rgb(255, 255, 255, .5);
    private static Color ratingsColor2=Color.WHITE;
    
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    
    public boolean isOkClicked() {
        return okClicked;
    }
    
    @FXML
    private void handleOk() {
        backgroundColor1 = bc1.getValue();
        backgroundColor2 = bc2.getValue();
        selectionColor = sc.getValue();
        textColor = tc.getValue();
        ratingsColor1 = rc1.getValue();
        ratingsColor2 = rc2.getValue();
        Rating.setUnColor(ratingsColor1);
        Rating.setSelColor(ratingsColor2);
        update();
        okClicked = true;
        save();
        dialogStage.close();
    }
    public static void applyStyles(Parent p){
        p.setStyle("");
        p.setStyle(p.getStyle()+"-textColor: "+formatRGB(textColor)+";");
        p.setStyle(p.getStyle()+"-backgroundColor: "+formatRGB(backgroundColor1)+";");
        p.setStyle(p.getStyle()+"-secondBackgroundColor: "+formatRGB(backgroundColor2)+";");
        p.setStyle(p.getStyle()+"-selectionColor: "+formatRGB(selectionColor)+";");
    }
    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }
    
    private static String formatRGB(Color c) {
        String red = ""+(c.getRed()*255);
        String blue = ""+(c.getBlue()*255);
        String green = ""+(c.getGreen()*255);
        String alpha = ""+(c.getOpacity());
        return "rgba("+red+","+green+","+blue+","+alpha+");";
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        load();
        rc1.setValue(ratingsColor1);
        rc2.setValue(ratingsColor2);
        bc1.setValue(backgroundColor1);
        bc2.setValue(backgroundColor2);
        tc.setValue(textColor);
        sc.setValue(selectionColor);
    }   
    
    public static void save(){
        try{
            double[][] colors = {
                {backgroundColor1.getRed(),backgroundColor1.getGreen(),backgroundColor1.getBlue(),backgroundColor1.getOpacity()},
                {backgroundColor2.getRed(),backgroundColor2.getGreen(),backgroundColor2.getBlue(),backgroundColor2.getOpacity()},
                {selectionColor.getRed(),selectionColor.getGreen(),selectionColor.getBlue(),selectionColor.getOpacity()},
                {textColor.getRed(),textColor.getGreen(),textColor.getBlue(),textColor.getOpacity()},
                {ratingsColor1.getRed(),ratingsColor1.getGreen(),ratingsColor1.getBlue(),ratingsColor1.getOpacity()},
                {ratingsColor2.getRed(),ratingsColor2.getGreen(),ratingsColor2.getBlue(),ratingsColor2.getOpacity()}
            };
            FileOutputStream fileOut = new FileOutputStream("fermata.pref");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(colors);
            out.close();
            fileOut.close();
        }catch(Exception e){}
    }
    
    public static void load(){
        try{
            FileInputStream fileIn = new FileInputStream("fermata.pref");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            double[][] colors = (double[][]) in.readObject();
            in.close();
            fileIn.close();
            backgroundColor1=new Color(colors[0][0],colors[0][1],colors[0][2],colors[0][3]);
            backgroundColor2=new Color(colors[1][0],colors[1][1],colors[1][2],colors[1][3]);
            selectionColor=new Color(colors[2][0],colors[2][1],colors[2][2],colors[2][3]);
            textColor=new Color(colors[3][0],colors[3][1],colors[3][2],colors[3][3]);
            ratingsColor1=new Color(colors[4][0],colors[4][1],colors[4][2],colors[4][3]);
            ratingsColor2=new Color(colors[5][0],colors[5][1],colors[5][2],colors[5][3]);
            Rating.setUnColor(ratingsColor1);
            Rating.setSelColor(ratingsColor2);
            update();
        }catch(Exception e){}
    }
    public static void update(){
        for(Parent p : Main.getRoots())applyStyles(p);
    }
}
