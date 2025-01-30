package com.project.SafetyNet.service;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.project.SafetyNet.dto.FireDTO;
import com.project.SafetyNet.dto.FireResponseDTO;
import com.project.SafetyNet.dto.FloodDTO;
import com.project.SafetyNet.model.Firestations;
import com.project.SafetyNet.model.MedicalRecords;
import com.project.SafetyNet.model.Person;
import com.project.SafetyNet.repository.JsonFileConnect;
import com.project.SafetyNet.tool.CalculateAge;

/**
 * Service class for managing Firestations entities
 */
@Service
public class FirestationsService {

	private final JsonFileConnect jsonFileConnect;
	
	private static final Logger successLogger = LogManager.getLogger("com.project.success");
    private static final Logger errorLogger = LogManager.getLogger("com.project.error");
    private static final Logger debugLogger = LogManager.getLogger("com.project.debug");

	
	public FirestationsService(JsonFileConnect jsonFileConnect) {
		this.jsonFileConnect = jsonFileConnect;
	}
	
	/** Retrieves all firestation mappings from the data source.
	*
	* @return a list of {@link Firestations} objects, or {@code null} if an error occurs
	*/
	public List<Firestations> getAllFirestations() {
        debugLogger.debug("Starting execution of method getAllFirestations.");
        try {
            List<Firestations> firestations = jsonFileConnect.getAllFirestations();
            successLogger.info("Successfully retrieved all firestations.");
            return firestations;
        } catch (IOException e) {
            errorLogger.error("Error retrieving all firestations: {}", e.getMessage(), e);
            return null;
        }
    }

	 /**
     * Saves a list of firestation mappings to the data source.
     *
     * @param firestations the list of firestation mappings to save
     */
	public void saveFirestationMapping(List<Firestations> firestations) {
        debugLogger.debug("Starting method saveFirestationMapping with firestations size={}", firestations.size());
        try {
            jsonFileConnect.saveAllFirestations(firestations);
            successLogger.info("Firestation mappings have been successfully saved.");
        } catch (IOException e) {
            errorLogger.error("Error saving firestation mappings: {}", e.getMessage(), e);
        }
    }

	/**
     * Adds a new firestation mapping to the data source.
     *
     * @param firestation the firestation mapping to add
     */
	public void addFirestationMapping(Firestations firestation) {
	    debugLogger.debug("Starting method addFirestationMapping with firestation={}", firestation);
	    try {
	        List<Firestations> firestations = jsonFileConnect.getAllFirestations();
	        firestations.add(firestation);
	        successLogger.info("Firestation mapping added successfully: {}", firestation);
	        saveFirestationMapping(firestations);
	    } catch (IOException e) {
	        errorLogger.error("Error adding firestation mapping: {}", e.getMessage(), e);
	    }
	}

	 /**
     * Updates the firestation number for an existing firestation mapping.
     *
     * @param firestation the updated firestation object containing the address and new station number
     * @return the updated {@link Firestations} object, or {@code null} if no matching firestation is found
     */
	public Firestations updateFirestationNumber(Firestations firestation) {
	    debugLogger.debug("Starting method updateFirestationNumber with firestation={}", firestation);
	    List<Firestations> firestations = extractedFirestation();
	    Optional<Firestations> optionalFirestation = firestations.stream()
	            .filter(p -> p.getAddress().equals(firestation.getAddress()))
	            .findFirst();

	    if (optionalFirestation.isPresent()) {
	        Firestations updatedFirestation = rewriteFirestation(firestation, optionalFirestation);
	        successLogger.info("Updated firestation number for address={} to station={}", firestation.getAddress(), firestation.getStation());
	        saveFirestationMapping(firestations);
	        return updatedFirestation;
	    }

	    debugLogger.debug("No firestation found with address={} to update.", firestation.getAddress());
	    return null;
	}

	/**
	 * Updates the station number of an existing firestation mapping.
	 *
	 * @param firestation the new {@link Firestations} object with updated station information.
	 * @param optionalFirestation the existing firestation to update, wrapped in an {@link Optional}.
	 * @return the updated {@link Firestations} object, or {@code null} if no match is found.
	 */
	private Firestations rewriteFirestation(Firestations firestation, Optional<Firestations> optionalFirestation) {
	    debugLogger.debug("Starting method rewriteFirestation with firestation={}", firestation);

	    if (optionalFirestation.isEmpty()) {
	        errorLogger.error("No matching firestation found for the provided address. Update operation skipped.");
	        return null;
	    }

	    Firestations existingFirestation = optionalFirestation.get();
	    existingFirestation.setStation(firestation.getStation());
	    successLogger.info("Successfully updated firestation: {}", existingFirestation);

	    return existingFirestation;
	}

	/**
	 * Fetches all firestations from the JSON file.
	 *
	 * @return a list of {@link Firestations}, or an empty list if an error occurs.
	 */
	private List<Firestations> extractedFirestation() {
	    debugLogger.debug("Starting method extractedFirestation to retrieve all firestations.");
	    try {
	        List<Firestations> firestations = jsonFileConnect.getAllFirestations();
	        successLogger.info("Successfully fetched {} firestations from the JSON file.", firestations.size());
	        return firestations;
	    } catch (IOException e) {
	        errorLogger.error("Error extracting firestations: {}", e.getMessage(), e);
	        return Collections.emptyList();
	    }
	}
	
