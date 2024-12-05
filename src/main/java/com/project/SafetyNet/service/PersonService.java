package com.project.SafetyNet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.SafetyNet.dto.ChildAlertDTO;
import com.project.SafetyNet.dto.PersonInfoLastNameDTO;
import com.project.SafetyNet.model.Firestations;
import com.project.SafetyNet.model.MedicalRecords;
import com.project.SafetyNet.model.Person;
import com.project.SafetyNet.repository.JsonFileConnect;
import com.project.SafetyNet.tool.CalculateAge;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class PersonService {
	
	private final JsonFileConnect jsonFileConnect;
	
	@Autowired
	public PersonService(JsonFileConnect jsonFileConnect) {
		this.jsonFileConnect = jsonFileConnect;
	}
	
	public List<Person> getAllPersons() {
		try {
			return jsonFileConnect.getAllPersons();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void addPerson(Person person) {
		try {
			List<Person> persons = jsonFileConnect.getAllPersons();
			persons.add(person);
			savePersonData(persons);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Person updatePerson(Person person) {
		List<Person> persons = extractedPerson();
			Optional<Person> optionalPerson = persons.stream().filter(p -> p.getFirstName().equals(person.getFirstName()) && p.getLastName().equals(person.getLastName())).findFirst();
			if (optionalPerson.isPresent()) {
			 Person p = rewritePerson(person, optionalPerson);
				savePersonData(persons);
				return p;
			}
		return null;
	}

	private List<Person> extractedPerson() {
		try {
			return jsonFileConnect.getAllPersons();
		
		} catch (IOException e) {
			e.printStackTrace();
			
		}
		return Collections.emptyList();
	}

	private Person rewritePerson(Person person, Optional<Person> optionalPerson) {
		Person p = optionalPerson.get();
			p.setAddress(person.getAddress());
			p.setCity(person.getCity());
			p.setZip(person.getZip());
			p.setPhone(person.getPhone());
			p.setEmail(person.getEmail());
		return p;
	}

	private void savePersonData(List<Person> persons) {
		try {
			jsonFileConnect.saveAllPersons(persons);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean deletePerson(String firstName, String lastName) {
		try {
			List<Person> persons = jsonFileConnect.getAllPersons();
			for (int i = 0; i < persons.size(); i++) {
				Person p = persons.get(i);
				if (p.getFirstName().equals(firstName) && p.getLastName().equals(lastName)) {
					persons.remove(i);
					savePersonData(persons);
					return true;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public List<String> getEmailsByCity(String city) {
		List<Person> persons = getAllPersons();
		List<String> emails = persons.stream()
				.filter(person -> person.getCity().equalsIgnoreCase(city))
				.map(Person::getEmail)
				.collect(Collectors.toList());
		return emails;
	}

	public List<ChildAlertDTO> getChildAlertByAddress(String address) {
		try {
			List<Person> persons = jsonFileConnect.getAllPersons();
			List<MedicalRecords> medicalRecords = jsonFileConnect.getAllMedicalRecords();
			List<Person> residentsAtTheAddress = persons.stream()
					.filter(person -> address.equals(person.getAddress()))
					.collect(Collectors.toList());
			if (residentsAtTheAddress.isEmpty()) {
				return new ArrayList<>();
			}
			List<ChildAlertDTO> childAlerts = new ArrayList<>();
			for (Person resident : residentsAtTheAddress) {
				MedicalRecords medicalRecord = medicalRecords.stream()
						.filter(record -> record.getFirstName().equals(resident.getFirstName()) && record.getLastName().equals(resident.getLastName()))
						.findFirst()
						.orElse(null);
				if (medicalRecord != null) {
					int age = CalculateAge.calculateAge(medicalRecord.getBirthdate());
					if (!CalculateAge.isOverEighteen(medicalRecord.getBirthdate())) {
						List<String> houseMembers = residentsAtTheAddress.stream()
								.filter(otherMember -> !otherMember.equals(resident))
								.map(otherMember -> otherMember.getFirstName() + " " + otherMember.getLastName())
								.collect(Collectors.toList());
						childAlerts.add(new ChildAlertDTO(resident.getFirstName(), resident.getLastName(), age, houseMembers));
					}
				}
			}
			return childAlerts;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public List<PersonInfoLastNameDTO> getAllPersonsInfoByLastName(String lastName) {
		try {
	        List<Person> persons = jsonFileConnect.getAllPersons();
	        List<MedicalRecords> medicalRecords = jsonFileConnect.getAllMedicalRecords();

	        return persons.stream()
	                .filter(person -> person.getLastName().equalsIgnoreCase(lastName))
	                .map(person -> {
	                    MedicalRecords medicalRecord = medicalRecords.stream()
	                            .filter(record -> record.getFirstName().equals(person.getFirstName()) &&
	                                              record.getLastName().equals(person.getLastName()))
	                            .findFirst()
	                            .orElse(null);

	                    int age = (medicalRecord != null) ? CalculateAge.calculateAge(medicalRecord.getBirthdate()) : -1;
                
	                    return new PersonInfoLastNameDTO(
	                            person.getLastName(),
	                            person.getAddress(),
	                            age,
	                            person.getEmail(),
	                            medicalRecord != null ? medicalRecord.getMedications() : List.of(),
	                            medicalRecord != null ? medicalRecord.getAllergies() : List.of()
	                    );
	                })
	                .collect(Collectors.toList());
	    } catch (IOException e) {
	        e.printStackTrace();
	        return Collections.emptyList();
	    }
	}
	
	
}

















// Other method for updatePerson:
//persons.forEach(System.out::println);
//for (Person p : persons) {
//	if (p.getFirstName().equals(person.getFirstName()) && p.getLastName().equals(person.getLastName())) {
//		p.setAddress(person.getAddress());
//		p.setCity(person.getCity());
//		p.setZip(person.getZip());
//		p.setPhone(person.getPhone());
//		p.setEmail(person.getEmail());
//		jsonFileConnect.saveAllPersons(persons);
//		return person;
//	}
//}
