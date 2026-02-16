package isen.contactapp;

import isen.contactapp.model.Person;
import isen.contactapp.model.PersonDao;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class App extends Application {

    private static Scene scene;

    private static PersonDao personDao;


    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("view/Main-page"), 640, 480);
        stage.setScene(scene);
        stage.setTitle("Contact App");
        stage.show();
    }
    
    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }


    @Override
    public void init(){

        personDao = new PersonDao("jdbc:sqlite:sqlite.db");

        try{

            Connection connection = DriverManager.getConnection("jdbc:sqlite:sqlite.db");
            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS person (idperson INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,lastname VARCHAR(45) NOT NULL,firstname VARCHAR(45) NOT NULL,nickname VARCHAR(45) NOT NULL,phone_number VARCHAR(15) NULL,address VARCHAR(200) NULL,email_address VARCHAR(150) NULL,birth_date DATE NULL); ");
            statement.executeUpdate("INSERT INTO person(lastname,firstname,nickname,phone_number,address,email_address,birth_date) VALUES ('Droulers','Aymeric','Riric','0612345678','42 bvd Vauban','aymeric.droulers@student.junia.com','2004-12-29');");
            connection.close();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }

        List<Person> personList = personDao.getAllPersons();
        System.out.println("personList.size(): " + personList.size());

    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}