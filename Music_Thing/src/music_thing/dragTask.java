/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package music_thing;

import java.io.File;
import java.util.List;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;

/**
 *
 * @author csstudent
 */
public class dragTask extends Task<Void> {
    final private List<File> files;
    final private DragEvent event;
    
    public dragTask(List<File> files, DragEvent ev){
        super();
        this.files = files;
        event = ev;
    }
    
    @Override
    protected Void call() throws Exception {
        boolean imported = false;
        for (File file : files) {
            imported = Main.getController().importFile(file) || imported;
        }
        event.setDropCompleted(true);
        Main.getController().getSongList().sort();
        MusicLibrary.save();
        if(imported){
            Platform.runLater(() -> {FXMLDocumentController.alertImportComplete(true);});
        }else{
            Platform.runLater(() -> {FXMLDocumentController.alertImportComplete(false);});
        }
        return null;
    }
    
}
