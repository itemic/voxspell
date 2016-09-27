package se206_voxspell;

import java.io.FileNotFoundException;
import java.io.IOException;

public class VOXTest {

	public static void main(String[] args) throws IOException {
		//Playground for testing things before I get into the GUIs.
		
		WordListReader wlr = new WordListReader();
		System.out.println(wlr.readWords());

	}

}
