package com.project.SafetyNet.controller;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.SafetyNet.dto.FireDTO;
import com.project.SafetyNet.dto.FireResponseDTO;
import com.project.SafetyNet.dto.FloodDTO;
import com.project.SafetyNet.model.Firestations;
import com.project.SafetyNet.service.FirestationsService;

/**
 * Controller for handling firestation-related API requests.
 */
@RestController
@RequestMapping
public class FirestationController {

    private final FirestationsService firestationService;

    private static final Logger successLogger = LogManager.getLogger("com.project.success");
    private static final Logger errorLogger = LogManager.getLogger("com.project.error");
    private static final Logger debugLogger = LogManager.getLogger("com.project.debug");

    /**
     * Constructs a FirestationController with the specified FirestationsService.
     * 
     * @param firestationService
     */
    public FirestationController(FirestationsService firestationService) {
        this.firestationService = firestationService;
    }

    /**
     * Fetches all firestations.
     * 
     * @return list of {@link Firestations}.
     */
    @GetMapping("/firestation")
    public List<Firestations> getAllFirestations() {
        debugLogger.debug("Fetching all firestations.");
        List<Firestations> firestations = firestationService.getAllFirestations();
        successLogger.info("Successfully fetched all firestations.");
        return firestations;
    }

    /**
     * Fetches persons associated with a given firestation number.
     * 
     * @param stationNumber the firestation number.
     * @return map with persons' information.
     */
    @GetMapping(value = "/firestation", params = "stationNumber")
    public ResponseEntity<Map<String, Object>> getPersonsByStation(@RequestParam int stationNumber) {
        debugLogger.debug("Fetching persons by station number: {}", stationNumber);
        try {
            Map<String, Object> result = firestationService.getPersonsByStation(stationNumber);
            successLogger.info("Successfully fetched persons by station number: {}", stationNumber);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            errorLogger.error("Error fetching persons by station number: {}", stationNumber, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Adds a new firestation mapping.
     * 
     * @param firestation the {@link Firestations} to add.
     * @return ResponseEntity with the status of the operation.
     */
    @PostMapping("/firestation")
    public ResponseEntity<Firestations> addFirestationMapping(@RequestBody Firestations firestation) {
        debugLogger.debug("Adding new firestation mapping: {}", firestation);
        try {
            firestationService.addFirestationMapping(firestation);
            successLogger.info("Successfully added firestation mapping: {}", firestation);
            return new ResponseEntity<>(firestation, HttpStatus.CREATED);
        } catch (Exception e) {
            errorLogger.error("Error adding firestation mapping: {}", firestation, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates an existing firestation number.
     * 
     * @param firestation the {@link Firestations} to update.
     * @return ResponseEntity with the status of the operation.
     */
    @PutMapping("/firestation")
    public ResponseEntity<Firestations> updateFirestationNumber(@RequestBody Firestations firestation) {
        debugLogger.debug("Updating firestation: {}", firestation);
        try {
            Firestations updatedFirestation = firestationService.updateFirestationNumber(firestation);
            if (updatedFirestation != null) {
                successLogger.info("Successfully updated firestation: {}", updatedFirestation);
                return new ResponseEntity<>(updatedFirestation, HttpStatus.OK);
            } else {
                errorLogger.error("Firestation not found for update: {}", firestation);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            errorLogger.error("Error updating firestation: {}", firestation, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes a firestation mapping for a given address.
     * 
     * @param address the address associated with the firestation mapping.
     * @return ResponseEntity with the status of the operation.
     */
    @DeleteMapping("/firestation")
    public ResponseEntity<Void> deleteFirestationMapping(@RequestParam String address) {
        debugLogger.debug("Deleting firestation mapping for address: {}", address);
        try {
            boolean isDeleted = firestationService.deleteFirestationMapping(address);
            if (isDeleted) {
                successLogger.info("Successfully deleted firestation mapping for address: {}", address);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                errorLogger.error("Firestation mapping not found for address: {}", address);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            errorLogger.error("Error deleting firestation mapping for address: {}", address, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Fetches phone numbers for a given firestation.
     * 
     * @param firestation the firestation to fetch phone numbers for.
     * @return list of phone numbers.
     */
    @GetMapping("/phoneAlert")
    public ResponseEntity<List<String>> getPhoneByFirestationNumber(@RequestParam String firestation) {
        debugLogger.debug("Fetching phone numbers for firestation: {}", firestation);
        List<String> phones = firestationService.getPhoneByFirestationNumber(firestation);
        if (phones == null || phones.isEmpty()) {
            errorLogger.error("No phone numbers found for firestation: {}", firestation);
            return ResponseEntity.noContent().build();
        }
        successLogger.info("Successfully fetched phone numbers for firestation: {}", firestation);
        return ResponseEntity.ok(phones);
    }

    /**
     * Fetches house members and firestation information by address.
     * 
     * @param address the address to fetch information for.
     * @return {@link FireResponseDTO} containing house members and firestation data.
     */
    @GetMapping("/fire")
    public ResponseEntity<FireResponseDTO> getHouseMembersAndFirestationByAddress(@RequestParam String address) {
        debugLogger.debug("Fetching house members and firestation for address: {}", address);
        FireResponseDTO response = firestationService.getHouseMembersAndFirestationByAddress(address);
        if (response == null || response.getHouseMembers().isEmpty()) {
            errorLogger.error("No house members found for address: {}", address);
            return ResponseEntity.noContent().build();
        }
        successLogger.info("Successfully fetched house members and firestation for address: {}", address);
        return ResponseEntity.ok(response);
    }

    /**
     * Fetches flood information for a given station.
     * 
     * @param station the station to fetch flood information for.
     * @return list of {@link FloodDTO} containing flood data.
     */
    @GetMapping("/flood/stations")
    public ResponseEntity<List<FloodDTO>> getFloodInfoByStation(@RequestParam String station) {
        debugLogger.debug("Fetching flood info for station: {}", station);
        List<FloodDTO> result = firestationService.getFloodInfoByStation(station);
        if (result.isEmpty()) {
            errorLogger.error("No flood info found for station: {}", station);
            return ResponseEntity.noContent().build();
        }
        successLogger.info("Successfully fetched flood info for station: {}", station);
        return ResponseEntity.ok(result);
    }
}