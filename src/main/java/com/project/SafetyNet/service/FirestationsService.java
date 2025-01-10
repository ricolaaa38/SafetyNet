package com.project.SafetyNet.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.SafetyNet.controller.FirestationController;
import com.project.SafetyNet.dto.FireDTO;
import com.project.SafetyNet.dto.FireResponseDTO;
import com.project.SafetyNet.dto.FloodDTO;
import com.project.SafetyNet.model.Firestations;
import com.project.SafetyNet.model.MedicalRecords;
import com.project.SafetyNet.model.Person;
import com.project.SafetyNet.repository.JsonFileConnect;
import com.project.SafetyNet.tool.CalculateAge;

@Service
public class FirestationsService {

	private final JsonFileConnect jsonFileConnect;
	
	private static final Logger logger = LoggerFactory.getLogger(FirestationsService.class);	
	@Autowired
	public FirestationsService(JsonFileConnect jsonFileConnect) {
		this.jsonFileConnect = jsonFileConnect;
	}
	
	public List<Firestations> getAllFirestations() {
	    logger.debug("Starting method getAllFirestations...");
	    try {
	        List<Firestations> firestations = jsonFileConnect.getAllFirestations();
	        logger.info("Successfully fetched all firestations.");
	        return firestations;
	    } catch (IOException e) {
	        logger.error("Error fetching all firestations: {}", e.getMessage(), e);
	        return null;
	    }
	}

	public void saveFirestationMapping(List<Firestations> firestations) {
	    logger.debug("Starting method saveFirestationMapping with firestations size={}", firestations.size());
	    try {
	        jsonFileConnect.saveAllFirestations(firestations);
	        logger.info("Successfully saved firestation mappings.");
	    } catch (IOException e) {
	        logger.error("Error saving firestation mappings: {}", e.getMessage(), e);
	    }
	}

	public void addFirestationMapping(Firestations firestation) {
	    logger.debug("Starting method addFirestationMapping with firestation={}", firestation);
	    try {
	        List<Firestations> firestations = jsonFileConnect.getAllFirestations();
	        firestations.add(firestation);
	        logger.info("Firestation mapping added: {}", firestation);
	        saveFirestationMapping(firestations);
	    } catch (IOException e) {
	        logger.error("Error adding firestation mapping: {}", e.getMessage(), e);
	    }
	}

	public Firestations updateFirestationNumber(Firestations firestation) {
	    logger.debug("Starting method updateFirestationNumber with firestation={}", firestation);
	    List<Firestations> firestations = extractedFirestation();
	    Optional<Firestations> optionalFirestation = firestations.stream()
	            .filter(p -> p.getAddress().equals(firestation.getAddress()))
	            .findFirst();

	    if (optionalFirestation.isPresent()) {
	        Firestations updatedFirestation = rewriteFirestation(firestation, optionalFirestation);
	        logger.info("Updating firestation number for address={} to station={}", firestation.getAddress(), firestation.getStation());
	        saveFirestationMapping(firestations);
	        return updatedFirestation;
	    }

	    logger.warn("No firestation found with address={} to update.", firestation.getAddress());
	    return null;
	}

	private Firestations rewriteFirestation(Firestations firestation, Optional<Firestations> optionalFirestation) {
	    logger.debug("Starting method rewriteFirestation with firestation={}", firestation);
	    Firestations p = optionalFirestation.get();
	    p.setStation(firestation.getStation());
	    logger.debug("Firestation updated: {}", p);
	    return p;
	}

	private List<Firestations> extractedFirestation() {
	    logger.debug("Starting method extractedFirestation...");
	    try {
	        List<Firestations> firestations = jsonFileConnect.getAllFirestations();
	        logger.info("Successfully fetched extracted firestations.");
	        return firestations;
	    } catch (IOException e) {
	        logger.error("Error extracting firestations: {}", e.getMessage(), e);
	        return Collections.emptyList();
	    }
	}
	
	public boolean deleteFirestationMapping(String address) {
	    logger.debug("Starting method deleteFirestationMapping with address={}", address);
	    try {
	        logger.debug("Fetching firestation data from JSON...");
	        List<Firestations> firestations = jsonFileConnect.getAllFirestations();

	        for (int i = 0; i < firestations.size(); i++) {
	            Firestations firestation = firestations.get(i);
	            if (firestation.getAddress().equals(address)) {
	                logger.info("Firestation mapping found for address={}, deleting...", address);
	                firestations.remove(i);
	                saveFirestationMapping(firestations);
	                logger.info("Firestation mapping for address={} successfully deleted.", address);
	                return true;
	            }
	        }

	        logger.warn("No firestation mapping found for address={}.", address);
	    } catch (IOException e) {
	        logger.error("Error occurred while deleting firestation mapping for address {}: {}", address, e.getMessage(), e);
	    }

	    logger.debug("Firestation mapping deletion process completed with address={} not found.", address);
	    return false;
	}


