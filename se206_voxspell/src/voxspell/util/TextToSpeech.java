package voxspell.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import javafx.concurrent.Task;
import javafx.scene.Node;

/**
 * <h1>TextToSpeech</h1> This class is responsible for taking text, synthesising
 * speech from it and then immediately playing it on the speakers
 * <p>
 * This happens on another thread so that the GUI doesn't freeze
 * Implementation from shared prototype. 
 * 
 * @version 0.4
 * @author tkro003 (primary)
 * @author mkem114 (secondary)
 * @since 2016-09-03
 */
public class TextToSpeech {


	/**
	 * <h1>OS</h1> Represents the three main PC operating system categories
	 * (OSX, Linux, Windows)
	 * <p>
	 * 
	 * @version 1.0
	 * @author mkem114 (primary)
	 * @since 2016-09-16
	 */
	private enum OS {
		WINDOWS, OSX, LINUX
	}

	/**
	 * The location of festival in UG4
	 */
	public static final String festivalLocation = "/usr/share/festival/voices/english";

	private static TextToSpeech _instance = null;
	private static OS _os;

	private Thread lastSpeech;
	private int _selectedVoiceInt;
	private ArrayList<String> _voices = new ArrayList<>();

	/**
	 * This constructor determines the operating system and picks the default
	 * voice
	 */
	private TextToSpeech() {
		lastSpeech = null;
		if (System.getProperty("os.name").matches("Mac OS X")) {
			_os = OS.OSX;
		} else if (System.getProperty("os.name").matches("Linux")) {
			_os = OS.LINUX;
		} else {
			_os = OS.WINDOWS;
		}
		makeVoices();
		chooseVoice(0);
	}

	/**
	 * The available voices' names
	 * 
	 * @return ArrayList of voice names to choose from
	 */
	public ArrayList<String> voices() {
		return _voices;
	}

	/**
	 * Which number in the list of voice names is the selected voice
	 * 
	 * @return Number of the selected voice
	 */
	public int selectedVoiceNum() {
		return _selectedVoiceInt;
	}

	/**
	 * Select a voice to use by using the number of order it's in the list
	 * 
	 * @param index
	 *            Number along the list to choose
	 */
	public void chooseVoice(int index) {
		_selectedVoiceInt = index;
	}

	/**
	 * Name of the current voice
	 * 
	 * @return Voice name
	 */
	public String selectedVoice() {
		return _voices.get(_selectedVoiceInt);
	}

	/**
	 * Access to the singleton instance of TextToSpeech
	 * 
	 * @return Singleton's reference
	 */
	public static TextToSpeech access() {
		if (_instance == null) {
			_instance = new TextToSpeech();
		}
		return _instance;
	}

	/**
	 * Calls the text-to-speech process to speak the string that is provided,
	 * while disabling all the controls that could change the flow of the game.
	 * Some implementation done by Michael.
	 * @param speak The string to speak
	 * @param controls The controls to disable
	 */
	public void speak(String speak, ArrayList<Node> controls) {
		ArrayList<String> cmd = new ArrayList<>();
		if (_os == OS.OSX) {
			cmd.add("/bin/bash");
			cmd.add("-c");
			cmd.add("say -v " + _voices.get(_selectedVoiceInt) + " " + speak);
		} else if (_os == OS.LINUX) {
			//Instead of using bash, directly call the Festival command
			cmd.add("/usr/bin/festival");
			cmd.add("(Parameter.set 'Duration_Stretch 1.2)");
			cmd.add("(voice_" + selectedVoice() + ")");
			cmd.add("(SayText \"" + speak + "\")");
			cmd.add("(exit)");
		}
		if (_os != OS.WINDOWS) {
			Thread tmp = new Thread(new Speech(lastSpeech, cmd, controls));
			tmp.start();
			lastSpeech = tmp;

		}

	}

	/**
	 * Find the list of available voices depending on the user's operating system
	 */
	private void makeVoices() {
		String cmd = null;
		if (_os == OS.OSX) {
			try {
				// Usage of awk referenced from
				// http://stackoverflow.com/questions/2440414/
				cmd = "say -v '?' | awk '{print $1;}'";
				ProcessBuilder pb = new ProcessBuilder("/bin/bash", "-c", cmd);
				Process p = pb.start();
				InputStream stdout = p.getInputStream();
				BufferedReader out = new BufferedReader(new InputStreamReader(stdout));
				String line = null;
				while ((line = out.readLine()) != null) {
					_voices.add(line);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (_os == OS.LINUX) {
			try {
				cmd = "ls " + festivalLocation;
				ProcessBuilder pb = new ProcessBuilder("/bin/bash", "-c", cmd);
				Process p = pb.start();
				InputStream stdout = p.getInputStream();
				BufferedReader out = new BufferedReader(new InputStreamReader(stdout));
				String line = null;
				while ((line = out.readLine()) != null) {
					_voices.add(line);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Not available on Windows.");
			_voices.add("Temporary"); // temporary solution to prevent crashing
			// on Windows
			_voices.add("Solution");
			_voices.add("Windows");
		}
	}
	

	/***
	 * <h1>Speech</h1> This class is a one time use that speaks one piece of
	 * text, it links with what's to be said before it so that speaking doesn't
	 * occur simultaneously and queues up
	 * 
	 * @version 1.6
	 * @author mkem114
	 * @author tkro003 (updated with control disable functionality)
	 * @since 2016-09-19
	 */
	@SuppressWarnings("rawtypes")
	private class Speech extends Task {
		Thread _last;
		ArrayList<String> _cmd;
		ArrayList<Node> _disable;

		/**
		 * The only constructor requires what comes before and what to say
		 * 
		 * @param last
		 *            What's said first
		 * @param cmd
		 *            What to say
		 */
		public Speech(Thread last, ArrayList<String> cmd, ArrayList<Node> disable) {
			super();
			_last = last;
			_cmd = cmd;
			_disable = disable;
		}

		/**
		 * Actually says it
		 */
		protected Object call() throws Exception {
			if (_last != null) {
				_last.join();
			}
			
			ProcessBuilder pb = new ProcessBuilder(_cmd);
			try {
				// testing concurrency
				for (Node n: _disable) {
					n.setDisable(true);
				}
				Process p = pb.start();
				p.waitFor();
				
				for (Node n: _disable) {
					n.setDisable(false);
				}
				
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
			return null;
		}
	}
}