package com.project.SafetyNet.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.SafetyNet.model.MedicalRecords;
import com.project.SafetyNet.service.MedicalRecordsService;

/**
 * Controller for handling medical record-related API requests.
 */
@RestController
@RequestMapping("/medicalRecord")
public class MedicalRecordController {

    private final MedicalRecordsService medicalRecordService;

    private static final Logger successLogger = LogManager.getLogger("com.project.success");
    private static final Logger errorLogger = LogManager.getLogger("com.project.error");
    private static final Logger debugLogger = LogManager.getLogger("com.project.debug");

    public MedicalRecordController(MedicalRecordsService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    /**
     * Fetches all medical records from the service.
     * 
     * @return list of {@link MedicalRecords}.
     */
    @GetMapping
    public List<MedicalRecords> getAllMedicalRecords() {
        debugLogger.debug("Fetching all medical records.");
        List<MedicalRecords> medicalRecords = medicalRecordService.getAllMedicalRecords();
        successLogger.info("Successfully fetched all medical records.");
        return medicalRecords;
    }

    /**
     * Adds a new medical record.
     * 
     * @param medicalRecord the {@link MedicalRecords} to add.
     * @return ResponseEntity with the status of the operation.
     */
    @PostMapping
    public ResponseEntity<MedicalRecords> addMedicalRecord(@RequestBody MedicalRecords medicalRecord) {
        debugLogger.debug("Adding new medical record: {}", medicalRecord);
        try {
            medicalRecordService.addMedicalRecord(medicalRecord);
            successLogger.info("Successfully added medical record: {}", medicalRecord);
            return new ResponseEntity<>(medicalRecord, HttpStatus.CREATED);
        } catch (Exception e) {
            errorLogger.error("Error adding medical record: {}", medicalRecord, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates an existing medical record.
     * 
     * @param medicalRecord the {@link MedicalRecords} to update.
     * @return ResponseEntity with the status of the operation.
     */
    @PutMapping
    public ResponseEntity<MedicalRecords> updateMedicalRecord(@RequestBody MedicalRecords medicalRecord) {
        debugLogger.debug("Updating medical record: {}", medicalRecord);
        try {
            MedicalRecords updatedMedicalRecord = medicalRecordService.updateMedicalRecord(medicalRecord);
            if (updatedMedicalRecord != null) {
                successLogger.info("Successfully updated medical record: {}", updatedMedicalRecord);
                return new ResponseEntity<>(updatedMedicalRecord, HttpStatus.OK);
            } else {
                errorLogger.error("Medical record not found for update: {}", medicalRecord);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            errorLogger.error("Error updating medical record: {}", medicalRecord, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes a medical record by first and last name.
     * 
     * @param firstName the first name of the person.
     * @param lastName the last name of the person.
     * @return ResponseEntity with the status of the operation.
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteMedicalRecord(@RequestParam String firstName, @RequestParam String lastName) {
        debugLogger.debug("Deleting medical record for: {} {}", firstName, lastName);
        try {
            boolean isDeleted = medicalRecordService.deleteMedicalRecord(firstName, lastName);
            if (isDeleted) {
                successLogger.info("Successfully deleted medical record for: {} {}", firstName, lastName);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                errorLogger.error("Medical record not found for: {} {}", firstName, lastName);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            errorLogger.error("Error deleting medical record for: {} {}", firstName, lastName, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
