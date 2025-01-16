package com.project.SafetyNet.integrationTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import java.util.Arrays;
import java.util.List;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.project.SafetyNet.model.MedicalRecords;


@SpringBootTest
@AutoConfigureMockMvc
public class MedicalRecordsControllerIT {

	@Autowired
	private MockMvc mockMvc;
	

	
	private ObjectMapper objectMapper = new ObjectMapper();
	

	
	@Test
	void testGetAllMedicalRecords() throws Exception {
		mockMvc.perform(get("/medicalRecord"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$[0].firstName").value("John"))
		.andExpect(jsonPath("$[1].firstName").value("Jacob"))
		.andDo(result -> {
			System.out.println(result.getResponse().getContentAsString());
			List<MedicalRecords> medicalRecords = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<MedicalRecords>>() {});
			System.out.println("Liste complete des medical records :");
			medicalRecords.forEach(medicalRecord -> System.out.println(medicalRecord.getFirstName() + " " + medicalRecord.getLastName()));
		});
	}
	
	@Test
	void testAddMedicalRecord() throws Exception {
		MedicalRecords medicalRecord = new MedicalRecords();
		medicalRecord.setFirstName("pat");
		medicalRecord.setLastName("patrouille");
		medicalRecord.setBirthdate("01/01/1980");
		medicalRecord.setMedications(Arrays.asList("doliprane", "aspirine"));
		medicalRecord.setAllergies(Arrays.asList("pollen", "chat"));
		mockMvc.perform(post("/medicalRecord") 
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(medicalRecord))) 
		.andExpect(status().isCreated()) 
		.andExpect(jsonPath("$.firstName").value("pat"))
		.andExpect(jsonPath("$.lastName").value("patrouille")) 
		.andExpect(jsonPath("$.birthdate").value("01/01/1980")) 
		.andExpect(jsonPath("$.medications[0]").value("doliprane"))
		.andExpect(jsonPath("$.medications[1]").value("aspirine"))
		.andExpect(jsonPath("$.allergies[0]").value("pollen")) 
		.andExpect(jsonPath("$.allergies[1]").value("chat"));
	}
	
	@Test
	void testUpdateMedicalRecord_whenMedicalRecordDoeasExist() throws Exception {
		MedicalRecords medicalRecord = new MedicalRecords();
		medicalRecord.setFirstName("pat");
		medicalRecord.setLastName("patrouille");
		medicalRecord.setBirthdate("02/03/1989");
		medicalRecord.setMedications(Arrays.asList("doli", "paracetamol"));
		medicalRecord.setAllergies(Arrays.asList("pol", "chien"));
		mockMvc.perform(put("/medicalRecord") 
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(medicalRecord))) 
		.andExpect(status().isOk()) 
		.andExpect(jsonPath("$.birthdate").value("02/03/1989")) 
		.andExpect(jsonPath("$.medications[0]").value("doli"))
		.andExpect(jsonPath("$.medications[1]").value("paracetamol"))
		.andExpect(jsonPath("$.allergies[0]").value("pol")) 
		.andExpect(jsonPath("$.allergies[1]").value("chien"));
	}
	
	@Test
	void testUpdateMedicalRecord_whenMedicalRecordDoesNotExist() throws Exception {
	    MedicalRecords medicalRecord = new MedicalRecords();
	    medicalRecord.setFirstName("notfound");
	    medicalRecord.setLastName("person");

	    mockMvc.perform(put("/medicalRecord")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(objectMapper.writeValueAsString(medicalRecord)))
	           .andExpect(status().isNotFound());
	}
	
	@Test
	void testDeletePerson_whenMedicalRecordDoesExist() throws Exception {
		mockMvc.perform(delete("/medicalRecord")
				.param("firstName", "pat")
				.param("lastName", "patrouille"))
		.andExpect(status().isNoContent());
	}
	
	@Test
	void testDeleteMedicalRecord_whenMedicalRecordDoesNotExist() throws Exception {
	    mockMvc.perform(delete("/medicalRecord")
	            .param("firstName", "notfound")
	            .param("lastName", "person"))
	           .andExpect(status().isNotFound());
	}
}
