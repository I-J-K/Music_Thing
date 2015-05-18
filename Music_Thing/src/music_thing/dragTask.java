/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package music_thing;

import java.io.File;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;

/**
 *
 * @author csstudent
 */
public class dragTask extends Task<Void> {
    final private Dragboard db;
    final private DragEvent event;
    
    public dragTask(Dragboard board, DragEvent ev){
        super();
        db = board;
        event = ev;
    }
    
    @Override
    protected Void call() throws Exception {
        boolean success = false;
        boolean imported = false;
                        System.out.println(db.hasFiles());
        if (db.hasFiles()) {
            success = true;
            for (File file : db.getFiles()) {
                imported = Main.getController().importFile(file) || imported;
            }
        }
        event.setDropCompleted(success);
        event.consume();

        if(success){
            Main.getController().getSongList().sort();
            MusicLibrary.save();
            if(imported){
                Platform.runLater(() -> {FXMLDocumentController.alertImportComplete(true);});
            }else{
                Platform.runLater(() -> {FXMLDocumentController.alertImportComplete(false);});
            }
        }
        return null;
    }
    
}
