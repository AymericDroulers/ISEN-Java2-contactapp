package isen.contactapp.view;

import isen.contactapp.App;
import isen.contactapp.model.Person;
import isen.contactapp.model.PersonDao;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.io.IOException;

/**
 * Controller for editing an existing person.
 */
public class EditPersonController {
    
    @FXML private TextField lastNameField;
    @FXML private TextField firstNameField;
    @FXML private TextField nicknameField;
    @FXML private TextField phoneNumberField;
    @FXML private TextField addressField;
    @FXML private TextField emailAddressField;
    @FXML private DatePicker dateField;
    
    private Person person;
    private final PersonDao personDao = new PersonDao("jdbc:sqlite:sqlite.db");
    
    /**
     * Sets the person to edit and fills the form with their data.
     */
    public void setPerson(Person person) {
        this.person = person;
        
        if (person != null) {
            lastNameField.setText(person.getLastName());
            firstNameField.setText(person.getFirstName());
            nicknameField.setText(person.getNickName());
            phoneNumberField.setText(person.getPhoneNumber());
            addressField.setText(person.getAddress());
            emailAddressField.setText(person.getEmailAddress());
            dateField.setValue(person.getBirthDate());
        }
    }
    
    /**
     * Saves the edited person to the database.
     */
    @FXML
    private void handleSaveButton() {
        if (person == null) {
            showAlert("Error", "No person to edit.");
            return;
        }
        
        try {
            
            person.setLastName(lastNameField.getText().trim());
            person.setFirstName(firstNameField.getText().trim());
            person.setNickName(nicknameField.getText().trim());
            person.setPhoneNumber(phoneNumberField.getText().trim());
            person.setAddress(addressField.getText().trim());
            person.setEmailAddress(emailAddressField.getText().trim());
            person.setBirthDate(dateField.getValue());
            
            
            if (person.getLastName().isEmpty() || 
                person.getFirstName().isEmpty() || 
                person.getNickName().isEmpty()) {
                showAlert("Invalid Input", "Last name, first name, and nickname are required.");
                return;
            }
            
            personDao.updatePerson(person);
            showAlert("Success", person.getFullName() + " has been updated successfully!");
            
            App.setRoot("/isen/contactapp/view/Main-page");
            
        } catch (Exception e) {
            showAlert("Error", "Failed to update person: " + e.getMessage());
            e.printStackTrace();
        }
    }
    /**
     * Returns to the main page without saving.
     */
    @FXML
    private void handleBackButton() {
        try {
            App.setRoot("/isen/contactapp/view/Main-page");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Shows an alert dialog with the given title and message.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}