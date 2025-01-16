package com.project.SafetyNet.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    private static final Logger successLogger = LogManager.getLogger("com.project.success");
    private static final Logger errorLogger = LogManager.getLogger("com.project.error");
    private static final Logger debugLogger = LogManager.getLogger("com.project.debug");

    @Autowired
    public PersonService(JsonFileConnect jsonFileConnect) {
        this.jsonFileConnect = jsonFileConnect;
    }

    public List<Person> getAllPersons() {
        try {
            debugLogger.debug("Fetching all persons");
            List<Person> persons = jsonFileConnect.getAllPersons();
            successLogger.info("Successfully retrieved {} persons.", persons.size());
            return persons;
        } catch (IOException e) {
            errorLogger.error("Error fetching all persons: {}", e.getMessage(), e);
            return null;
        }
    }

    public void addPerson(Person person) {
        try {
            debugLogger.debug("Adding new person: {}", person);
            List<Person> persons = jsonFileConnect.getAllPersons();
            persons.add(person);
            savePersonData(persons);
            successLogger.info("Successfully added person {} {}", person.getFirstName(), person.getLastName());
        } catch (IOException e) {
            errorLogger.error("Error adding person {}: {}", person, e.getMessage(), e);
        }
    }

    public Person updatePerson(Person person) {
        debugLogger.debug("Updating person: {}", person);
        List<Person> persons = extractedPerson();
        Optional<Person> optionalPerson = persons.stream()
                .filter(p -> p.getFirstName().equals(person.getFirstName()) && p.getLastName().equals(person.getLastName()))
                .findFirst();

        if (optionalPerson.isPresent()) {
            Person updatedPerson = rewritePerson(person, optionalPerson);
            savePersonData(persons);
            successLogger.info("Successfully updated person {} {}", person.getFirstName(), person.getLastName());
            return updatedPerson;
        } else {
            errorLogger.error("No person found with name {} {}", person.getFirstName(), person.getLastName());
            return null;
        }
    }

    private List<Person> extractedPerson() {
        try {
            debugLogger.debug("Extracting all persons");
            return jsonFileConnect.getAllPersons();
        } catch (IOException e) {
            errorLogger.error("Error extracting persons: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    private Person rewritePerson(Person person, Optional<Person> optionalPerson) {
        Person existingPerson = optionalPerson.get();
        debugLogger.debug("Updating fields for person: {}", existingPerson);
        existingPerson.setAddress(person.getAddress());
        existingPerson.setCity(person.getCity());
        existingPerson.setZip(person.getZip());
        existingPerson.setPhone(person.getPhone());
        existingPerson.setEmail(person.getEmail());
        successLogger.info("Successfully updated fields for person {} {}", existingPerson.getFirstName(), existingPerson.getLastName());
        return existingPerson;
    }

    private void savePersonData(List<Person> persons) {
        try {
            debugLogger.debug("Saving person data");
            jsonFileConnect.saveAllPersons(persons);
            successLogger.info("Successfully saved all person data.");
        } catch (IOException e) {
            errorLogger.error("Error saving person data: {}", e.getMessage(), e);
        }
    }

    public boolean deletePerson(String firstName, String lastName) {
        try {
            debugLogger.debug("Attempting to delete person {} {}", firstName, lastName);
            List<Person> persons = jsonFileConnect.getAllPersons();
            for (int i = 0; i < persons.size(); i++) {
                Person p = persons.get(i);
                if (p.getFirstName().equals(firstName) && p.getLastName().equals(lastName)) {
                    persons.remove(i);
                    savePersonData(persons);
                    successLogger.info("Successfully deleted person {} {}", firstName, lastName);
                    return true;
                }
            }
            errorLogger.error("No person found with name {} {}", firstName, lastName);
            return false;
        } catch (IOException e) {
            errorLogger.error("Error deleting person {} {}: {}", firstName, lastName, e.getMessage(), e);
            return false;
        }
    }

    public List<String> getEmailsByCity(String city) {
        debugLogger.debug("Fetching emails for city: {}", city);
        List<Person> persons = getAllPersons();
        List<String> emails = persons.stream()
                .filter(person -> person.getCity().equalsIgnoreCase(city))
                .map(Person::getEmail)
                .collect(Collectors.toList());
        successLogger.info("Successfully retrieved {} emails for city {}", emails.size(), city);
        return emails;
    }

    public List<ChildAlertDTO> getChildAlertByAddress(String address) {
        try {
            debugLogger.debug("Fetching child alerts for address: {}", address);
            List<Person> persons = jsonFileConnect.getAllPersons();
            List<MedicalRecords> medicalRecords = jsonFileConnect.getAllMedicalRecords();

            List<Person> residentsAtTheAddress = persons.stream()
                    .filter(person -> address.equals(person.getAddress()))
                    .collect(Collectors.toList());

            if (residentsAtTheAddress.isEmpty()) {
                errorLogger.error("No residents found at address {}", address);
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
            successLogger.info("Successfully retrieved {} child alerts for address {}", childAlerts.size(), address);
            return childAlerts;
        } catch (IOException e) {
            errorLogger.error("Error fetching child alerts for address {}: {}", address, e.getMessage(), e);
            return null;
        }
    }

    public List<PersonInfoLastNameDTO> getAllPersonsInfoByLastName(String lastName) {
        try {
            debugLogger.debug("Fetching person info by last name: {}", lastName);
            List<Person> persons = jsonFileConnect.getAllPersons();
            List<MedicalRecords> medicalRecords = jsonFileConnect.getAllMedicalRecords();

            List<PersonInfoLastNameDTO> personInfoList = persons.stream()
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
            

            successLogger.info("Successfully retrieved {} person info entries for last name {}", personInfoList.size(), lastName);
            return personInfoList;
        } catch (IOException e) {
            errorLogger.error("Error fetching person info by last name {}: {}", lastName, e.getMessage(), e);
            return Collections.emptyList();
        }
    }
}
