package com.project.SafetyNet.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.SafetyNet.model.Person;
import com.project.SafetyNet.service.PersonService;

@RestController
@RequestMapping("/person")
public class PersonController {
	
	private final PersonService personService;
	
	@Autowired
	public PersonController(PersonService personService) {
		this.personService = personService;
	}
	
	@GetMapping 
	public List<Person> getAllPersons() {
		return personService.getAllPersons();
	}
	
	@PostMapping
	public ResponseEntity<Person> addPerson(@RequestBody Person person) {
		personService.addPerson(person);
		return new ResponseEntity<>(person, HttpStatus.CREATED);
	}
	
	@PutMapping
	public ResponseEntity<Person> updatePerson(@RequestBody Person person) {
		Person updatedPerson = personService.updatePerson(person);
		if (updatedPerson != null) {
			return new ResponseEntity<>(updatedPerson, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@DeleteMapping
	public ResponseEntity<Void> deletePerson(@RequestParam String firstName, @RequestParam String lastName) {
		boolean isDeleted = personService.deletePerson(firstName,  lastName);
		if (isDeleted) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}
