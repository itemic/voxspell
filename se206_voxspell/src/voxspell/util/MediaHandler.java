package voxspell.util;

import java.io.File;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;

/**
 * Class tasked with handling all MediaPlayers so there won't
 * be multiple instances at the same time.
 * @author terran
 *
 */
public class MediaHandler {
	private static MediaPlayer _mp;
	private static double volume = 0.1;
	
	/**
	 * Creates a MediaPlayer to play music.
	 * @param filename The location of the music file
	 */
	public static void play(String filename) {
		//Plays music from a given string filename
		if (filename.equals("None")) {
			return;
		}
		//ref: http://stackoverflow.com/questions/22490064/how-to-use-javafx-mediaplayer-correctly
		Media m = new Media(new File(filename).toURI().toString());
		_mp = new MediaPlayer(m);
		_mp.setVolume(volume);
		_mp.play();
	}
	
	/**
	 * Set the MediaPlayer volume if one exists.
	 * @param d The volume (between 0 and 1)
	 */
	public static void setVolume(double d) {
		volume = d;
		if (_mp != null) { //only set it if there is a music player at the moment
			_mp.setVolume(volume);	
		}
		
	}
	
	/**
	 * Stops the current MediaPlayer only if it is not playing 
	 * (and if it exists).
	 */
	public static void stop() {
		if (_mp != null && _mp.getStatus().equals(Status.PLAYING)) {
			_mp.stop();
		}
	}
	
	
}
