package com.project.SafetyNet.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.SafetyNet.model.Firestations;
import com.project.SafetyNet.service.FirestationsService;

@RestController
@RequestMapping("/firestation")
public class FirestationController {

	private final FirestationsService firestationService;
	
	@Autowired
	public FirestationController(FirestationsService firestationService) {
		this.firestationService = firestationService;
	}
	
	@GetMapping
	public List<Firestations> getAllFirestations() {
		return firestationService.getAllFirestations();
	}
	
	@PostMapping
	public ResponseEntity<Firestations> addFirestationMapping(@RequestBody Firestations firestation) {
		try {
			firestationService.addFirestationMapping(firestation);
			return new ResponseEntity<>(firestation, HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping
	public ResponseEntity<Firestations> updateFirestationNumber(@RequestBody Firestations firestation) {
		Firestations updatedFirestation = firestationService.updateFirestationNumber(firestation);
		if (updatedFirestation != null) {
			return new ResponseEntity<>(updatedFirestation, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@DeleteMapping
	public ResponseEntity<Void> deleteFirestationMapping(@RequestParam String address) {
		boolean isDeleted = firestationService.deleteFirestationMapping(address);
		if (isDeleted) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
}
