package se206_util;

import java.io.File;
import java.util.ArrayList;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;

public class MediaHandler {
	private static MediaPlayer _mp;
	
	public static void play(String filename) {
		if (filename.equals("None")) {
			return;
		}
		//ref: http://stackoverflow.com/questions/22490064/how-to-use-javafx-mediaplayer-correctly
		Media m = new Media(new File(filename).toURI().toString());
		_mp = new MediaPlayer(m);
		_mp.setVolume(0.08);
		_mp.play();
	}
	
	public static void stop() {
		if (_mp != null && _mp.getStatus().equals(Status.PLAYING)) {
			_mp.stop();
		}
	}
	
	public static void tracklist() {
		ArrayList<String> tracks = new ArrayList<>();
		File[] dir = new File("resources/soundtrack/").listFiles();
		for (File f: dir) {
			tracks.add(f.getName());
		}
	}
	
}
