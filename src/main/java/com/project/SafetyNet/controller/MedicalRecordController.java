package com.project.SafetyNet.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.SafetyNet.model.MedicalRecords;
import com.project.SafetyNet.service.MedicalRecordsService;

@RestController
@RequestMapping("/medicalRecord")
public class MedicalRecordController {

	private final MedicalRecordsService medicalRecordService;
	
	@Autowired
	public MedicalRecordController(MedicalRecordsService medicalRecordService) {
		this.medicalRecordService = medicalRecordService;
	}
	
	@GetMapping
	public List<MedicalRecords> getAllMedicalRecords() {
		return medicalRecordService.getAllMedicalRecords();
	}
	
	@PostMapping
	public ResponseEntity<MedicalRecords> addMedicalRecord(@RequestBody MedicalRecords medicalRecord) {
		try {
			medicalRecordService.addMedicalRecord(medicalRecord);
			return new ResponseEntity<>(medicalRecord, HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping
	public ResponseEntity<MedicalRecords> updateMedicalRecord(@RequestBody MedicalRecords medicalRecord) {
		MedicalRecords updatedMedicalRecord = medicalRecordService.updateMedicalRecord(medicalRecord);
		if (updatedMedicalRecord != null) {
			return new ResponseEntity<>(updatedMedicalRecord, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@DeleteMapping
	public ResponseEntity<Void> deleteMedicalRecord(@RequestParam String firstName, @RequestParam String lastName) {
		boolean isDeleted = medicalRecordService.deleteMedicalRecord(firstName, lastName);
		if (isDeleted) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}