	/**
	 * Deletes a firestation mapping by its address.
	 *
	 * @param address the address of the firestation mapping to delete
	 * @return {@code true} if the firestation mapping was successfully deleted, {@code false} otherwise
	 */
	public boolean deleteFirestationMapping(String address) {
	    debugLogger.debug("Starting method deleteFirestationMapping with address={}", address);
	    try {
	        List<Firestations> firestations = jsonFileConnect.getAllFirestations();

	        for (int i = 0; i < firestations.size(); i++) {
	            Firestations firestation = firestations.get(i);
	            if (firestation.getAddress().equals(address)) {
	                successLogger.info("Found firestation mapping for address={}, deleting...", address);
	                firestations.remove(i);
	                saveFirestationMapping(firestations);
	                successLogger.info("Successfully deleted firestation mapping for address={}.", address);
	                return true;
	            }
	        }

	        debugLogger.debug("No firestation mapping found for address={}.", address);
	    } catch (IOException e) {
	        errorLogger.error("Error deleting firestation mapping for address {}: {}", address, e.getMessage(), e);
	    }
	    return false;
	}


	 /**
     * Retrieves a list of persons living at addresses covered by a specific firestation number, along with the count of adults and children.
     *
     * @param stationNumber the firestation number
     * @return a map containing the list of persons, the count of adults, and the count of children
     */
	public Map<String, Object> getPersonsByStation(int stationNumber) {
	    debugLogger.debug("Starting method getPersonsByStation with stationNumber={}", stationNumber);
	    Map<String, Object> result = new HashMap<>();
	    try {
	        List<Firestations> firestations = jsonFileConnect.getAllFirestations();
	        List<String> addresses = firestations.stream()
	                .filter(firestation -> firestation.getStation().equals(String.valueOf(stationNumber)))
	                .map(Firestations::getAddress)
	                .toList();

	        successLogger.info("Found {} addresses for stationNumber {}", addresses.size(), stationNumber);

	        if (addresses.isEmpty()) {
	            successLogger.info("No addresses found for stationNumber {}", stationNumber);
	            result.put("persons", Collections.emptyList());
	            result.put("adults", 0);
	            result.put("children", 0);
	            return result;
	        }

	        List<Person> persons = jsonFileConnect.getAllPersons();
	        List<Map<String, String>> filteredPersons = persons.stream()
	                .filter(person -> addresses.contains(person.getAddress()))
	                .map(person -> Map.of(
	                        "firstName", person.getFirstName(),
	                        "lastName", person.getLastName(),
	                        "address", person.getAddress(),
	                        "phone", person.getPhone()))
	                .toList();

	        List<MedicalRecords> medicalRecords = jsonFileConnect.getAllMedicalRecords();

	        int adults = 0;
	        int children = 0;
	        for (Map<String, String> m : filteredPersons) {
	            MedicalRecords record = medicalRecords.stream()
	                    .filter(mr -> m.get("firstName").equals(mr.getFirstName()) &&
	                                  m.get("lastName").equals(mr.getLastName()))
	                    .findFirst()
	                    .orElse(null);

	            if (record != null && CalculateAge.isOverEighteen(record.getBirthdate())) {
	                adults++;
	            } else {
	                children++;
	            }
	        }

	        successLogger.info("Found {} adults and {} children for stationNumber {}", adults, children, stationNumber);
	        result.put("persons", filteredPersons);
	        result.put("adults", adults);
	        result.put("children", children);
	    } catch (IOException e) {
	        errorLogger.error("Error processing stationNumber {}: {}", stationNumber, e.getMessage(), e);
	    }
	    return result;
	}

	  /**
     * Retrieves unique phone numbers of persons covered by a specific firestation.
     *
     * @param firestation the firestation number.
     * @return a list of unique phone numbers for the specified firestation.
     */
	public List<String> getPhoneByFirestationNumber(String firestation) {
	    debugLogger.debug("Starting method getPhoneByFirestationNumber with firestation={}", firestation);

	    try {
	        debugLogger.debug("Fetching firestations from JSON...");
	        List<Firestations> firestations = jsonFileConnect.getAllFirestations();

	        List<String> addresses = firestations.stream()
	                .filter(station -> firestation.equals(station.getStation()))
	                .map(Firestations::getAddress)
	                .collect(Collectors.toList());

	        successLogger.info("Found {} address(es) for firestation {}", addresses.size(), firestation);

	        if (addresses.isEmpty()) {
	            successLogger.warn("No addresses found for firestation {}", firestation);
	            return List.of();
	        }

	        debugLogger.debug("Fetching persons from JSON...");
	        List<Person> persons = jsonFileConnect.getAllPersons();
	        List<String> phoneNumbers = persons.stream()
	                .filter(person -> addresses.contains(person.getAddress()))
	                .map(Person::getPhone)
	                .distinct()
	                .collect(Collectors.toList());

	        successLogger.info("Retrieved {} unique phone number(s) for firestation {}", phoneNumbers.size(), firestation);

	        return phoneNumbers;
	    } catch (IOException e) {
	        errorLogger.error("Error occurred while processing firestation {}: {}", firestation, e.getMessage(), e);
	        return List.of();
	    }
	}

