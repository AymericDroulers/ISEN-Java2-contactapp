package isen.contactapp.view;

import java.io.IOException;
import isen.contactapp.App;
import isen.contactapp.model.Person;
import isen.contactapp.model.PersonDao;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 * Controller for the create person page.
 * Handles creation of new persons.
 */
public class CreatePersonController {
    
    @FXML public AnchorPane formPane;
    @FXML public TextField lastNameField;
    @FXML public TextField firstNameField;
    @FXML public TextField nicknameField;
    @FXML public TextField addressField;
    @FXML public TextField phoneNumberField;
    @FXML public TextField emailAddressField;
    @FXML public DatePicker dateField;
    @FXML public ComboBox<String> categoryField;
    @FXML public Label successMessage;
    
    public Person currentPerson;
    
    private final PersonDao personDao = new PersonDao("jdbc:sqlite:sqlite.db");
  
    /**
     * Saves the new person to the database.
     */
    @FXML
    private void handleSavePersonButton() {
        
        boolean missingRequired =
            lastNameField.getText() == null || lastNameField.getText().isBlank() ||
            firstNameField.getText() == null || firstNameField.getText().isBlank() ||
            nicknameField.getText() == null || nicknameField.getText().isBlank();
        
        if (missingRequired) {
            successMessage.setText("Last name, first name, and nickname are required!");
            successMessage.setStyle("-fx-text-fill: red;");
            successMessage.setVisible(true);
            return;
        }
        
        try {
            Person newPerson = new Person();
            newPerson.setLastName(lastNameField.getText().trim());
            newPerson.setFirstName(firstNameField.getText().trim());
            newPerson.setNickName(nicknameField.getText().trim());
            newPerson.setAddress(addressField.getText().trim());
            newPerson.setPhoneNumber(phoneNumberField.getText().trim());
            newPerson.setEmailAddress(emailAddressField.getText().trim());
            newPerson.setBirthDate(dateField.getValue());
            
            String category = categoryField.getValue();
            if (category == null || category.isEmpty()) {
                category = "Other";
            }
            newPerson.setCategory(category);
            
            personDao.createPerson(newPerson);
            
            successMessage.setText("Person created successfully!");
            successMessage.setStyle("-fx-text-fill: green;");
            successMessage.setVisible(true);
            
            App.setRoot("/isen/contactapp/view/Main-page");
            
        } catch (IllegalArgumentException e) {
            successMessage.setText(e.getMessage());
            successMessage.setStyle("-fx-text-fill: red;");
            successMessage.setVisible(true);
        } catch (Exception e) {
            successMessage.setText("Unexpected error occurred.");
            successMessage.setStyle("-fx-text-fill: red;");
            successMessage.setVisible(true);
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
     * Initializes the controller.
     */
    @FXML
    private void initialize() {
        categoryField.getItems().addAll("Friend", "Family", "Work", "Other");
    }
}