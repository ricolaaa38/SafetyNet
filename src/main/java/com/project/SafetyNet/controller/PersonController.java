package com.project.SafetyNet.controller;

import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    private static final Logger successLogger = LogManager.getLogger("com.project.success");
    private static final Logger errorLogger = LogManager.getLogger("com.project.error");
    private static final Logger debugLogger = LogManager.getLogger("com.project.debug");

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/person")
    public List<Person> getAllPersons() {
        debugLogger.debug("Fetching all persons.");
        List<Person> persons = personService.getAllPersons();
        successLogger.info("Successfully fetched all persons.");
        return persons;
    }

    @PostMapping("/person")
    public ResponseEntity<Person> addPerson(@RequestBody Person person) {
        debugLogger.debug("Adding new person: {}", person);
        try {
            personService.addPerson(person);
            successLogger.info("Successfully added person: {}", person);
            return new ResponseEntity<>(person, HttpStatus.CREATED);
        } catch (Exception e) {
            errorLogger.error("Error adding person: {}", person, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/person")
    public ResponseEntity<Person> updatePerson(@RequestBody Person person) {
        debugLogger.debug("Updating person: {}", person);
        try {
            Person updatedPerson = personService.updatePerson(person);
            if (updatedPerson != null) {
                successLogger.info("Successfully updated person: {}", updatedPerson);
                return new ResponseEntity<>(updatedPerson, HttpStatus.OK);
            } else {
                errorLogger.error("Person not found for update: {}", person);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            errorLogger.error("Error updating person: {}", person, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/person")
    public ResponseEntity<Void> deletePerson(@RequestParam String firstName, @RequestParam String lastName) {
        debugLogger.debug("Deleting person: {} {}", firstName, lastName);
        try {
            boolean isDeleted = personService.deletePerson(firstName, lastName);
            if (isDeleted) {
                successLogger.info("Successfully deleted person: {} {}", firstName, lastName);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                errorLogger.error("Person not found for deletion: {} {}", firstName, lastName);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            errorLogger.error("Error deleting person: {} {}", firstName, lastName, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/communityEmail")
    public ResponseEntity<List<String>> getEmailsByCity(@RequestParam String city) {
        debugLogger.debug("Fetching emails for city: {}", city);
        List<String> emails = personService.getEmailsByCity(city);
        if (emails.isEmpty()) {
            successLogger.info("No emails found for city: {}", city);
            return ResponseEntity.noContent().build();
        }
        successLogger.info("Successfully fetched emails for city: {}", city);
        return ResponseEntity.ok(emails);
    }

    @GetMapping("/childAlert")
    public ResponseEntity<List<ChildAlertDTO>> getChildAlertByAddress(@RequestParam String address) {
        debugLogger.debug("Fetching child alert for address: {}", address);
        List<ChildAlertDTO> children = personService.getChildAlertByAddress(address);
        if (children.isEmpty()) {
            successLogger.info("No children found at address: {}", address);
            return ResponseEntity.ok(Collections.emptyList());
        }
        successLogger.info("Successfully fetched child alert for address: {}", address);
        return ResponseEntity.ok(children);
    }

    @GetMapping("/personInfo")
    public ResponseEntity<List<PersonInfoLastNameDTO>> getAllPersonsInfoByLastName(@RequestParam String lastName) {
        debugLogger.debug("Fetching person info for last name: {}", lastName);
        List<PersonInfoLastNameDTO> personInfos = personService.getAllPersonsInfoByLastName(lastName);
        if (personInfos.isEmpty()) {
            successLogger.info("No person info found for last name: {}", lastName);
            return ResponseEntity.noContent().build();
        }
        successLogger.info("Successfully fetched person info for last name: {}", lastName);
        return ResponseEntity.ok(personInfos);
    }
}
