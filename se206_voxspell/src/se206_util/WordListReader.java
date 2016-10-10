package se206_util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class WordListReader {

	//Will ignore concurrency temporarily
	private static final String DEFAULT_WORDLIST = "se206_voxspell/resources/nzcer-wordlist.txt";
	private BufferedReader _fileReader;
	private ArrayList<String> _levelNames = new ArrayList<>();
	
	public WordListReader() throws FileNotFoundException {
		this(DEFAULT_WORDLIST); 
	}
	
	public WordListReader(String filename) throws FileNotFoundException {
		_fileReader = new BufferedReader(new FileReader(filename));
		//extend to other file lists
	}
	
	public ArrayList<ArrayList<String>> readWords() throws IOException {
		ArrayList<ArrayList<String>> words = new ArrayList<>();
		String w;
		while ((w = _fileReader.readLine()) != null) {
			if (w.startsWith("%")) {
//				System.out.println(w.substring(1));
				_levelNames.add(w.substring(1));
				
				words.add(new ArrayList<String>());
			} else {
				words.get(words.size()-1).add(w);
				
			}
		}
		return words;
	}
	
	public ArrayList<String> getLevelNames() {
		return _levelNames;
		//find a better way to do this.
	}
}
