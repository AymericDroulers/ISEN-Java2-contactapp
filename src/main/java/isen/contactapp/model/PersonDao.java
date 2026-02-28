package isen.contactapp.model;

import java.sql.*;
import java.time.LocalDate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Data Access Object for Person entity.
 * Handles all database operations (CRUD) for persons.
 */
public class PersonDao {

    private final String url;

    /**
     * Constructor.
     * @param url JDBC connection URL
     */
    public PersonDao(String url) {
        this.url = url;
    }
  
    /**
     * Creates a new person in the database.
     * 
     * @param person The person to create
     * @return The person with generated ID
     */
    public Person createPerson(Person person) {
        String query = "INSERT INTO person(" +
                       "lastname, firstname, nickname, phone_number, address, " +
                       "email_address, birth_date, category" +
                       ") VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            setStatementValues(person, statement);
            String category = person.getCategory();
            if (category == null || category.isBlank()) {
                category = "Other";
            }

            statement.executeUpdate();
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);

                    return new Person(
                        generatedId,
                        person.getLastName(),
                        person.getFirstName(),
                        person.getNickName(),
                        person.getPhoneNumber(),
                        person.getAddress(),
                        person.getEmailAddress(),
                        person.getBirthDate(),
                        category
                    );
                }
            }
            
            throw new RuntimeException("Failed to get generated ID");
            
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create person: " + e.getMessage(), e);
        }
    }


    /**
     * Retrieves all persons from the database.
     * 
     * @return An ObservableList containing all persons
     */
    public ObservableList<Person> getAllPersons() {
        ObservableList<Person> persons = FXCollections.observableArrayList();
        String query = "SELECT * FROM person";
        
        try (Connection connection = DriverManager.getConnection(url);
             Statement statement = connection.createStatement();
             ResultSet results = statement.executeQuery(query)) {
            
            while (results.next()) {
                int idPerson = results.getInt("idperson");
                String lastName = results.getString("lastname");
                String firstName = results.getString("firstname");
                String nickName = results.getString("nickname");
                String phoneNumber = results.getString("phone_number");
                String address = results.getString("address");
                String emailAddress = results.getString("email_address");
                LocalDate birthDate = results.getObject("birth_date", LocalDate.class);
                
                String category = results.getString("category");
                if (category == null || category.isBlank()) {
                    category = "Other";
                }
                
                Person person = new Person(
                    idPerson,
                    lastName,
                    firstName,
                    nickName,
                    phoneNumber,
                    address,
                    emailAddress,
                    birthDate,
                    category
                );
                
                persons.add(person);
            }
            
            return persons;
            
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get all persons: " + e.getMessage(), e);
        }
    }

    /**
     * Updates an existing person in the database.
     * 
     * @param person The person to update (must have valid ID)
     * @throws RuntimeException if person not found
     */
    public void updatePerson(Person person) {
        String query = "UPDATE person SET " +
                       "lastname = ?, firstname = ?, nickname = ?, " +
                       "phone_number = ?, address = ?, email_address = ?, " +
                       "birth_date = ?, category = ? " +
                       "WHERE idperson = ?";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(query)) {

            setStatementValues(person, statement);

            statement.setInt(9, person.getIdPerson());

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected == 0) {
                throw new RuntimeException("Update failed - person not found with ID: " + person.getIdPerson());
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to update person: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves a person by ID.
     * 
     * @param id The person ID
     * @return The person, or null if not found
     */
    public Person getPersonById(int id) {
        String query = "SELECT * FROM person WHERE idperson = ?";
        
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setInt(1, id);
            
            try (ResultSet results = statement.executeQuery()) {
                if (results.next()) {
                    String category = results.getString("category");
                    if (category == null || category.isBlank()) {
                        category = "Other";
                    }
                    
                    return new Person(
                        results.getInt("idperson"),
                        results.getString("lastname"),
                        results.getString("firstname"),
                        results.getString("nickname"),
                        results.getString("phone_number"),
                        results.getString("address"),
                        results.getString("email_address"),
                        results.getObject("birth_date", LocalDate.class),
                        category
                    );
                }
                
                return null;
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get person by ID: " + e.getMessage(), e);
        }
    }
    
    /**
     * Deletes a person from the database.
     * 
     * @param id The ID of the person to delete
     * @throws RuntimeException if person not found
     */
    public void deletePerson(int id) {
        String query = "DELETE FROM person WHERE idperson = ?";
        
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setInt(1, id);
            int rowsAffected = statement.executeUpdate();
            
            if (rowsAffected == 0) {
                throw new RuntimeException("Delete failed - person not found with ID: " + id);
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete person: " + e.getMessage(), e);
        }
    }


    /**
     * Set the values of a create or update statement from Person object
     *
     * @param person the person data
     * @param statement the create or update statement
     * */
    private void setStatementValues(Person person, PreparedStatement statement) throws SQLException {
        statement.setString(1, person.getLastName());
        statement.setString(2, person.getFirstName());
        statement.setString(3, person.getNickName());

        if (person.getPhoneNumber() != null && !person.getPhoneNumber().isBlank()) {
            statement.setString(4, person.getPhoneNumber());
        } else {
            statement.setNull(4, Types.VARCHAR);
        }

        if (person.getAddress() != null && !person.getAddress().isBlank()) {
            statement.setString(5, person.getAddress());
        } else {
            statement.setNull(5, Types.VARCHAR);
        }

        if (person.getEmailAddress() != null && !person.getEmailAddress().isBlank()) {
            statement.setString(6, person.getEmailAddress());
        } else {
            statement.setNull(6, Types.VARCHAR);
        }

        if (person.getBirthDate() != null) {
            statement.setDate(7, Date.valueOf(person.getBirthDate()));
        } else {
            statement.setNull(7, Types.DATE);
        }

        String category = person.getCategory();
        if (category == null || category.isBlank()) {
            category = "Other";
        }
        statement.setString(8, category);
    }

}