package isen.contactapp.model;


import java.time.LocalDate;

public class Person {
    private int idPerson;
    private String lastName;
    private String firstName;
    private String nickName;
    private String phone_number;
    private String address;
    private String email_address;
    private LocalDate birth_date;


    public Person(int idPerson, String lastname, String firstname, String nickname, String phone_number, String address, String email_address, LocalDate birth_date) {
        this.idPerson = idPerson;
        this.lastName = lastname;
        this.firstName = firstname;
        this.nickName = nickname;
        this.phone_number = phone_number;
        this.address = address;
        this.email_address = email_address;
        this.birth_date = birth_date;
    }
    
    public Person(){}

    public int getIdPerson() {
        return idPerson;
    }

    public void setIdPerson(int idPerson) {
        this.idPerson = idPerson;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail_address() {
        return email_address;
    }

    public void setEmail_address(String email_address) {
        this.email_address = email_address;
    }

    public LocalDate getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(LocalDate birth_date) {
        this.birth_date = birth_date;
    }
}
