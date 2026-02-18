package isen.contactapp.util;

import isen.contactapp.model.Person;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public abstract class PersonChangeListener implements ChangeListener<Person> {

	@Override
	public void changed(ObservableValue<? extends Person> observable, Person oldValue, Person newValue) {
		handleNewValue(newValue);
	}

	public abstract void handleNewValue(Person newValue);

}
