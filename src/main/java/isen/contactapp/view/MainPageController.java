package isen.contactapp.view;

import isen.contactapp.App;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import isen.contactapp.model.Person;
import isen.contactapp.model.PersonDao;
import isen.contactapp.util.PersonValueFactory;
import isen.contactapp.util.PersonChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

/**
 * Controller for the main page.
 * Handles person listing, search, and in-place editing.
 */
public class MainPageController {

    @FXML public AnchorPane formPane;
    @FXML public TextField lastNameField;
    @FXML public TextField firstNameField;
    @FXML public TextField nicknameField;
    @FXML public TextField addressField;
    @FXML public TextField emailAddressField;
    @FXML public TextField phoneNumberField;
    @FXML public DatePicker dateField;
    @FXML public ComboBox<String> categoryField;
    @FXML public TableView<Person> personTable;
    @FXML public TableColumn<Person, String> personColumn;
    @FXML public TextField searchField;

    private Person currentPerson;
    private final PersonDao personDao = new PersonDao("jdbc:sqlite:sqlite.db");
    private ObservableList<Person> masterList = FXCollections.observableArrayList();
    private FilteredList<Person> filteredList;

    /**
     * Initializes the controller.
     * Called automatically after FXML loading.
     */
    @FXML
    private void initialize() {
        personColumn.setCellValueFactory(new PersonValueFactory());
        
        categoryField.getItems().addAll("Friend", "Family", "Work", "Other");
        
        populateList();
        
        filteredList = new FilteredList<>(masterList, person -> true);

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            String filter = (newVal == null) ? "" : newVal.toLowerCase().trim();

            filteredList.setPredicate(person -> {
                if (filter.isEmpty()) return true;

                String fn = person.getFirstName() == null ? "" : person.getFirstName().toLowerCase();
                String ln = person.getLastName() == null ? "" : person.getLastName().toLowerCase();

                return fn.contains(filter) || ln.contains(filter);
            });
        });
        
        personTable.setItems(filteredList);
        
        personTable.getSelectionModel()
            .selectedItemProperty()
            .addListener(new PersonChangeListener() {
                @Override
                public void handleNewValue(Person newValue) {
                    showPersonDetails(newValue);
                }
            });
        
        personTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        resetView();
    }

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
     * Saves the edited person to the database.
     */
    @FXML
    private void handleSaveButton() {
        if (currentPerson == null) {
            showAlert("No Selection", "Please select a person to edit.");
            return;
        }
        
        try {
            currentPerson.setLastName(lastNameField.getText().trim());
            currentPerson.setFirstName(firstNameField.getText().trim());
            currentPerson.setNickName(nicknameField.getText().trim());
            currentPerson.setPhoneNumber(phoneNumberField.getText().trim());
            currentPerson.setAddress(addressField.getText().trim());
            currentPerson.setEmailAddress(emailAddressField.getText().trim());
            currentPerson.setBirthDate(dateField.getValue());
            currentPerson.setCategory(categoryField.getValue());
            
            if (currentPerson.getLastName().isEmpty() || 
                currentPerson.getFirstName().isEmpty() || 
                currentPerson.getNickName().isEmpty()) {
                showAlert("Invalid Input", "Last name, first name, and nickname are required.");
                return;
            }
            
            personDao.updatePerson(currentPerson);
            
            showAlert("Success", currentPerson.getFullName() + " has been updated successfully!");
            categoryField.setValue(currentPerson.getCategory());
            
            populateList();
            
            filteredList = new FilteredList<>(masterList, person -> true);
            
        } catch (Exception e) {
            showAlert("Error", "Failed to update person: " + e.getMessage());
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

    /**
     * Displays the details of the selected person in the form.
     * 
     * @param person The person to display, or null to hide the form
     */
    @FXML
    private void showPersonDetails(Person person) {
        if (person == null) {
            formPane.setVisible(false);
        } else {
            formPane.setVisible(true);
            currentPerson = person;
            
            lastNameField.setText(currentPerson.getLastName());
            firstNameField.setText(currentPerson.getFirstName());
            nicknameField.setText(currentPerson.getNickName());
            addressField.setText(currentPerson.getAddress());
            emailAddressField.setText(currentPerson.getEmailAddress());
            phoneNumberField.setText(currentPerson.getPhoneNumber());
            dateField.setValue(currentPerson.getBirthDate());
            categoryField.setValue(currentPerson.getCategory());
            
            setFieldsEditable(true);
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
     * 
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
        categoryField.setDisable(!editable);
    }

    /**
     * Populates the table with all persons from the database.
     */
    @FXML
    private void populateList() {
        try {
            masterList.setAll(personDao.getAllPersons());
            personTable.refresh();
        } catch (Exception e) {
            showAlert("Load Failed", "Could not load person list: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Updates the person list.
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

    /**
     * Displays an alert dialog.
     * 
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