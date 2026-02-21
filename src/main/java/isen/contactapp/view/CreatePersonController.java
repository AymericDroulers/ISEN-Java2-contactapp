package isen.contactapp.view;

import java.io.IOException;

import isen.contactapp.App;
import isen.contactapp.model.Person;
import isen.contactapp.model.PersonDao;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
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
    	
    	boolean missing =
                lastNameField.getText() == null || lastNameField.getText().isBlank() ||
                firstNameField.getText() == null || firstNameField.getText().isBlank() ||
                nicknameField.getText() == null || nicknameField.getText().isBlank() ||
                phoneNumberField.getText() == null || phoneNumberField.getText().isBlank() ||
                addressField.getText() == null || addressField.getText().isBlank() ||
                emailAddressField.getText() == null || emailAddressField.getText().isBlank() ||
                dateField.getValue() == null;

        if (missing) {
            successMessage.setText("Fill all fields");
            successMessage.setStyle("-fx-text-fill: red;");
            successMessage.setVisible(true);
            return;
        }
        try {
            Person newPerson = new Person();

            newPerson.setLastName(lastNameField.getText());
            newPerson.setFirstName(firstNameField.getText());
            newPerson.setNickName(nicknameField.getText());
            newPerson.setAddress(addressField.getText());
            newPerson.setPhoneNumber(phoneNumberField.getText());
            newPerson.setBirthDate(dateField.getValue());
            newPerson.setEmailAddress(emailAddressField.getText());

            personDao.createPerson(newPerson);

            successMessage.setText("Person created successfully!");
            successMessage.setStyle("-fx-text-fill: green;");

            App.setRoot("/isen/contactapp/view/Main-page");

        } catch (IllegalArgumentException e) {

            successMessage.setText(e.getMessage());
            successMessage.setStyle("-fx-text-fill: red;");
            successMessage.setVisible(true);
        } catch (Exception e) {
            successMessage.setText("Unexpected error occurred.");
            successMessage.setStyle("-fx-text-fill: red;");
            e.printStackTrace();
            successMessage.setVisible(true);
        }
    }
    
    
    	
}
