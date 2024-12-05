package com.project.SafetyNet.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.SafetyNet.dto.FireDTO;
import com.project.SafetyNet.dto.FireResponseDTO;
import com.project.SafetyNet.dto.FloodDTO;
import com.project.SafetyNet.model.Firestations;
import com.project.SafetyNet.service.FirestationsService;

@RestController
@RequestMapping
public class FirestationController {

	private final FirestationsService firestationService;
	
	@Autowired
	public FirestationController(FirestationsService firestationService) {
		this.firestationService = firestationService;
	}
	
	@GetMapping("/firestation")
	public List<Firestations> getAllFirestations() {
		return firestationService.getAllFirestations();
	}
	
	@GetMapping(value = "/firestation", params = "stationNumber")
	public ResponseEntity<Map<String, Object>> getPersonsByStation(@RequestParam int stationNumber) {
		Map<String, Object> result = firestationService.getPersonsByStation(stationNumber);
		return ResponseEntity.ok(result);
	}
	
	
	
	@PostMapping("/firestation")
	public ResponseEntity<Firestations> addFirestationMapping(@RequestBody Firestations firestation) {
		try {
			firestationService.addFirestationMapping(firestation);
			return new ResponseEntity<>(firestation, HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/firestation")
	public ResponseEntity<Firestations> updateFirestationNumber(@RequestBody Firestations firestation) {
		Firestations updatedFirestation = firestationService.updateFirestationNumber(firestation);
		if (updatedFirestation != null) {
			return new ResponseEntity<>(updatedFirestation, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@DeleteMapping("/firestation")
	public ResponseEntity<Void> deleteFirestationMapping(@RequestParam String address) {
		boolean isDeleted = firestationService.deleteFirestationMapping(address);
		if (isDeleted) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	@GetMapping("/phoneAlert")
	public ResponseEntity<List<String>> getPhoneByFirestationNumber(@RequestParam String firestation) {
		List<String> phones = firestationService.getPhoneByFirestationNumber(firestation);
		if (phones.isEmpty() || phones == null) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(phones);
	}
	
	@GetMapping("/fire")
	public ResponseEntity<FireResponseDTO> getHouseMembersAndFirestationByAddress(@RequestParam String address) {
		FireResponseDTO response = firestationService.getHouseMembersAndFirestationByAddress(address);
		if (response.getHouseMembers().isEmpty() || response == null) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/flood/stations")
	public ResponseEntity<List<FloodDTO>> getFloodInfoByStation(@RequestParam String station) {
	    List<FloodDTO> result = firestationService.getFloodInfoByStation(station);
	    if (result.isEmpty()) {
	        return ResponseEntity.noContent().build();
	    }
	    return ResponseEntity.ok(result);
	}
	
}
