package isen.contactapp.view;

import java.io.IOException;
import java.util.List;

import isen.contactapp.App;
import isen.contactapp.model.Person;
import isen.contactapp.model.PersonDao;
import isen.contactapp.util.PersonValueFactory;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class CreatePersonController {
	@FXML
	public AnchorPane formPane;

	@FXML
	public TextField lastNameField;

	@FXML
	public TextField firstNameField;
	@FXML
	public TextField nicknameField;

	@FXML
	public TextField addressField;

	@FXML
	public TextField phoneNumberField;
	@FXML
	public DatePicker dateField;
	@FXML
	public TextField emailAddressField;

	
	public Person currentPerson;
	
	@FXML
	public Label successMessage;


    private final PersonDao personDao = new PersonDao("jdbc:sqlite:sqlite.db");
  

    @FXML
    private void handleSavePersonButton() {
    	Person newPerson=new Person();
    	newPerson.setLastName(lastNameField.getText());
    	newPerson.setFirstName(firstNameField.getText());
    	newPerson.setNickName(nicknameField.getText());
    	newPerson.setAddress(addressField.getText());
    	newPerson.setPhone_number(phoneNumberField.getText());
    	newPerson.setBirth_date(dateField.getValue());
    	newPerson.setEmail_address(emailAddressField.getText());
    	personDao.createPerson(newPerson);
    	
    	try {
			App.setRoot("/isen/contactapp/view/Main-page");
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    
    	
}
