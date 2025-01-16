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

	private List<MedicalRecords> extractedMedicalRecord() {
	    try {
	        debugLogger.debug("Extracting all medical records");
	        return jsonFileConnect.getAllMedicalRecords();
	    } catch (IOException e) {
	        errorLogger.error("Error occurred while extracting medical records: {}", e.getMessage(), e);
	    }
	    return Collections.emptyList();
	}

	private MedicalRecords rewriteMedicalRecord(MedicalRecords medicalRecord, Optional<MedicalRecords> optionalMedicalRecord) {
	    MedicalRecords p = optionalMedicalRecord.get();
	    debugLogger.debug("Updating medical record: {}", p);
	    p.setBirthdate(medicalRecord.getBirthdate());
	    p.setMedications(medicalRecord.getMedications());
	    p.setAllergies(medicalRecord.getAllergies());
	    successLogger.info("Successfully rewritten medical record for {}", p.getFirstName());
	    return p;
	}

	private void saveMedicalRecord(List<MedicalRecords> medicalRecords) {
	    try {
	        debugLogger.debug("Saving medical records");
	        jsonFileConnect.saveAllMedicalRecords(medicalRecords);
	        successLogger.info("Successfully saved all medical records.");
	    } catch (IOException e) {
	        errorLogger.error("Error occurred while saving medical records: {}", e.getMessage(), e);
	    }
	}
	
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
