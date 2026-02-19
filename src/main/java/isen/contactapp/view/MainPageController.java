package isen.contactapp.view;

import isen.contactapp.App;
import isen.contactapp.model.Person;
import isen.contactapp.model.PersonDao;
import isen.contactapp.util.PersonValueFactory;
import isen.contactapp.util.PersonChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

/**
 * Controller for the main page.
 * Handles display and navigation to CRUD pages.
 */
public class MainPageController {

    // ========== FXML Components ==========
    @FXML public AnchorPane formPane;
    @FXML public TextField lastNameField;
    @FXML public TextField firstNameField;
    @FXML public TextField nicknameField;
    @FXML public TextField addressField;
    @FXML public TextField emailAddressField;
    @FXML public TextField phoneNumberField;
    @FXML public DatePicker dateField;
    @FXML public TableView<Person> personTable;
    @FXML public TableColumn<Person, String> personColumn;

    // ========== Private Fields ==========
    private Person currentPerson;
    private final PersonDao personDao = new PersonDao("jdbc:sqlite:sqlite.db");

    // ========== Initialization ==========
    
    /**
     * Initializes the controller.
     * Called automatically after FXML loading.
     */
    @FXML
    private void initialize() {
        // Configure table column
        personColumn.setCellValueFactory(new PersonValueFactory());
        
        // Load data
        populateList();
        
        // Setup selection listener
        personTable.getSelectionModel()
            .selectedItemProperty()
            .addListener(new PersonChangeListener() {
                @Override
                public void handleNewValue(Person newValue) {
                    showPersonDetails(newValue);
                }
            });
        
        // Initial state
        resetView();
    }

    // ========== Button Handlers ==========
    
    /**
     * Opens the create person page.
     */
    @FXML
    private void handleCreatePersonButton() {
        try {
            App.setRoot("/isen/contactapp/view/createPerson");
        } catch (IOException e) {
            showAlert("Navigation Error", "Failed to open create person page: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Opens the edit person page for the selected person.
     */
    @FXML
    private void handleSaveButton() {
        if (currentPerson == null) {
            showAlert("No Selection", "Please select a person to edit.");
            return;
        }
        
        try {
            // Pass the selected person to the edit page
            App.setSelectedPerson(currentPerson);
            App.setRoot("/isen/contactapp/view/edit-person");
        } catch (IOException e) {
            showAlert("Navigation Error", "Failed to open edit page: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Deletes the selected person (not implemented yet).
     */
    @FXML
    private void handleDeleteButton() {
        showAlert("Not Implemented", "Delete functionality will be added later.");
    }

    // ========== View Management ==========
    
    /**
     * Displays the details of the selected person in the form.
     * @param person The person to display, or null to hide the form
     */
    @FXML
    private void showPersonDetails(Person person) {
        if (person == null) {
            formPane.setVisible(false);
        } else {
            formPane.setVisible(true);
            currentPerson = person;
            
            // Populate fields with person data (read-only display)
            lastNameField.setText(currentPerson.getLastName());
            firstNameField.setText(currentPerson.getFirstName());
            nicknameField.setText(currentPerson.getNickName());
            addressField.setText(currentPerson.getAddress());
            emailAddressField.setText(currentPerson.getEmail_address());
            phoneNumberField.setText(currentPerson.getPhone_number());
            dateField.setValue(currentPerson.getBirth_date());
            
            // Make fields read-only (display only)
            setFieldsEditable(false);
        }
    }

    /**
     * Resets the view to initial state (no selection).
     */
    @FXML
    private void resetView() {
        showPersonDetails(null);
        personTable.refresh();
    }

    /**
     * Sets whether the form fields are editable.
     * @param editable true to allow editing, false to disable
     */
    private void setFieldsEditable(boolean editable) {
        lastNameField.setEditable(editable);
        firstNameField.setEditable(editable);
        nicknameField.setEditable(editable);
        addressField.setEditable(editable);
        emailAddressField.setEditable(editable);
        phoneNumberField.setEditable(editable);
        dateField.setEditable(editable);
    }

    // ========== Data Management ==========
    
    /**
     * Populates the table with all persons from the database.
     */
    @FXML
    private void populateList() {
        try {
            personTable.setItems(personDao.getAllPersons());
            personTable.refresh();
        } catch (Exception e) {
            showAlert("Load Failed", "Could not load person list: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Updates the person list (same as populateList).
     */
    @FXML
    public void updatePersonList() {
        populateList();
    }

    /**
     * Refreshes the table display.
     */
    @FXML
    private void refreshList() {
        personTable.refresh();
        personTable.getSelectionModel().clearSelection();
    }

    // ========== Alert Dialogs ==========
    
    /**
     * Displays an alert dialog with the given title and message.
     * @param title Dialog title
     * @param message Dialog message
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}