package isen.contactapp.model;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PersonDao {

    private final String url;

    public PersonDao(String url) {
        this.url = url;
    }
  
    public Person createPerson(Person person) {
    	try(Connection connection=DriverManager.getConnection(url)){
			String query="INSERT INTO person(lastname,firstname,nickname,phone_number,address,email_address, birth_date) VALUES(?,?,?,?,?,?,?)";
			try(PreparedStatement statement=connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){
				
				statement.setString(1,person.getLastName());
				statement.setString(2,person.getFirstName() );
				statement.setString(3,person.getNickName());
				statement.setString(4, person.getPhone_number());
				statement.setString(5, person.getAddress());
				statement.setString(6, person.getEmail_address());
				statement.setDate(7, Date.valueOf(person.getBirth_date()));
				statement.executeUpdate();
				try (ResultSet ids = statement.getGeneratedKeys()) {
				if(ids.next()) {
					return new Person(ids.getInt(1), person.getLastName(), person.getFirstName(), person.getNickName(), person.getPhone_number(), person.getAddress(), person.getEmail_address(), person.getBirth_date());
				}}
			} return new Person();
					
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to create person", e);
		}
		
	}



    public List<Person> getAllPersons(){
        List<Person> persons = new ArrayList<>();
        try(Connection connection = DriverManager.getConnection(url)){
            try(Statement statement = connection.createStatement()){
                try(ResultSet results = statement.executeQuery("SELECT  * FROM person")){
                    while(results.next()){
                        System.out.println(results.getInt("idperson"));
                        persons.add(new Person(
                            results.getInt("idperson"),
                                results.getString("lastname"),
                                results.getString("firstname"),
                                results.getString("nickname"),
                                results.getString("phone_number"),
                                results.getString("address"),
                                results.getString("email_address"),
                                results.getObject("birth_date", LocalDate.class)

                        ));
                    }
            }
        }
            return persons;
    }catch(SQLException e){
        throw new RuntimeException("Database error",e);}
    }
    
    
    
    

    public void updatePerson(Person person) {
        String query = "UPDATE person SET lastname = ?, firstname = ?, nickname = ?, " +
                       "phone_number = ?, address = ?, email_address = ?, birth_date = ? " +
                       "WHERE idperson = ?";
        
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setString(1, person.getLastName());
            statement.setString(2, person.getFirstName());
            statement.setString(3, person.getNickName());
            statement.setString(4, person.getPhone_number());
            statement.setString(5, person.getAddress());
            statement.setString(6, person.getEmail_address());
            
          
            if (person.getBirth_date() != null) {
                statement.setDate(7, Date.valueOf(person.getBirth_date()));
            } else {
                statement.setNull(7, Types.DATE);
            }
            
            statement.setInt(8, person.getIdPerson());
            
            int rowsAffected = statement.executeUpdate();
            
            if (rowsAffected == 0) {
                throw new RuntimeException("Update failed - person not found with ID: " + person.getIdPerson());
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update person: " + e.getMessage(), e);
        }
    }
    
    
    
    
    
    
    public Person getPersonById(int id) {
        String query = "SELECT * FROM person WHERE idperson = ?";
        
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setInt(1, id);
            
            try (ResultSet results = statement.executeQuery()) {
                if (results.next()) {
                    return new Person(
                        results.getInt("idperson"),
                        results.getString("lastname"),
                        results.getString("firstname"),
                        results.getString("nickname"),
                        results.getString("phone_number"),
                        results.getString("address"),
                        results.getString("email_address"),
                        results.getObject("birth_date", LocalDate.class)
                    );
                }
                return null;
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get person by ID: " + e.getMessage(), e);
        }
    }
    
    
    
    
}
