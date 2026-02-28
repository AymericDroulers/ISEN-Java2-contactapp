package isen.contactapp.util;

import isen.contactapp.model.Person;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

public class PersonFirstNameValueFactory
		implements Callback<CellDataFeatures<Person, String>, ObservableValue<String>> {

	@Override
	public ObservableValue<String> call(CellDataFeatures<Person, String> cellData) {
		return new SimpleStringProperty(cellData.getValue().getFirstName());
	}
}

