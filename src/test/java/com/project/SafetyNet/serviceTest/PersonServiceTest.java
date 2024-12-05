package com.project.SafetyNet.serviceTest;

import com.project.SafetyNet.model.Person;
import com.project.SafetyNet.repository.JsonFileConnect;
import com.project.SafetyNet.service.PersonService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test; 
import org.mockito.InjectMocks; 
import org.mockito.Mock; 
import org.mockito.MockitoAnnotations; 
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays; 
import java.util.List; 
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class PersonServiceTest {

	 @Mock 
	 private JsonFileConnect jsonFileConnect;
	 
	 @InjectMocks 
	 private PersonService personService;
	 
	 @BeforeEach
	 void setUp() {
		 MockitoAnnotations.openMocks(this); 
		 }
	 
	 @Test 
	 void testGetAllPersons() throws IOException {
		 Person person1 = new Person();
		 person1.setFirstName("Jean"); 
		 person1.setLastName("Jacque"); 
		 Person person2 = new Person();
		 person2.setFirstName("Ber"); 
		 person2.setLastName("Nadette"); 
		 List<Person> persons = Arrays.asList(person1, person2);
		 when(jsonFileConnect.getAllPersons()).thenReturn(persons);
		 List<Person> result = personService.getAllPersons();
		 assertEquals(2, result.size()); 
		 assertEquals("Jean", result.get(0).getFirstName());
		 assertEquals("Ber", result.get(1).getFirstName()); 
		 } 
	 
	 @Test void testAddPerson() throws IOException { 
		 Person person = new Person();
		 person.setFirstName("Jean"); 
		 person.setLastName("Jacque"); 
		 List<Person> persons = new ArrayList<>(Arrays.asList(person));
		 when(jsonFileConnect.getAllPersons()).thenReturn(persons);
		 personService.addPerson(person); 
		 verify(jsonFileConnect, times(1)).saveAllPersons(persons);
	 }
	
	 @Test 
	 void testUpdatePerson() throws IOException { 
		 Person person = new Person();
		 person.setFirstName("Jean");
		 person.setLastName("Jacque");
		 person.setAddress("123 Main St"); 
		 List<Person> persons = Arrays.asList(person);
		 when(jsonFileConnect.getAllPersons()).thenReturn(persons);
		 Person updatedPerson = new Person();
		 updatedPerson.setFirstName("Jean");
		 updatedPerson.setLastName("Jacque"); 
		 updatedPerson.setAddress("456 Oak St");
		 Person result = personService.updatePerson(updatedPerson);
		 assertEquals("456 Oak St", result.getAddress());
		 }
	 
	 @Test 
	 void testDeletePerson() throws IOException {
		 Person person = new Person();
		 person.setFirstName("Jean");
		 person.setLastName("Jacque");
		 List<Person> persons = new ArrayList<>(Arrays.asList(person));
		 when(jsonFileConnect.getAllPersons()).thenReturn(persons);
		 boolean isDeleted = personService.deletePerson("Jean", "Jacque"); 
		 assertEquals(true, isDeleted);
		 }
	 
	 @Test
	 void testGetEmailsByCity() throws IOException {
		 Person person1 = new Person();
		 person1.setCity("grenoble");
		 person1.setEmail("test@test.com");
		 Person person2 = new Person();
		 person2.setCity("grenoble");
		 person2.setEmail("test2@test.com");
		 when(jsonFileConnect.getAllPersons()).thenReturn(Arrays.asList(person1, person2));
		 List<String> emails = personService.getEmailsByCity("grenoble");
		 assertEquals(2, emails.size());
		 assertTrue(emails.contains("test@test.com"));
		 assertTrue(emails.contains("test2@test.com"));
	 }
}
