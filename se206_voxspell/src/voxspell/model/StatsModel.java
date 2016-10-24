package voxspell.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Wrapper class for handling statistics because StringProperties
 * and ObservableLists cannot be Serialized
 * @author terran
 *
 */
public class StatsModel {
	//EMULATES WORD BUT NOT SERIALIZABLE
	//NOT SURE IF THERES A BETTER WAY FOR THIS
	private SimpleStringProperty word;
	private SimpleStringProperty correct;
	private SimpleStringProperty attempts;
	private SimpleStringProperty accuracy;
	private SimpleStringProperty lastTried;
	
	public StatsModel(WordModel w) {
		word = new SimpleStringProperty(w.getWord());
		correct = new SimpleStringProperty(w.getTimesCorrect() + "");
		attempts = new SimpleStringProperty(w.getTotalAttempts() + "");
		accuracy = new SimpleStringProperty(w.getAccuracy() + "");
		lastTried = new SimpleStringProperty(w.getLastTriedStr() + ""); //try to format later 
	}
	
	//Getters, setters, and StringProperty getters are below
	//General table related content referenced from
	//http://code.makery.ch/blog/javafx-8-tableview-sorting-filtering/
	//http://code.makery.ch/library/javafx-8-tutorial/part2/
	
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
	
	public String getAccuracy() {
		return accuracy.get();
	}
	
	public void setAccuracy(String s) {
		accuracy.set(s);
	}
	
	public StringProperty accuracyProperty() {
		return accuracy;
	}

	public String getLastTried() {
		return lastTried.get();
	}
	
	public void setLastTried(String s) {
		lastTried.set(s);
	}
	
	public StringProperty lastTriedProperty() {
		return lastTried;
	}
}
