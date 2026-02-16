package isen.contactapp;

import isen.contactapp.model.Person;
import isen.contactapp.model.PersonDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

public class PersonDaoTestCase {

    private final PersonDao personDao = new PersonDao("jdbc:sqlite:sqlitetest.db");

    @BeforeEach
    public void setUp() throws Exception {



        Connection connection = DriverManager.getConnection("jdbc:sqlite:sqlitetest.db");
        Statement statement = connection.createStatement();
        statement.executeUpdate("DELETE FROM person");
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS person (idperson INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,lastname VARCHAR(45) NOT NULL,firstname VARCHAR(45) NOT NULL,nickname VARCHAR(45) NOT NULL,phone_number VARCHAR(15) NULL,address VARCHAR(200) NULL,email_address VARCHAR(150) NULL,birth_date DATE NULL); ");
        statement.executeUpdate("INSERT INTO person(lastname,firstname,nickname,phone_number,address,email_address,birth_date) VALUES ('Droulers','Aymeric','Riric','0612345678','42 bvd Vauban','aymeric.droulers@student.junia.com','2004-12-29');");
        statement.executeUpdate("INSERT INTO person(lastname,firstname,nickname,phone_number,address,email_address,birth_date) VALUES ('Dupont','Lucas','Ludu','0612345678','43 bvd Vauban','ludu@ik.me','1985-06-06');");
        statement.close();
        connection.close();
    }

    @Test
    public void shouldListPersons(){
        //WHEN
        List<Person> persons = personDao.getAllPersons();

        //THEN
        assertThat(persons).hasSize(2);
        assertThat(persons).extracting(Person::getLastName,Person::getFirstName,Person::getNickName, Person::getPhone_number, Person::getAddress, Person::getEmail_address,Person::getBirth_date).containsOnly(
                tuple("Droulers","Aymeric","Riric","0612345678","42 bvd Vauban","aymeric.droulers@student.junia.com", LocalDate.of(2004,12,29)),
                tuple("Dupont","Lucas","Ludu","0612345678","43 bvd Vauban","ludu@ik.me",LocalDate.of(1985,6,6))
        );

    }
}
