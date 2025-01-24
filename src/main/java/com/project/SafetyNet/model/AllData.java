package com.project.SafetyNet.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * Represents the structure of data in the JSON file, including persons, firestations, and medical records.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AllData {
	@JsonProperty("persons") private List<Person> persons;
	@JsonProperty("firestations") private List<Firestations> firestations;
	@JsonProperty("medicalrecords") private List<MedicalRecords> medicalrecords;

	  /**
     * Default constructor for AllData. It is generated by Lombok 
     * and is used to initialize the AllData object.
     */
    public AllData() {
        // Default constructor generated by Lombok.
    }

	/**
     * Gets the list of persons.
     * 
     * @return List of persons.
     */
	public List<Person> getPersons() {
		return persons;
	}

	/**
     * Sets the list of persons.
     * 
     * @param persons List of persons.
     */
	public void setPersons(List<Person> persons) {
		this.persons = persons;
	}
	
	 /**
     * Gets the list of medical records.
     * 
     * @return List of medical records.
     */
	public List<MedicalRecords> getMedicalRecords() {
		return medicalrecords;
	}
	
	  /**
     * Sets the list of medical records.
     * 
     * @param medicalRecords List of medical records.
     */
	public void setMedicalRecords(List<MedicalRecords> medicalRecords) {
		this.medicalrecords = medicalRecords;
	}
	
	/**
     * Gets the list of firestations.
     * 
     * @return List of firestations.
     */
	public List<Firestations> getFirestations() {
		return firestations;
	}
	
	 /**
     * Sets the list of firestations.
     * 
     * @param firestations List of firestations.
     */
	public void setFirestations(List<Firestations> firestations) {
		this.firestations = firestations;
	}
	
}
