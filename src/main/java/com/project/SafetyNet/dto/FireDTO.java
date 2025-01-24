package com.project.SafetyNet.dto;

import java.util.List;

/**
 * Data Transfer Object (DTO) representing the fire-related information of an individual,
 * including their personal details and medical informations
 */
public class FireDTO {

    private String lastName;
    private String phone;
    private String age;
    private List<String> medications;
    private List<String> allergies;

    /**
     * Constructs a new FireDTO with the provided information.
     * 
     * @param lastName   The last name of the individual.
     * @param phone      The phone number of the individual.
     * @param age        The age of the individual.
     * @param medications List of medications the individual is taking.
     * @param allergies  List of allergies the individual has.
     */
    public FireDTO(String lastName, String phone, String age, List<String> medications, List<String> allergies) {
        this.lastName = lastName;
        this.phone = phone;
        this.age = age;
        this.medications = medications;
        this.allergies = allergies;
    }

    /**
     * Gets the last name of the individual.
     * 
     * @return The last name of the individual.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Gets the phone number of the individual.
     * 
     * @return The phone number of the individual.
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Gets the age of the individual.
     * 
     * @return The age of the individual.
     */
    public String getAge() {
        return age;
    }

    /**
     * Gets the list of medications the individual is taking.
     * 
     * @return List of medications.
     */
    public List<String> getMedications() {
        return medications;
    }

    /**
     * Gets the list of allergies the individual has.
     * 
     * @return List of allergies.
     */
    public List<String> getAllergies() {
        return allergies;
    }
}

