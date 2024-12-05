package com.project.SafetyNet.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.SafetyNet.dto.ChildAlertDTO;
import com.project.SafetyNet.dto.PersonInfoLastNameDTO;
import com.project.SafetyNet.model.Person;
import com.project.SafetyNet.service.PersonService;

@RestController
@RequestMapping
public class PersonController {
	
	private final PersonService personService;
	
	@Autowired
	public PersonController(PersonService personService) {
		this.personService = personService;
	}
	
	@GetMapping("/person")
	public List<Person> getAllPersons() {
		return personService.getAllPersons();
	}
	
	@PostMapping("/person")
	public ResponseEntity<Person> addPerson(@RequestBody Person person) {
		try {
			personService.addPerson(person);
			return new ResponseEntity<>(person, HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/person")
	public ResponseEntity<Person> updatePerson(@RequestBody Person person) {
		Person updatedPerson = personService.updatePerson(person);
		if (updatedPerson != null) {
			return new ResponseEntity<>(updatedPerson, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@DeleteMapping("/person")
	public ResponseEntity<Void> deletePerson(@RequestParam String firstName, @RequestParam String lastName) {
		boolean isDeleted = personService.deletePerson(firstName,  lastName);
		if (isDeleted) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/communityEmail")
	public ResponseEntity<List<String>> getEmailsByCity(@RequestParam String city) {
		List<String> emails = personService.getEmailsByCity(city);
		if (emails.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(emails);
	}
	
	@GetMapping("/childAlert")
	public ResponseEntity<List<ChildAlertDTO>> getChildAlertByAddress(@RequestParam String address) {
		List<ChildAlertDTO> children = personService.getChildAlertByAddress(address);
		if (children.isEmpty()) {
			return ResponseEntity.ok(Collections.emptyList());
		}
		return ResponseEntity.ok(children);
	}
	
	@GetMapping("/personInfo")
	public ResponseEntity<List<PersonInfoLastNameDTO>> getAllPersonsInfoByLastName(@RequestParam String lastName) {
		List<PersonInfoLastNameDTO> personInfos = personService.getAllPersonsInfoByLastName(lastName);
		if (personInfos.isEmpty()) {
			return ResponseEntity.ok(Collections.emptyList());
		}
		return ResponseEntity.ok(personInfos);
	}
}
