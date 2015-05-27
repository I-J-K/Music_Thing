package music_thing;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.Initializable;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

/**
 * FXML Controller class
 *
 * @author joshuakaplan
 */

public class TwitterWindowController implements Initializable {

    
    Twitter twitter = TwitterFactory.getSingleton();
    Status status;

    public TwitterWindowController() {
        try {
            this.status = twitter.updateStatus("");
        } catch (TwitterException ex) {
            Logger.getLogger(TwitterWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
