package isen.contactapp.model;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Person {
    private int idPerson;
    private String lastName;
    private String firstName;
    private String nickName;
    private String phoneNumber;
    private String address;
    private String emailAddress;
    private LocalDate birthDate;

    public Person(int idPerson, String lastname, String firstname, String nickname, String phoneNumber, String address, String emailAddress, LocalDate birthDate) {
        this.idPerson = idPerson;
        this.lastName = lastname;
        this.firstName = firstname;
        this.nickName = nickname;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.emailAddress = emailAddress;
        this.birthDate = birthDate;
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
        if(lastName == null || lastName.isBlank()){
            throw new IllegalArgumentException("lastName cannot be null or empty");
        }
        lastName = lastName.strip();
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        if(firstName == null || firstName.isBlank()){
            throw new IllegalArgumentException("firstName cannot be null or empty");
        }
        firstName = firstName.strip();
        this.firstName = firstName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        if(nickName == null || nickName.isBlank()){
            throw new IllegalArgumentException("nickName cannot be null or empty");
        }
        nickName = nickName.strip();
        this.nickName = nickName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    
    public void setPhoneNumber(String phoneNumber) {
        
        if(phoneNumber == null || phoneNumber.isBlank()){
            this.phoneNumber = null;
            return;
        }
        

        phoneNumber = phoneNumber.strip();
        Pattern pattern = Pattern.compile("^\\d{10}$");
        Matcher matcher = pattern.matcher(phoneNumber);
        if(!matcher.find()){
            throw new IllegalArgumentException("phone number is invalid (must be 10 digits)");
        }
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

   
    public void setAddress(String address) {
 
        if(address == null || address.isBlank()){
            this.address = null;
            return;
        }
        
        address = address.strip();
        this.address = address;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    
    public void setEmailAddress(String emailAddress) {
     
        if(emailAddress == null || emailAddress.isBlank()){
            this.emailAddress = null;  
            return;
        }
        
        
        emailAddress = emailAddress.trim();
        Pattern pattern = Pattern.compile("^[A-z]+\\.[A-z]+@[A-z]+\\.[A-z.]+$");
        Matcher matcher = pattern.matcher(emailAddress);
        if(!matcher.find()){
            throw new IllegalArgumentException("email address is invalid");
        }
        this.emailAddress = emailAddress;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    
    public void setBirthDate(LocalDate birthDate) {
        if(birthDate == null){
            this.birthDate = null;
            return;
        }
        if(birthDate.isAfter(LocalDate.now())){
            throw new IllegalArgumentException("birthDate cannot be after now");
        }
        this.birthDate = birthDate;
    }
    
    /**
     * Returns the full name of the person.
     * @return firstName + " " + lastName
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }
}