	/**
     * Retrieves the firestation number and details of house members for a specific address.
     *
     * @param address the address to retrieve information for.
     * @return a {@link FireResponseDTO} containing the station number and details of house members.
     */
	public FireResponseDTO getHouseMembersAndFirestationByAddress(String address) {
	    debugLogger.debug("Starting method getHouseMembersAndFirestationByAddress with address={}", address);

	    try {
	        debugLogger.debug("Fetching data from JSON...");
	        List<Firestations> firestations = jsonFileConnect.getAllFirestations();
	        List<Person> persons = jsonFileConnect.getAllPersons();
	        List<MedicalRecords> medicalRecords = jsonFileConnect.getAllMedicalRecords();

	        debugLogger.debug("Finding station number for address={}", address);
	        String stationNumber = firestations.stream()
	                .filter(firestation -> firestation.getAddress().equals(address))
	                .map(Firestations::getStation)
	                .findFirst()
	                .orElse(null);

	        if (stationNumber == null) {
	            successLogger.warn("No station found for address={}", address);
	            return null;
	        }

	        successLogger.info("Station number {} found for address={}", stationNumber, address);

	        debugLogger.debug("Getting house members for address={}", address);
	        List<FireDTO> houseMembers = persons.stream()
	                .filter(person -> person.getAddress().equals(address))
	                .map(person -> {
	                    MedicalRecords medicalRecord = medicalRecords.stream()
	                            .filter(record -> record.getFirstName().equals(person.getFirstName()) &&
	                                              record.getLastName().equals(person.getLastName()))
	                            .findFirst()
	                            .orElse(null);

	                    String age = medicalRecord != null ? String.valueOf(CalculateAge.calculateAge(medicalRecord.getBirthdate())) : "N/A";
	                    List<String> medications = medicalRecord != null ? medicalRecord.getMedications() : List.of();
	                    List<String> allergies = medicalRecord != null ? medicalRecord.getAllergies() : List.of();

	                    return new FireDTO(person.getLastName(), person.getPhone(), age, medications, allergies);
	                })
	                .collect(Collectors.toList());

	        successLogger.info("Retrieved {} house members for address={}", houseMembers.size(), address);
	        return new FireResponseDTO(stationNumber, houseMembers);

	    } catch (IOException e) {
	        errorLogger.error("Error reading data for address {}: {}", address, e.getMessage(), e);
	        return null;
	    }
	}

	/**
     * Retrieves residents informations for a given firestation.
     *
     * @param station the firestation number.
     * @return a list of {@link FloodDTO} containing addresses and their residents' details.
     */
	public List<FloodDTO> getFloodInfoByStation(String station) {
	    debugLogger.debug("Starting method getFloodInfoByStation with station={}", station);
	    try {
	        debugLogger.debug("Fetching data from JSON...");
	        List<Firestations> firestations = jsonFileConnect.getAllFirestations();
	        List<Person> persons = jsonFileConnect.getAllPersons();
	        List<MedicalRecords> medicalRecords = jsonFileConnect.getAllMedicalRecords();

	        debugLogger.debug("Finding addresses for station={}", station);
	        List<String> addresses = firestations.stream()
	                .filter(firestation -> station.equals(firestation.getStation()))
	                .map(Firestations::getAddress)
	                .collect(Collectors.toList());

	        successLogger.info("Found {} address(es) for station {}", addresses.size(), station);

	        List<FloodDTO> floodInfo = addresses.stream()
	                .map(address -> {
	                    debugLogger.debug("Fetching residents for address={}", address);
	                    List<FireDTO> residents = persons.stream()
	                            .filter(person -> person.getAddress().equals(address))
	                            .map(person -> {
	                                MedicalRecords medicalRecord = medicalRecords.stream()
	                                        .filter(record -> record.getFirstName().equals(person.getFirstName()) &&
	                                                          record.getLastName().equals(person.getLastName()))
	                                        .findFirst()
	                                        .orElse(null);

	                                int age = (medicalRecord != null) ? CalculateAge.calculateAge(medicalRecord.getBirthdate()) : -1;

	                                return new FireDTO(
	                                        person.getLastName(),
	                                        person.getPhone(),
	                                        String.valueOf(age),
	                                        medicalRecord != null ? medicalRecord.getMedications() : List.of(),
	                                        medicalRecord != null ? medicalRecord.getAllergies() : List.of()
	                                );
	                            })
	                            .collect(Collectors.toList());

	                    return new FloodDTO(address, residents);
	                })
	                .collect(Collectors.toList());

	        successLogger.info("Retrieved flood info for {} address(es) in station {}", floodInfo.size(), station);
	        return floodInfo;

	    } catch (IOException e) {
	        errorLogger.error("Error occurred while processing flood info for station {}: {}", station, e.getMessage(), e);
	        return List.of();
	    }
	}

	
}
