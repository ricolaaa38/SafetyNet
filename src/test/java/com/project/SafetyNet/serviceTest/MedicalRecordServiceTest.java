package com.project.SafetyNet.serviceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.project.SafetyNet.model.MedicalRecords;
import com.project.SafetyNet.repository.JsonFileConnect;
import com.project.SafetyNet.service.MedicalRecordsService;

public class MedicalRecordServiceTest {

	@Mock
	private JsonFileConnect jsonFileConnect;
	
	@InjectMocks
	private MedicalRecordsService medicalRecordService;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	void testGetAllMedicalRecords() throws IOException {
		MedicalRecords medicalRecord1 = new MedicalRecords();
		medicalRecord1.setFirstName("pierre");
		medicalRecord1.setLastName("paul");
		MedicalRecords medicalRecord2 = new MedicalRecords();
		medicalRecord2.setFirstName("bob");
		medicalRecord2.setLastName("patrick");
		List<MedicalRecords> medicalRecords = Arrays.asList(medicalRecord1, medicalRecord2);
		when(jsonFileConnect.getAllMedicalRecords()).thenReturn(medicalRecords);
		List<MedicalRecords> result = medicalRecordService.getAllMedicalRecords();
		assertEquals(2, result.size());
		assertEquals("pierre", result.get(0).getFirstName());
		assertEquals("patrick", result.get(1).getLastName());
	}
	
	@Test
	void testAddMedicalRecord() throws IOException {
		MedicalRecords medicalRecord = new MedicalRecords();
		medicalRecord.setFirstName("pierre");
		medicalRecord.setLastName("paul");
		medicalRecord.setBirthdate("12/12/1995");
		List<MedicalRecords> medicalRecords = new ArrayList<>(Arrays.asList(medicalRecord));
		when(jsonFileConnect.getAllMedicalRecords()).thenReturn(medicalRecords);
		medicalRecordService.addMedicalRecord(medicalRecord);
		verify(jsonFileConnect, times(1)).saveAllMedicalRecords(medicalRecords);
	}
	
	@Test 
	 void testUpdateMedicalRecord() throws IOException { 
		 MedicalRecords medicalRecord = new MedicalRecords();
		 medicalRecord.setFirstName("Jean");
		 medicalRecord.setLastName("Jacque");
		 medicalRecord.setBirthdate("12/12/1995"); 
		 List<MedicalRecords> medicalRecords = Arrays.asList(medicalRecord);
		 when(jsonFileConnect.getAllMedicalRecords()).thenReturn(medicalRecords);
		 MedicalRecords updatedMedicalRecord = new MedicalRecords();
		 updatedMedicalRecord.setFirstName("Jean");
		 updatedMedicalRecord.setLastName("Jacque"); 
		 updatedMedicalRecord.setBirthdate("13/10/1999");
		 MedicalRecords result = medicalRecordService.updateMedicalRecord(updatedMedicalRecord);
		 assertEquals("13/10/1999", result.getBirthdate());
		 }
	
	@Test 
	 void testDeleteMedicalRecord() throws IOException {
		MedicalRecords medicalRecord = new MedicalRecords();
		 medicalRecord.setFirstName("Jean");
		 medicalRecord.setLastName("Jacque");
		 List<MedicalRecords> medicalRecords = new ArrayList<>(Arrays.asList(medicalRecord));
		 when(jsonFileConnect.getAllMedicalRecords()).thenReturn(medicalRecords);
		 boolean isDeleted = medicalRecordService.deleteMedicalRecord("Jean", "Jacque"); 
		 assertEquals(true, isDeleted);
		 }
	
}
