package se206_model;

import java.util.ArrayList;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import se206_voxspell.MainApp;

public class StatsModel {
	//EMULATES WORD BUT NOT SERIALIZABLE
	//NOT SURE IF THERES A BETTER WAY FOR THIS
	private SimpleStringProperty word;
	private SimpleStringProperty correct;
	private SimpleStringProperty attempts;
	
	public StatsModel(WordModel w) {
		word = new SimpleStringProperty(w.getWord());
		correct = new SimpleStringProperty(w.getTimesCorrect() + "");
		attempts = new SimpleStringProperty(w.getTotalAttempts() + "");
	}
	
	public String getWord() {
		return word.get();
	}
	
	public void setWord(String s) {
		word.set(s);
	}
	
	public StringProperty wordProperty() {
		return word;
	}
	
	public String getCorrect() {
		return correct.get();
	}
	
	public void setCorrect(String s) {
		correct.set(s);
	}
	
	public StringProperty correctProperty() {
		return correct;
	}
	
	public String getAttempts() {
		return attempts.get();
	}
	
	public void setAttempts(String s) {
		attempts.set(s);
	}
	
	public StringProperty attemptsProperty() {
		return attempts;
	}

}
