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
}
