package com.project.SafetyNet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.SafetyNet.model.Person;
import com.project.SafetyNet.repository.JsonFileConnect;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

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
			jsonFileConnect.saveAllPersons(persons);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Person updatePerson(Person person) {
		try {
			List<Person> persons = jsonFileConnect.getAllPersons();
			Optional<Person> optionalPerson = persons.stream().filter(p -> p.getFirstName().equals(person.getFirstName()) && p.getLastName().equals(person.getLastName())).findFirst();
			optionalPerson.ifPresent(p -> {
				p.setAddress(person.getAddress());
				p.setCity(person.getCity());
				p.setZip(person.getZip());
				p.setPhone(person.getPhone());
				p.setEmail(person.getEmail());
				try {
					jsonFileConnect.saveAllPersons(persons);
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
			
			return optionalPerson.orElse(null);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean deletePerson(String firstName, String lastName) {
		try {
			List<Person> persons = jsonFileConnect.getAllPersons();
			for (int i = 0; i < persons.size(); i++) {
				Person p = persons.get(i);
				if (p.getFirstName().equals(firstName) && p.getLastName().equals(lastName)) {
					persons.remove(i);
					jsonFileConnect.saveAllPersons(persons);
					return true;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
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
