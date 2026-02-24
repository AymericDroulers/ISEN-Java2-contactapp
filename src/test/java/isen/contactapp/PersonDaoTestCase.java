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

/**
 * Unit tests for PersonDao.
 * Tests all CRUD operations with various scenarios.
 */
public class PersonDaoTestCase {

    private final PersonDao personDao = new PersonDao("jdbc:sqlite:sqlitetest.db");

    /**
     * Sets up the test database before each test.
     * Creates the person table, deletes all data, and inserts test data.
     */
    @BeforeEach
    public void setUp() throws Exception {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:sqlitetest.db");
             Statement statement = connection.createStatement()) {
            
            statement.executeUpdate("DROP TABLE IF EXISTS person");
            
            statement.executeUpdate(
                "CREATE TABLE person (" +
                "idperson INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "lastname VARCHAR(45) NOT NULL," +
                "firstname VARCHAR(45) NOT NULL," +
                "nickname VARCHAR(45) NOT NULL," +
                "phone_number VARCHAR(15) NULL," +
                "address VARCHAR(200) NULL," +
                "email_address VARCHAR(150) NULL," +
                "birth_date DATE NULL," +
                "category VARCHAR(20) DEFAULT 'Other'" +
                ");"
            );
            
            statement.executeUpdate(
                "INSERT INTO person(lastname, firstname, nickname, phone_number, address, email_address, birth_date, category) " +
                "VALUES ('Droulers', 'Aymeric', 'Riric', '0612345678', '42 bvd Vauban', 'aymeric.droulers@student.junia.com', '2004-12-29', 'Friend');"
            );
            
            statement.executeUpdate(
                "INSERT INTO person(lastname, firstname, nickname, phone_number, address, email_address, birth_date, category) " +
                "VALUES ('Dupont', 'Lucas', 'Ludu', '0612345678', '43 bvd Vauban', 'ludu@ik.me', '1985-06-06', 'Work');"
            );
        }
    }

    /**
     * Tests getAllPersons().
     * Verifies that all persons are retrieved correctly.
     */
    @Test
    public void shouldListPersons() {
        List<Person> persons = personDao.getAllPersons();

        assertThat(persons).hasSize(2);
        assertThat(persons).extracting(
            Person::getLastName,
            Person::getFirstName,
            Person::getNickName,
            Person::getPhoneNumber,
            Person::getAddress,
            Person::getEmailAddress,
            Person::getBirthDate,
            Person::getCategory
        ).containsOnly(
            tuple("Droulers", "Aymeric", "Riric", "0612345678", "42 bvd Vauban", 
                  "aymeric.droulers@student.junia.com", LocalDate.of(2004, 12, 29), "Friend"),
            tuple("Dupont", "Lucas", "Ludu", "0612345678", "43 bvd Vauban", 
                  "ludu@ik.me", LocalDate.of(1985, 6, 6), "Work")
        );
    }

    /**
     * Tests createPerson().
     * Verifies that a new person is created in the database.
     */
    @Test
    public void shouldCreatePerson() throws SQLException {
        Person person = new Person(0, "Scheving", "Hekla", "Hekli", "83902302", 
                                   "Lille", "hekla@gmail.com", LocalDate.of(2001, 9, 22), "Family");
        personDao.createPerson(person);

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:sqlitetest.db");
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM person WHERE lastname='Scheving'")) {
            
            assertThat(resultSet.next()).isTrue();
            assertThat(resultSet.getInt("idPerson")).isNotNull();
            assertThat(resultSet.getString("firstname")).isEqualTo("Hekla");
            assertThat(resultSet.getString("category")).isEqualTo("Family");
            assertThat(resultSet.next()).isFalse();
        }
    }

    /**
     * Tests updatePerson().
     * Verifies that an existing person is updated correctly.
     */
    @Test
    public void shouldUpdatePerson() throws SQLException {
        List<Person> persons = personDao.getAllPersons();
        Person personToUpdate = persons.get(0);
        int originalId = personToUpdate.getIdPerson();

        personToUpdate.setFirstName("Aymeric-Updated");
        personToUpdate.setNickName("Riric2");
        personToUpdate.setPhoneNumber("0698765432");
        personToUpdate.setEmailAddress("aymeric.updated@junia.com");
        personToUpdate.setCategory("Family");

        personDao.updatePerson(personToUpdate);

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:sqlitetest.db");
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM person WHERE idperson=" + originalId)) {
            
            assertThat(resultSet.next()).isTrue();
            assertThat(resultSet.getInt("idperson")).isEqualTo(originalId);
            assertThat(resultSet.getString("firstname")).isEqualTo("Aymeric-Updated");
            assertThat(resultSet.getString("nickname")).isEqualTo("Riric2");
            assertThat(resultSet.getString("phone_number")).isEqualTo("0698765432");
            assertThat(resultSet.getString("email_address")).isEqualTo("aymeric.updated@junia.com");
            assertThat(resultSet.getString("category")).isEqualTo("Family");
            assertThat(resultSet.getString("lastname")).isEqualTo("Droulers");
            assertThat(resultSet.next()).isFalse();
        }
    }

    /**
     * Tests updatePerson() with null values.
     * Verifies that optional fields can be set to null.
     */
    @Test
    public void shouldUpdatePersonWithNullValues() throws SQLException {
        List<Person> persons = personDao.getAllPersons();
        Person personToUpdate = persons.get(1);
        int originalId = personToUpdate.getIdPerson();

        personToUpdate.setPhoneNumber(null);
        personToUpdate.setAddress(null);
        personToUpdate.setEmailAddress(null);
        personToUpdate.setBirthDate(null);

        personDao.updatePerson(personToUpdate);

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:sqlitetest.db");
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM person WHERE idperson=" + originalId)) {
            
            assertThat(resultSet.next()).isTrue();
            assertThat(resultSet.getString("phone_number")).isNull();
            assertThat(resultSet.getString("address")).isNull();
            assertThat(resultSet.getString("email_address")).isNull();
            assertThat(resultSet.getDate("birth_date")).isNull();

            assertThat(resultSet.getString("lastname")).isEqualTo("Dupont");
            assertThat(resultSet.getString("firstname")).isEqualTo("Lucas");
            assertThat(resultSet.getString("nickname")).isEqualTo("Ludu");
            assertThat(resultSet.getString("category")).isEqualTo("Work");
        }
    }

    /**
     * Tests updatePerson() isolation.
     * Verifies that only the targeted person is updated.
     */
    @Test
    public void shouldUpdateOnlyTargetedPerson() throws SQLException {
        List<Person> persons = personDao.getAllPersons();
        Person person1 = persons.get(0);
        Person person2 = persons.get(1);

        person1.setFirstName("Modified");
        personDao.updatePerson(person1);

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:sqlitetest.db");
             Statement statement = connection.createStatement()) {
            
            try (ResultSet resultSet1 = statement.executeQuery("SELECT * FROM person WHERE idperson=" + person1.getIdPerson())) {
                assertThat(resultSet1.next()).isTrue();
                assertThat(resultSet1.getString("firstname")).isEqualTo("Modified");
            }

            try (ResultSet resultSet2 = statement.executeQuery("SELECT * FROM person WHERE idperson=" + person2.getIdPerson())) {
                assertThat(resultSet2.next()).isTrue();
                assertThat(resultSet2.getString("firstname")).isEqualTo("Lucas");
            }
        }
    }

    /**
     * Tests updatePerson() with non-existent ID.
     * Verifies that an exception is thrown.
     */
    @Test
    public void shouldThrowExceptionWhenUpdatingNonExistentPerson() {
        Person fakePerson = new Person(99999, "Ghost", "Phantom", "Boo", null, null, null, null, "Other");

        org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () -> {
            personDao.updatePerson(fakePerson);
        });
    }

    /**
     * Tests updatePerson() multiple times.
     * Verifies that multiple consecutive updates are applied correctly.
     */
    @Test
    public void shouldUpdatePersonMultipleTimes() throws SQLException {
        List<Person> persons = personDao.getAllPersons();
        Person person = persons.get(0);
        int originalId = person.getIdPerson();

        person.setPhoneNumber("0611111111");
        personDao.updatePerson(person);

        person.setAddress("New Address 1");
        personDao.updatePerson(person);

        person.setEmailAddress("new.email@test.com");
        personDao.updatePerson(person);

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:sqlitetest.db");
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM person WHERE idperson=" + originalId)) {
            
            assertThat(resultSet.next()).isTrue();
            assertThat(resultSet.getString("phone_number")).isEqualTo("0611111111");
            assertThat(resultSet.getString("address")).isEqualTo("New Address 1");
            assertThat(resultSet.getString("email_address")).isEqualTo("new.email@test.com");
        }
    }

    /**
     * Tests getPersonById() with valid ID.
     * Verifies that the correct person is retrieved.
     */
    @Test
    public void shouldGetPersonById() {
        List<Person> persons = personDao.getAllPersons();
        Person expectedPerson = persons.get(0);
        int targetId = expectedPerson.getIdPerson();

        Person retrievedPerson = personDao.getPersonById(targetId);

        assertThat(retrievedPerson).isNotNull();
        assertThat(retrievedPerson.getIdPerson()).isEqualTo(targetId);
        assertThat(retrievedPerson.getLastName()).isEqualTo("Droulers");
        assertThat(retrievedPerson.getFirstName()).isEqualTo("Aymeric");
        assertThat(retrievedPerson.getNickName()).isEqualTo("Riric");
        assertThat(retrievedPerson.getCategory()).isEqualTo("Friend");
    }

    /**
     * Tests getPersonById() with non-existent ID.
     * Verifies that null is returned.
     */
    @Test
    public void shouldReturnNullWhenPersonNotFound() {
        Person person = personDao.getPersonById(99999);

        assertThat(person).isNull();
    }

    /**
     * Tests deletePerson().
     * Verifies that a person is deleted from the database.
     */
    @Test
    public void shouldDeletePerson() throws SQLException {
        List<Person> persons = personDao.getAllPersons();
        Person personToDelete = persons.get(0);
        int idToDelete = personToDelete.getIdPerson();

        personDao.deletePerson(idToDelete);

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:sqlitetest.db");
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM person WHERE idperson=" + idToDelete)) {
            
            assertThat(resultSet.next()).isFalse();
        }

        Person deleted = personDao.getPersonById(idToDelete);
        assertThat(deleted).isNull();
    }

    /**
     * Tests deletePerson() with non-existent ID.
     * Verifies that an exception is thrown.
     */
    @Test
    public void shouldThrowExceptionWhenDeletingNonExistentPerson() {
        org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () -> {
            personDao.deletePerson(99999);
        });
    }
    
    /**
     * Tests category default value.
     * Verifies that category defaults to "Other" when null.
     */
    @Test
    public void shouldDefaultCategoryToOther() throws SQLException {
        Person person = new Person(0, "Test", "User", "Testy", null, null, null, null, null);
        Person created = personDao.createPerson(person);

        assertThat(created.getCategory()).isEqualTo("Other");

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:sqlitetest.db");
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM person WHERE lastname='Test'")) {
            
            assertThat(resultSet.next()).isTrue();
            assertThat(resultSet.getString("category")).isEqualTo("Other");
        }
    }
    
    /**
     * Tests category update.
     * Verifies that category can be updated to all valid values.
     */
    @Test
    public void shouldUpdateCategory() throws SQLException {
        List<Person> persons = personDao.getAllPersons();
        Person person = persons.get(0);
        int originalId = person.getIdPerson();

        String[] categories = {"Friend", "Family", "Work", "Other"};
        
        for (String category : categories) {
            person.setCategory(category);
            personDao.updatePerson(person);

            try (Connection connection = DriverManager.getConnection("jdbc:sqlite:sqlitetest.db");
                 Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery("SELECT category FROM person WHERE idperson=" + originalId)) {
                
                assertThat(resultSet.next()).isTrue();
                assertThat(resultSet.getString("category")).isEqualTo(category);
            }
        }
    }
}