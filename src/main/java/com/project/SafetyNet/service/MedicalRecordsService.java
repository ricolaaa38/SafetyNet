package com.project.SafetyNet.service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.SafetyNet.model.MedicalRecords;
import com.project.SafetyNet.repository.JsonFileConnect;

/**
 * Service class for managing MedicalRecords entities
 */
@Service
public class MedicalRecordsService {
		
	private final JsonFileConnect jsonFileConnect;
	
	private static final Logger successLogger = LogManager.getLogger("com.project.success");
    private static final Logger errorLogger = LogManager.getLogger("com.project.error");
    private static final Logger debugLogger = LogManager.getLogger("com.project.debug");
	
	@Autowired
	public MedicalRecordsService(JsonFileConnect jsonFileConnect) {
		this.jsonFileConnect = jsonFileConnect;
	}
	
	  /**
     * Retrieves all medical records.
     *
     * @return A list of all medical records, or {@code null} in case of an error.
     */
	public List<MedicalRecords> getAllMedicalRecords() {
	    try {
	        debugLogger.debug("Fetching all medical records");
	        List<MedicalRecords> records = jsonFileConnect.getAllMedicalRecords();
	        successLogger.info("Successfully retrieved {} medical records", records.size());
	        return records;
	    } catch (IOException e) {
	        errorLogger.error("Error occurred while fetching all medical records: {}", e.getMessage(), e);
	        return null;
	    }
	}
	
	 /**
     * Adds a new medical record.
     *
     * @param medicalRecord The MedicalRecords object to be added.
     */
	public void addMedicalRecord(MedicalRecords medicalRecord) {
	    try {
	        debugLogger.debug("Adding new medical record: {}", medicalRecord);
	        List<MedicalRecords> medicalRecords = jsonFileConnect.getAllMedicalRecords();
	        medicalRecords.add(medicalRecord);
	        saveMedicalRecord(medicalRecords);
	        successLogger.info("Successfully added medical record for {}", medicalRecord.getFirstName());
	    } catch (IOException e) {
	        errorLogger.error("Error occurred while adding medical record for {}: {}", medicalRecord.getFirstName(), e.getMessage(), e);
	    }
	}
	
	 /**
     * Updates an existing medical record.
     *
     * @param medicalRecord The MedicalRecords object containing updated information.
     * @return The updated MedicalRecords object, or {@code null} if no matching record is found.
     */
	public MedicalRecords updateMedicalRecord(MedicalRecords medicalRecord) {
	    debugLogger.debug("Updating medical record for {} {}", medicalRecord.getFirstName(), medicalRecord.getLastName());
	    List<MedicalRecords> medicalRecords = extractedMedicalRecord();
	    Optional<MedicalRecords> optionalMedicalRecord = medicalRecords.stream()
	            .filter(p -> p.getFirstName().equals(medicalRecord.getFirstName()) && p.getLastName().equals(medicalRecord.getLastName()))
	            .findFirst();

	    if (optionalMedicalRecord.isPresent()) {
	        MedicalRecords p = rewriteMedicalRecord(medicalRecord, optionalMedicalRecord);
	        saveMedicalRecord(medicalRecords);
	        successLogger.info("Successfully updated medical record for {}", medicalRecord.getFirstName());
	        return p;
	    } else {
	        errorLogger.error("No medical record found for {} {}", medicalRecord.getFirstName(), medicalRecord.getLastName());
	        return null;
	    }
	}

    /**
     * Extracts all medical records from the data source.
     *
     * @return A list of medical records, or an empty list in case of an error.
     */
	private List<MedicalRecords> extractedMedicalRecord() {
	    try {
	        debugLogger.debug("Extracting all medical records");
	        return jsonFileConnect.getAllMedicalRecords();
	    } catch (IOException e) {
	        errorLogger.error("Error occurred while extracting medical records: {}", e.getMessage(), e);
	    }
	    return Collections.emptyList();
	}

	  /**
     * Updates the details of an existing medical record.
     *
     * @param medicalRecord The updated version of the medical record.
     * @param optionalMedicalRecord The existing medical record to be updated.
     * @return The updated MedicalRecords object.
     */
	private MedicalRecords rewriteMedicalRecord(MedicalRecords medicalRecord, Optional<MedicalRecords> optionalMedicalRecord) {
	    MedicalRecords p = optionalMedicalRecord.get();
	    debugLogger.debug("Updating medical record: {}", p);
	    p.setBirthdate(medicalRecord.getBirthdate());
	    p.setMedications(medicalRecord.getMedications());
	    p.setAllergies(medicalRecord.getAllergies());
	    successLogger.info("Successfully rewritten medical record for {}", p.getFirstName());
	    return p;
	}

	 /**
     * Saves all medical records to the JSON file.
     *
     * @param medicalRecords The list of medical records to save.
     */
	private void saveMedicalRecord(List<MedicalRecords> medicalRecords) {
	    try {
	        debugLogger.debug("Saving medical records");
	        jsonFileConnect.saveAllMedicalRecords(medicalRecords);
	        successLogger.info("Successfully saved all medical records.");
	    } catch (IOException e) {
	        errorLogger.error("Error occurred while saving medical records: {}", e.getMessage(), e);
	    }
	}
	
	 /**
     * Deletes a medical record based on the first and last name.
     *
     * @param firstName The first name of the person whose medical record should be deleted.
     * @param lastName  The last name of the person whose medical record should be deleted.
     * @return {@code true} if the deletion was successful, {@code false} otherwise.
     */
	public boolean deleteMedicalRecord(String firstName, String lastName) {
	    try {
	        debugLogger.debug("Attempting to delete medical record for {} {}", firstName, lastName);
	        List<MedicalRecords> medicalRecords = jsonFileConnect.getAllMedicalRecords();
	        for (int i = 0; i < medicalRecords.size(); i++) {
	            MedicalRecords p = medicalRecords.get(i);
	            if (p.getFirstName().equals(firstName) && p.getLastName().equals(lastName)) {
	                medicalRecords.remove(i);
	                saveMedicalRecord(medicalRecords);
	                successLogger.info("Successfully deleted medical record for {}", firstName);
	                return true;
	            }
	        }
	        errorLogger.error("No medical record found for {} {}", firstName, lastName);
	        return false;
	    } catch (IOException e) {
	        errorLogger.error("Error occurred while deleting medical record for {} {}: {}", firstName, lastName, e.getMessage(), e);
	        return false;
	    }
	}
}
