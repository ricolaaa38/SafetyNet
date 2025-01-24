package com.project.SafetyNet.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.SafetyNet.model.AllData;
import com.project.SafetyNet.model.Firestations;
import com.project.SafetyNet.model.MedicalRecords;
import com.project.SafetyNet.model.Person;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;

/**
 * This class is responsible for connecting to and manipulating the JSON file that
 * contains the application's data. It provides methods to read from and write to
 * the JSON file for various data entities such as Persons, Medical Records, and Firestations.
 */
@Repository
@PropertySource("classpath:application.properties")
public class JsonFileConnect {
	
	@Value("${json.data.path}")
	private String jsonFilePath;
	private final ObjectMapper objectMapper = new ObjectMapper();
	
	  /**
     * Reads data from the JSON file and maps it to an AllData object.
     *
     * @return An AllData object containing the data from the JSON file.
     * @throws IOException If an I/O error occurs during file reading.
     */
	private AllData readJson() throws IOException {
		return objectMapper.readValue(new File(jsonFilePath), AllData.class);
	}
	
	/**
     * Writes the given AllData object to the JSON file.
     *
     * @param allData The AllData object to be written to the JSON file.
     * @throws IOException If an I/O error occurs during file writing.
     */
	private void writeJson(AllData allData) throws IOException {
		objectMapper.writeValue(new File(jsonFilePath), allData);
	}
	
	 /**
     * Retrieves all persons from the JSON file.
     *
     * @return A list of Person objects.
     * @throws IOException If an I/O error occurs during reading.
     */
	public List<Person> getAllPersons() throws IOException {
		return readJson().getPersons();
	}
	
	 /**
     * Saves a list of Person objects to the JSON file.
     *
     * @param persons The list of Person objects to be saved.
     * @throws IOException If an I/O error occurs during writing.
     */
	public void saveAllPersons(List<Person> persons) throws IOException {
		AllData allData = readJson();
		allData.setPersons(persons);
		writeJson(allData);
	}
	
	/**
     * Retrieves all medical records from the JSON file.
     *
     * @return A list of MedicalRecords objects.
     * @throws IOException If an I/O error occurs during reading.
     */
	public List<MedicalRecords> getAllMedicalRecords() throws IOException {
		return readJson().getMedicalRecords();
	}
	
	 /**
     * Saves a list of MedicalRecords objects to the JSON file.
     *
     * @param medicalrecords The list of MedicalRecords objects to be saved.
     * @throws IOException If an I/O error occurs during writing.
     */
	public void saveAllMedicalRecords(List<MedicalRecords> medicalrecords) throws IOException {
		AllData allData = readJson();
		allData.setMedicalRecords(medicalrecords);
		writeJson(allData);
	}
	
	 /**
     * Retrieves all firestations from the JSON file.
     *
     * @return A list of Firestations objects.
     * @throws IOException If an I/O error occurs during reading.
     */
	public List<Firestations> getAllFirestations() throws IOException {
		return readJson().getFirestations();
	}
	
	  /**
     * Saves a list of Firestations objects to the JSON file.
     *
     * @param firestations The list of Firestations objects to be saved.
     * @throws IOException If an I/O error occurs during writing.
     */
	public void saveAllFirestations(List<Firestations> firestations) throws IOException {
		AllData allData = readJson();
		allData.setFirestations(firestations);
		writeJson(allData);
	}

}
