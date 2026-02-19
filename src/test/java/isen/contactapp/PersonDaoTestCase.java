package isen.contactapp;

import isen.contactapp.model.Person;
import isen.contactapp.model.PersonDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    @Test
    public void shouldCreatePerson() throws SQLException {
    	// WHEN
    	Person person=new Person(2, "Scheving", "Hekla", "Hekli", "83902302", "Lille", "hekla@gmail.com", LocalDate.of(2001, 9, 22));
		personDao.createPerson(person);
		// THEN
		Connection connection = DriverManager.getConnection("jdbc:sqlite:sqlitetest.db");
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT * FROM person WHERE lastname='Scheving'");
		assertThat(resultSet.next()).isTrue();
		assertThat(resultSet.getInt("idPerson")).isNotNull();
		assertThat(resultSet.getString("firstname")).isEqualTo("Hekla");
		assertThat(resultSet.next()).isFalse();
		resultSet.close();
		statement.close();
		connection.close();
    }
    
    @Test
    public void shouldUpdatePerson() throws SQLException {
        // GIVEN - Récupérer une personne existante
        List<Person> persons = personDao.getAllPersons();
        Person personToUpdate = persons.get(0); // Aymeric Droulers
        int originalId = personToUpdate.getIdPerson();
        
        // WHEN - Modifier la personne
        personToUpdate.setFirstName("Aymeric-Updated");
        personToUpdate.setNickName("Riric2");
        personToUpdate.setPhone_number("0698765432");
        personToUpdate.setEmail_address("aymeric.updated@junia.com");
        
        personDao.updatePerson(personToUpdate);
        
        // THEN - Vérifier en base de données
        Connection connection = DriverManager.getConnection("jdbc:sqlite:sqlitetest.db");
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM person WHERE idperson=" + originalId);
        
        assertThat(resultSet.next()).isTrue();
        assertThat(resultSet.getInt("idperson")).isEqualTo(originalId);
        assertThat(resultSet.getString("firstname")).isEqualTo("Aymeric-Updated");
        assertThat(resultSet.getString("nickname")).isEqualTo("Riric2");
        assertThat(resultSet.getString("phone_number")).isEqualTo("0698765432");
        assertThat(resultSet.getString("email_address")).isEqualTo("aymeric.updated@junia.com");
        assertThat(resultSet.getString("lastname")).isEqualTo("Droulers");
        assertThat(resultSet.next()).isFalse();
        
        resultSet.close();
        statement.close();
        connection.close();
    }

    @Test
    public void shouldUpdatePersonWithNullValues() throws SQLException {
        // GIVEN - Récupérer une personne existante
        List<Person> persons = personDao.getAllPersons();
        Person personToUpdate = persons.get(1); // Lucas Dupont
        int originalId = personToUpdate.getIdPerson();
        
        // WHEN - Mettre à jour avec des valeurs null
        personToUpdate.setPhone_number(null);
        personToUpdate.setAddress(null);
        personToUpdate.setEmail_address(null);
        personToUpdate.setBirth_date(null);
        
        personDao.updatePerson(personToUpdate);
        
        // THEN - Vérifier que les champs sont bien null
        Connection connection = DriverManager.getConnection("jdbc:sqlite:sqlitetest.db");
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM person WHERE idperson=" + originalId);
        
        assertThat(resultSet.next()).isTrue();
        assertThat(resultSet.getString("phone_number")).isNull();
        assertThat(resultSet.getString("address")).isNull();
        assertThat(resultSet.getString("email_address")).isNull();
        assertThat(resultSet.getDate("birth_date")).isNull();
        
        // Les champs requis restent présents
        assertThat(resultSet.getString("lastname")).isEqualTo("Dupont");  
        assertThat(resultSet.getString("firstname")).isEqualTo("Lucas");  
        assertThat(resultSet.getString("nickname")).isEqualTo("Ludu");
        
        resultSet.close();
        statement.close();
        connection.close();
    }

    @Test
    public void shouldUpdateOnlyTargetedPerson() throws SQLException {
        // GIVEN - Deux personnes en base
        List<Person> persons = personDao.getAllPersons();
        Person person1 = persons.get(0); // Aymeric
        Person person2 = persons.get(1); // Lucas
        
        // WHEN - Modifier seulement person1
        person1.setFirstName("Modified");
        personDao.updatePerson(person1);
        
        // THEN - Vérifier que seul person1 a changé
        Connection connection = DriverManager.getConnection("jdbc:sqlite:sqlitetest.db");
        Statement statement = connection.createStatement();
        
        // Vérifier person1 modifiée
        ResultSet resultSet1 = statement.executeQuery("SELECT * FROM person WHERE idperson=" + person1.getIdPerson());
        assertThat(resultSet1.next()).isTrue();
        assertThat(resultSet1.getString("firstname")).isEqualTo("Modified");
        resultSet1.close();
        
        // Vérifier person2 inchangée
        ResultSet resultSet2 = statement.executeQuery("SELECT * FROM person WHERE idperson=" + person2.getIdPerson());
        assertThat(resultSet2.next()).isTrue();
        assertThat(resultSet2.getString("firstname")).isEqualTo("Dupont"); // Inchangé
        resultSet2.close();
        
        statement.close();
        connection.close();
    }

    @Test
    public void shouldThrowExceptionWhenUpdatingNonExistentPerson() {
        // GIVEN - Une personne avec un ID inexistant
        Person fakePerson = new Person(99999, "Ghost", "Phantom", "Boo", null, null, null, null);
        
        // WHEN & THEN - Vérifier qu'une exception est levée
        org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () -> {
            personDao.updatePerson(fakePerson);
        });
    }

    @Test
    public void shouldUpdatePersonMultipleTimes() throws SQLException {
        // GIVEN - Une personne existante
        List<Person> persons = personDao.getAllPersons();
        Person person = persons.get(0);
        int originalId = person.getIdPerson();
        
        // WHEN - Plusieurs mises à jour successives
        person.setPhone_number("0611111111");
        personDao.updatePerson(person);
        
        person.setAddress("New Address 1");
        personDao.updatePerson(person);
        
        person.setEmail_address("new.email@test.com");
        personDao.updatePerson(person);
        
        // THEN - Vérifier que toutes les modifications sont présentes
        Connection connection = DriverManager.getConnection("jdbc:sqlite:sqlitetest.db");
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM person WHERE idperson=" + originalId);
        
        assertThat(resultSet.next()).isTrue();
        assertThat(resultSet.getString("phone_number")).isEqualTo("0611111111");
        assertThat(resultSet.getString("address")).isEqualTo("New Address 1");
        assertThat(resultSet.getString("email_address")).isEqualTo("new.email@test.com");
        
        resultSet.close();
        statement.close();
        connection.close();
    }
    

}
