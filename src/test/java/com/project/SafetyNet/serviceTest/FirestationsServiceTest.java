package com.project.SafetyNet.serviceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.project.SafetyNet.model.Firestations;
import com.project.SafetyNet.model.MedicalRecords;
import com.project.SafetyNet.model.Person;
import com.project.SafetyNet.repository.JsonFileConnect;
import com.project.SafetyNet.service.FirestationsService;


public class FirestationsServiceTest {

	@Mock
	private JsonFileConnect jsonFileConnect;
	
	@InjectMocks
	private FirestationsService firestationsService;

	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	void testGetAllFirestationsMapping() throws IOException {
		Firestations firestation1 = new Firestations();
		firestation1.setAddress("rue de la paix");
		firestation1.setStation("2");
		Firestations firestation2 = new Firestations();
		firestation2.setAddress("avenue des champs");
		firestation2.setStation("4");
		List<Firestations> firestations = Arrays.asList(firestation1, firestation2);
		when(jsonFileConnect.getAllFirestations()).thenReturn(firestations);
		List<Firestations> result = firestationsService.getAllFirestations();
		assertEquals(2, result.size());
		assertEquals("rue de la paix", result.get(0).getAddress());
		assertEquals("4", result.get(1).getStation());
	}
	
	@Test
	void testAddFirestationMapping() throws IOException {
		Firestations firestation = new Firestations();
		firestation.setAddress("36 rue de la liberté");
		firestation.setStation("2");
		List<Firestations> firestations = new ArrayList<>(Arrays.asList(firestation));
		when(jsonFileConnect.getAllFirestations()).thenReturn(firestations);
		firestationsService.addFirestationMapping(firestation);
		verify(jsonFileConnect, times(1)).saveAllFirestations(firestations);
	}
	
	@Test 
	void testUpdateFirestationMapping() throws IOException {
		Firestations firestation = new Firestations();
		firestation.setAddress("12bis boulevard de la rue");
		firestation.setStation("1");
		List<Firestations> firestations = new ArrayList<>(Arrays.asList(firestation));
		when(jsonFileConnect.getAllFirestations()).thenReturn(firestations);
		Firestations updatedFirestationMapping = new Firestations();
		updatedFirestationMapping.setAddress("12bis boulevard de la rue");
		updatedFirestationMapping.setStation("3");
		Firestations result = firestationsService.updateFirestationNumber(updatedFirestationMapping);
		assertEquals("3", result.getStation());
	}
	
	@Test
	void testDeleteFirestationMapping() throws IOException {
		Firestations firestation = new Firestations();
		firestation.setAddress("5 impasse sans fin");
		firestation.setStation("42");
		List<Firestations> firestations = new ArrayList<>(Arrays.asList(firestation));
		when(jsonFileConnect.getAllFirestations()).thenReturn(firestations);
		boolean isDeleted = firestationsService.deleteFirestationMapping("5 impasse sans fin");
		assertEquals(true, isDeleted);
	}
	
	@Test
    void testGetPersonsByStation() throws IOException {
		 	Firestations firestation1 = new Firestations();
	     	firestation1.setStation("1");
	        firestation1.setAddress("123 Main St");

	        Firestations firestation2 = new Firestations();
	        firestation2.setStation("2");
	        firestation2.setAddress("456 Oak St");

	        List<Firestations> firestations = Arrays.asList(firestation1, firestation2);

	        Person person1 = new Person();
	        person1.setFirstName("Jean");
	        person1.setLastName("Doe");
	        person1.setAddress("123 Main St");
	        person1.setPhone("123-456-7890");

	        Person person2 = new Person();
	        person2.setFirstName("Jane");
	        person2.setLastName("Smith");
	        person2.setAddress("123 Main St");
	        person2.setPhone("234-567-8901");


	        MedicalRecords medicalRecord1 = new MedicalRecords();
	        medicalRecord1.setFirstName("Jean");
	        medicalRecord1.setLastName("Doe");
	        medicalRecord1.setBirthdate("01/01/1990");

	        MedicalRecords medicalRecord2 = new MedicalRecords();
	        medicalRecord2.setFirstName("Jane");
	        medicalRecord2.setLastName("Smith");
	        medicalRecord2.setBirthdate("01/01/2010");

	        when(jsonFileConnect.getAllFirestations()).thenReturn(firestations);
	        when(jsonFileConnect.getAllPersons()).thenReturn(Arrays.asList(person1, person2));
	        when(jsonFileConnect.getAllMedicalRecords()).thenReturn(Arrays.asList(medicalRecord1, medicalRecord2));   

	        Map<String, Object> result = firestationsService.getPersonsByStation(1);
	        assertNotNull(result);
	        assertTrue(result.containsKey("persons"));
	        assertTrue(result.containsKey("adults"));
	        assertTrue(result.containsKey("children"));
	        
	        List<Map<String, String>> persons = (List<Map<String, String>>) result.get("persons");
	        assertEquals(2, persons.size());
	        int adults = (int) result.get("adults");
	        int children = (int) result.get("children");
	        assertEquals(1, adults);
	        assertEquals(1, children);
	}
}