	public Map<String, Object> getPersonsByStation(int stationNumber) {
	    logger.debug("Starting method getPersonsByStation with stationNumber={}", stationNumber);
	    Map<String, Object> result = new HashMap<>();
	    try {
	        logger.debug("Fetching firestations from JSON...");
	        List<Firestations> firestations = jsonFileConnect.getAllFirestations();
	        List<String> addresses = firestations.stream()
	                .filter(firestation -> firestation.getStation().equals(String.valueOf(stationNumber)))
	                .map(Firestations::getAddress)
	                .toList();
	        logger.info("Found {} addresses for stationNumber {}", addresses.size(), stationNumber);

	        if (addresses.isEmpty()) {
	            logger.info("No addresses found for stationNumber {}", stationNumber);
	            result.put("persons", Collections.emptyList());
	            result.put("adults", 0);
	            result.put("children", 0);
	            return result;
	        }

	        logger.debug("Addresses: {}", addresses);
	        List<Person> persons = jsonFileConnect.getAllPersons();
	        List<Map<String, String>> filteredPersons = persons.stream()
	                .filter(person -> addresses.contains(person.getAddress()))
	                .map(person -> Map.of(
	                        "firstName", person.getFirstName(),
	                        "lastName", person.getLastName(),
	                        "address", person.getAddress(),
	                        "phone", person.getPhone()))
	                .toList();

	        logger.debug("Matching persons: {}", filteredPersons);
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

	        logger.info("Found {} adults and {} children for stationNumber {}", adults, children, stationNumber);
	        result.put("persons", filteredPersons);
	        result.put("adults", adults);
	        result.put("children", children);
	    } catch (IOException e) {
	        logger.error("Error processing stationNumber {}: {}", stationNumber, e.getMessage(), e);
	    }
	    return result;
	}

	public List<String> getPhoneByFirestationNumber(String firestation) {
	    logger.debug("Starting method getPhoneByFirestationNumber with firestation={}", firestation);
	    try {
	        logger.debug("Fetching firestations from JSON...");
	        List<Firestations> firestations = jsonFileConnect.getAllFirestations();

	        List<String> addresses = firestations.stream()
	                .filter(station -> firestation.equals(station.getStation()))
	                .map(Firestations::getAddress)
	                .collect(Collectors.toList());
	        logger.info("Found {} address(es) for firestation {}", addresses.size(), firestation);

	        if (addresses.isEmpty()) {
	            logger.warn("No addresses found for firestation {}", firestation);
	            return List.of();
	        }

	        logger.debug("Fetching persons from JSON...");
	        List<Person> persons = jsonFileConnect.getAllPersons();
	        List<String> phoneNumbers = persons.stream()
	                .filter(person -> addresses.contains(person.getAddress()))
	                .map(Person::getPhone)
	                .distinct()
	                .collect(Collectors.toList());
	        logger.info("Retrieved {} unique phone number(s) for firestation {}", phoneNumbers.size(), firestation);

	        return phoneNumbers;
	    } catch (IOException e) {
	        logger.error("Error occurred while processing firestation {}: {}", firestation, e.getMessage(), e);
	        return List.of();
	    }
	}

	public FireResponseDTO getHouseMembersAndFirestationByAddress(String address) {
	    logger.debug("Starting method getHouseMembersAndFirestationByAddress with address={}", address);
	    try {
	        logger.debug("Fetching data from JSON...");
	        List<Firestations> firestations = jsonFileConnect.getAllFirestations();
	        List<Person> persons = jsonFileConnect.getAllPersons();
	        List<MedicalRecords> medicalRecords = jsonFileConnect.getAllMedicalRecords();

	        logger.debug("Finding station number for address={}", address);
	        String stationNumber = firestations.stream()
	                .filter(firestation -> firestation.getAddress().equals(address))
	                .map(Firestations::getStation)
	                .findFirst()
	                .orElse(null);

	        if (stationNumber == null) {
	            logger.warn("No station found for address={}", address);
	            return null;
	        }

	        logger.info("Station number {} found for address={}", stationNumber, address);

	        logger.debug("Getting house members for address={}", address);
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

	        logger.info("Retrieved {} house members for address={}", houseMembers.size(), address);
	        return new FireResponseDTO(stationNumber, houseMembers);

	    } catch (IOException e) {
	        logger.error("Error reading data for address {}: {}", address, e.getMessage(), e);
	        return null;
	    }
	}

	public List<FloodDTO> getFloodInfoByStation(String station) {
	    logger.debug("Starting method getFloodInfoByStation with station={}", station);
	    try {
	        logger.debug("Fetching data from JSON...");
	        List<Firestations> firestations = jsonFileConnect.getAllFirestations();
	        List<Person> persons = jsonFileConnect.getAllPersons();
	        List<MedicalRecords> medicalRecords = jsonFileConnect.getAllMedicalRecords();

	        logger.debug("Finding addresses for station={}", station);
	        List<String> addresses = firestations.stream()
	                .filter(firestation -> station.equals(firestation.getStation()))
	                .map(Firestations::getAddress)
	                .collect(Collectors.toList());

	        logger.info("Found {} address(es) for station {}", addresses.size(), station);

	        List<FloodDTO> floodInfo = addresses.stream()
	                .map(address -> {
	                    logger.debug("Fetching residents for address={}", address);
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

	        logger.info("Retrieved flood info for {} address(es) in station {}", floodInfo.size(), station);
	        return floodInfo;

	    } catch (IOException e) {
	        logger.error("Error occurred while processing flood info for station {}: {}", station, e.getMessage(), e);
	        return List.of();
	    }
	}
	
}
