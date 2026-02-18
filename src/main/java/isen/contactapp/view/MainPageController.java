package isen.contactapp.view;

import isen.contactapp.App;
import isen.contactapp.model.Person;
import isen.contactapp.model.PersonDao;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import isen.contactapp.util.PersonValueFactory;
import isen.contactapp.util.PersonChangeListener;

import java.io.IOException;
import java.util.List;

public class MainPageController {
	
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
	public TextField emailAddressField;
	@FXML
	public TextField phoneNumberField;
	@FXML
	public DatePicker dateField;

	
	public Person currentPerson;


    private final PersonDao personDao = new PersonDao("jdbc:sqlite:sqlite.db");
    @FXML
    public List<Person> personList;
    @FXML
    public TableView<Person> personTable;
   
    @FXML
	public TableColumn<Person, String> personColumn;

    @FXML
    private void handleCreatePersonButton() {
    	 try {
             App.setRoot("/isen/contactapp/view/createPerson");
         } catch (IOException e) {
             e.printStackTrace();
         }
    }
    
    @FXML
    private void handleDeleteButton() {
    }
    
    @FXML
    private void handleSaveButton() {
    }
    
    @FXML
    private void refreshList() {
    	personTable.refresh();
    	personTable.setSelectionModel(null);
    }
    
    @FXML
    private void showPersonDetails(Person person) {
    	if(person==null) {
    		formPane.setVisible(false);
    	} else {
    		formPane.setVisible(true);
    		currentPerson=person;
    		lastNameField.setText(currentPerson.getLastName());
    		firstNameField.setText(currentPerson.getFirstName());
    		nicknameField.setText(currentPerson.getNickName());
    		addressField.setText(currentPerson.getAddress());
    		emailAddressField.setText(currentPerson.getEmail_address());
    		phoneNumberField.setText(currentPerson.getPhone_number());
    		dateField.setValue(currentPerson.getBirth_date());
    	}
    } 
    
    @FXML
    private void resetView() {
    	showPersonDetails(null);
    	personTable.refresh();
    	
    }
    @FXML
    public void updatePersonList(){
        this.personList= personDao.getAllPersons();
    }
    
    @FXML
    private void populateList() {
    	personTable.setItems(personDao.getAllPersons());
    	personTable.refresh();
    } 
    
    @FXML
	private void initialize() {
    	personColumn.setCellValueFactory(new PersonValueFactory());
    	populateList();
    	personTable.getSelectionModel()
        .selectedItemProperty()
        .addListener(new PersonChangeListener() {
            @Override
            public void handleNewValue(Person newValue) {
                showPersonDetails(newValue);
            }
        });

    resetView();}
    
}
