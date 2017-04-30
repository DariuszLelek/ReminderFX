/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import java.net.URL;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;


/**
 *
 * @author Dariusz
 */
public class AudioController {

    // <editor-fold defaultstate="collapsed" desc=" Singleton ">
    private static AudioController instance;

    public synchronized static AudioController getInstance() {
        if (instance == null) {
            instance = new AudioController();
        }
        return instance;
    }
    // </editor-fold>
    private final URL file;

    public AudioController() {
        file = this.getClass().getClassLoader().getResource("sounds/notification.mp3");
    }

    public void playNotification() {
        final Media media = new Media(file.toString());
        final MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
    }
}
