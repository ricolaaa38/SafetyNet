package com.project.SafetyNet.model;

import java.util.List;

/**
 * Represents the medical records of a person, including personal details,
 * medications, and allergies.
 */
public class MedicalRecords {

    private String firstName;
    private String lastName;
    private String birthdate;
    private List<String> medications;
    private List<String> allergies;

    /**
     * Gets the first name of the individual.
     * 
     * @return First name of the person.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Gets the last name of the individual.
     * 
     * @return Last name of the person.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Gets the birthdate of the individual.
     * 
     * @return Birthdate of the person.
     */
    public String getBirthdate() {
        return birthdate;
    }

    /**
     * Gets the list of medications for the individual.
     * 
     * @return List of medications.
     */
    public List<String> getMedications() {
        return medications;
    }

    /**
     * Gets the list of allergies for the individual.
     * 
     * @return List of allergies.
     */
    public List<String> getAllergies() {
        return allergies;
    }

    /**
     * Sets the first name of the individual.
     * 
     * @param firstName First name to set.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Sets the last name of the individual.
     * 
     * @param lastName Last name to set.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Sets the birthdate of the individual.
     * 
     * @param birthdate Birthdate to set.
     */
    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    /**
     * Sets the list of medications for the individual.
     * 
     * @param medications List of medications to set.
     */
    public void setMedications(List<String> medications) {
        this.medications = medications;
    }

    /**
     * Sets the list of allergies for the individual.
     * 
     * @param allergies List of allergies to set.
     */
    public void setAllergies(List<String> allergies) {
        this.allergies = allergies;
    }
}

