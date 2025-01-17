package com.project.SafetyNet.integrationTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.SafetyNet.model.Firestations;

@SpringBootTest
@AutoConfigureMockMvc
public class FirestationsControllerIT {

	@Autowired
	private MockMvc mockMvc;
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	@Test
	void testGetAllFirestations() throws Exception {
		mockMvc.perform(get("/firestation"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$[0].address").value("1509 Culver St"))
		.andExpect(jsonPath("$[1].address").value("29 15th St"))
		.andDo(result -> {
			System.out.println(result.getResponse().getContentAsString());
			List<Firestations> firestations = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<Firestations>>() {});
			System.out.println("Liste complete des firestations :");
			firestations.forEach(firestation -> System.out.println(firestation.getAddress() + " " + firestation.getStation()));
		});
	}
	
	@Test
	void testAddFirestationMapping() throws Exception {
		Firestations firestation = new Firestations();
		firestation.setAddress("36 15th Street");
		firestation.setStation("12");
		mockMvc.perform(post("/firestation")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(firestation)))
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$.address").value("36 15th Street"))
		.andExpect(jsonPath("$.station").value("12"));
	}
	
	@Test
	void testUpdateFirestationMapping_whenFirestationDoesExist() throws Exception {
		Firestations firestation = new Firestations();
		firestation.setAddress("36 15th Street");
		firestation.setStation("58");
		mockMvc.perform(put("/firestation")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(firestation)))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.station").value("58"));
	}
	
	@Test
	void testUpdateFirestationMapping_whenFirestationDoesNotExist() throws Exception {
	    Firestations firestation = new Firestations();
	    firestation.setAddress("unknown address");
	    firestation.setStation("99");

	    mockMvc.perform(put("/firestation")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(objectMapper.writeValueAsString(firestation)))
	            .andExpect(status().isNotFound());
	}
	
	@Test
	void testDeleteFirestationMapping_whenAddressDoesExist() throws Exception {
		mockMvc.perform(delete("/firestation")
				.param("address", "36 15th Street"))
		.andExpect(status().isNoContent());
	}
	
	@Test
	void testDeleteFirestationMapping_whenAddressDoesNotExist() throws Exception {
		mockMvc.perform(delete("/firestation")
				.param("address", "unknown address"))
		.andExpect(status().isNotFound());
	}
	
	@Test
	void testGetPersonsByStation() throws Exception {
		mockMvc.perform(get("/firestation?stationNumber=1"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.persons[0].firstName").value("Peter"))
			.andExpect(jsonPath("$.persons[1].lastName").value("Walker"));
	}
	
	@Test
	void testgetPersonsbyStation_whenNoOneIsAssociatedToTheStationNumber() throws Exception {
		mockMvc.perform(get("/firestation?stationNumber=99"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.persons").isEmpty())
        .andExpect(jsonPath("$.adults").value(0))
        .andExpect(jsonPath("$.children").value(0));
	}
	
	@Test
	void testGetPhoneByFirestationNumber_whenFirestationExists() throws Exception {
		mockMvc.perform(get("/phoneAlert?firestation=2"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.[0]").value("841-874-6513"));
	}
	
	@Test
    void testGetPhoneByFirestationNumber_whenFirestationDoesNotExist() throws Exception {
        mockMvc.perform(get("/phoneAlert?firestation=99"))
        .andExpect(status().isNoContent());
    }
	
	@Test
	void testGetHouseMembersAndFirestationByAddress_whenAddressExists() throws Exception {
		mockMvc.perform(get("/fire?address=1509 Culver St"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.stationNumber").value("3"))
			.andExpect(jsonPath("$.houseMembers[0].lastName").value("Boyd"));
	}
	
	@Test
    void testGetHouseMembersAndFirestationByAddress_whenAddressDoesNotExist() throws Exception {
        mockMvc.perform(get("/fire?address=Unknown Address"))
        .andExpect(status().isNoContent());
    }
	
	@Test
	void testGetFloodInfoByStation_whenStationExists() throws Exception {
		mockMvc.perform(get("/flood/stations?station=1"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.[0].residents[0].lastName").value("Duncan"));
	}
	
	 @Test
	    void testGetFloodInfoByStation_whenStationDoesNotExist() throws Exception {
	        mockMvc.perform(get("/flood/stations?station=99"))
	        .andExpect(status().isNoContent());
	    }
	
}
