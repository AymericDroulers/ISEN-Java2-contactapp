package isen.contactapp.model;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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



    public ObservableList<Person> getAllPersons(){
        ObservableList<Person> persons = FXCollections.observableArrayList();
        try(Connection connection = DriverManager.getConnection(url)){
            try(Statement statement = connection.createStatement()){
                try(ResultSet results = statement.executeQuery("SELECT  * FROM person")){
                    while(results.next()){
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
}
