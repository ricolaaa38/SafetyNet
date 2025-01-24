package com.project.SafetyNet.model;

import lombok.Data;

/**
 * Represents a person with personal information such as name, address, and contact details.
 */
@Data
public class Person {

    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String zip;
    private String phone;
    private String email;

    /**
     * Gets the first name of the person.
     * 
     * @return First name of the person.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Gets the last name of the person.
     * 
     * @return Last name of the person.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Gets the address of the person.
     * 
     * @return Address of the person.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Gets the city of the person.
     * 
     * @return City where the person resides.
     */
    public String getCity() {
        return city;
    }

    /**
     * Gets the zip code of the person's address.
     * 
     * @return Zip code of the person's address.
     */
    public String getZip() {
        return zip;
    }

    /**
     * Gets the phone number of the person.
     * 
     * @return Phone number of the person.
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Gets the email address of the person.
     * 
     * @return Email address of the person.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the first name of the person.
     * 
     * @param firstName First name to set.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Sets the last name of the person.
     * 
     * @param lastName Last name to set.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Sets the address of the person.
     * 
     * @param address Address to set.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Sets the city of the person.
     * 
     * @param city City to set.
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Sets the zip code of the person's address.
     * 
     * @param zip Zip code to set.
     */
    public void setZip(String zip) {
        this.zip = zip;
    }

    /**
     * Sets the phone number of the person.
     * 
     * @param phone Phone number to set.
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Sets the email address of the person.
     * 
     * @param email Email address to set.
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
