package voxspell.util;

import java.io.File;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;

public class MediaHandler {
	private static MediaPlayer _mp;
	private static double volume = 0.1;
	
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
	
	public static void setVolume(double d) {
		//set the music volume
		volume = d;
		if (_mp != null) { //only set it if there is a music player at the moment
			_mp.setVolume(volume);	
		}
		
	}
	
	public static void stop() {
		if (_mp != null && _mp.getStatus().equals(Status.PLAYING)) {
			_mp.stop();
		}
	}
	
	
}
