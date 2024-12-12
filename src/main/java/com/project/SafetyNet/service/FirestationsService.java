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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	
	@Autowired
	public FirestationsService(JsonFileConnect jsonFileConnect) {
		this.jsonFileConnect = jsonFileConnect;
	}
	
	public List<Firestations> getAllFirestations() {
		try {
			return jsonFileConnect.getAllFirestations();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void saveFirestationMapping(List<Firestations> firestations) {
		try {
			jsonFileConnect.saveAllFirestations(firestations);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addFirestationMapping(Firestations firestation) {
		try {
			List<Firestations> firestations = jsonFileConnect.getAllFirestations();
			firestations.add(firestation);
			saveFirestationMapping(firestations);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Firestations updateFirestationNumber(Firestations firestation) {
		List<Firestations> firestations = extractedFirestation();
		Optional<Firestations> optionalFirestation = firestations.stream().filter(p -> p.getAddress().equals(firestation.getAddress())).findFirst();
		if (optionalFirestation.isPresent()) {
			Firestations p = rewriteFirestation(firestation, optionalFirestation);
			saveFirestationMapping(firestations);
			return p;	
		}
		return null;
	}

	private Firestations rewriteFirestation(Firestations firestation, Optional<Firestations> optionalFirestation) {
		Firestations p = optionalFirestation.get();
		p.setStation(firestation.getStation());
		return p;
	}

	private List<Firestations> extractedFirestation() {
		try {
			return jsonFileConnect.getAllFirestations();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
	}
	
	public boolean deleteFirestationMapping(String address) {
		try {
			List<Firestations> firestations = jsonFileConnect.getAllFirestations();
			for (int i = 0; i < firestations.size(); i++) {
				Firestations p = firestations.get(i);
				if (p.getAddress().equals(address)) {
					firestations.remove(i);
					saveFirestationMapping(firestations);
					return true;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}


	public Map<String, Object> getPersonsByStation(int stationNumber) {
		Map<String, Object> result = new HashMap<>();
		try {
			List<Firestations> firestations = jsonFileConnect.getAllFirestations();
			List<String> addresses = firestations.stream()
					.filter(firestation -> firestation.getStation().equals(String.valueOf(stationNumber)))
					.map(Firestations::getAddress)
					.toList();
			if (addresses.isEmpty()) {
				result.put("persons", Collections.emptyList());
				result.put("adults", 0);
				result.put("children", 0);
			} else {
			List<Person> persons = jsonFileConnect.getAllPersons();
			
			List<Map<String, String>> filteredPersons = persons.stream()
					.filter(person -> addresses.contains(person.getAddress()))
					.map(person -> Map.of(
	                        "firstName", person.getFirstName(),
	                        "lastName", person.getLastName(),
	                        "address", person.getAddress(),
	                        "phone", person.getPhone()
	                ))
					.toList();
			
			List<MedicalRecords> medicalRecords = jsonFileConnect.getAllMedicalRecords();
			
			int adults = 0;
			int children = 0;
			for (Map<String, String> m : filteredPersons) {
				
					MedicalRecords record = medicalRecords.stream().filter(mr -> m.get("firstName").equals(mr.getFirstName()) 
														&& m.get("lastName").equals(mr.getLastName()))
					.findFirst().get();
				if (CalculateAge.isOverEighteen(record.getBirthdate())) {
					adults++;
				} else {
					children++;
				}
				
//			CalculateAge.isOverEighteen(record.getBirthdate()) ? adults++ : children++; 						
				}
		
			
			 result.put("persons", filteredPersons);
			 result.put("adults", adults);
			 result.put("children", children);
			
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public List<String> getPhoneByFirestationNumber(String firestation) {
		try {
			List<Firestations> firestations = jsonFileConnect.getAllFirestations();
			List<String> addresses = firestations.stream()
					.filter(station -> firestation.equals(station.getStation()))
					.map(Firestations::getAddress)
					.collect(Collectors.toList());
			if (addresses.isEmpty()) {
				return List.of();
			}
			List<Person> persons = jsonFileConnect.getAllPersons();
			return persons.stream()
					.filter(person -> addresses.contains(person.getAddress()))
					.map(Person::getPhone)
					.distinct()
					.collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
			return List.of();
		}
	}

	public FireResponseDTO getHouseMembersAndFirestationByAddress(String address) {
		 try {
		        List<Firestations> firestations = jsonFileConnect.getAllFirestations();
		        List<Person> persons = jsonFileConnect.getAllPersons();
		        List<MedicalRecords> medicalRecords = jsonFileConnect.getAllMedicalRecords();

		        String stationNumber = firestations.stream()
		                .filter(firestation -> firestation.getAddress().equals(address))
		                .map(Firestations::getStation)
		                .findFirst()
		                .orElse(null);

		        if (stationNumber == null) {
		            return null;
		        }

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

		        return new FireResponseDTO(stationNumber, houseMembers);

		    } catch (IOException e) {
		        System.err.println("Erreur lors de la lecture des données JSON : " + e.getMessage());
		        e.printStackTrace();
		        return null;
		    }
	}

	public List<FloodDTO> getFloodInfoByStation(String station) {
		try {
	        List<Firestations> firestations = jsonFileConnect.getAllFirestations();
	        List<Person> persons = jsonFileConnect.getAllPersons();
	        List<MedicalRecords> medicalRecords = jsonFileConnect.getAllMedicalRecords();

	        List<String> addresses = firestations.stream()
	                .filter(firestation -> station.equals(firestation.getStation()))
	                .map(Firestations::getAddress)
	                .collect(Collectors.toList());

	        List<FloodDTO> floodInfo = addresses.stream()
	        		.map(address -> {
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

	        return floodInfo;
	    } catch (IOException e) {
	        e.printStackTrace();
	        return List.of();
	    }
	}

	
}
