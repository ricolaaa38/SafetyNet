package com.project.SafetyNet.dto;

import java.util.List;

/**
 * Data Transfer Object (DTO) representing the detailed information of a person
 * based on their last name, including their address, age, email, and medical details
 * such as medications and allergies.
 */
public class PersonInfoLastNameDTO {

    private String lastName;
    private String address;
    private int age;
    private String email;
    private List<String> medications;
    private List<String> allergies;

    /**
     * Constructs a new PersonInfoLastNameDTO with the provided personal and medical information.
     * 
     * @param lastName   The last name of the person.
     * @param address    The address where the person resides.
     * @param age        The age of the person.
     * @param email      The email address of the person.
     * @param medications A list of medications the person is currently using.
     * @param allergies  A list of allergies the person is known to have.
     */
    public PersonInfoLastNameDTO(String lastName, String address, int age, String email, List<String> medications, List<String> allergies) {
        this.lastName = lastName;
        this.address = address;
        this.age = age;
        this.email = email;
        this.medications = medications;
        this.allergies = allergies;
    }

    /**
     * Gets the last name of the person.
     * 
     * @return The last name of the person.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Gets the address of the person.
     * 
     * @return The address of the person.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Gets the age of the person.
     * 
     * @return The age of the person.
     */
    public int getAge() {
        return age;
    }

    /**
     * Gets the email address of the person.
     * 
     * @return The email address of the person.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Gets the list of medications the person is currently taking.
     * 
     * @return A list of medications.
     */
    public List<String> getMedications() {
        return medications;
    }

    /**
     * Gets the list of allergies the person has.
     * 
     * @return A list of allergies.
     */
    public List<String> getAllergies() {
        return allergies;
    }
}
