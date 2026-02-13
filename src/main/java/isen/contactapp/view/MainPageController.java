package isen.contactapp.view;

import isen.contactapp.model.Person;
import isen.contactapp.model.PersonDao;
import javafx.fxml.FXML;

import java.util.List;

public class MainPageController {


    private final PersonDao personDao = new PersonDao("jdbc:sqlite:sqlite.db");
    @FXML
    public List<Person> personList;


    @FXML
    public void updatePersonList(){
        this.personList= personDao.getAllPersons();
    }
}
