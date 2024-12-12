package com.project.SafetyNet.integrationTest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper; 

import com.project.SafetyNet.model.Person; 

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc; 
 
import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest 
@AutoConfigureMockMvc
public class PersonControllerIT {

	@Autowired
	private MockMvc mockMvc;
	

	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	
	
	@Test 
	void testGetAllPersons() throws Exception { 
		mockMvc.perform(get("/person")) 
		.andExpect(status().isOk())
		.andExpect(jsonPath("$[0].firstName").value("John"))
		.andExpect(jsonPath("$[1].firstName").value("Jacob"))
		//This part is for taking the list of Person and writing/ordering them on the terminal
		.andDo(result -> { 
			System.out.println(result.getResponse().getContentAsString()); 
			List<Person> persons = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<Person>>() {}); 
			System.out.println("Liste complète des personnes :"); 
			persons.forEach(person -> System.out.println(person.getFirstName() + " " + person.getLastName()));
		});
	}
	
	@Test
	void testAddPerson() throws Exception {
		Person person = new Person(); 
		person.setFirstName("Ber");
		person.setLastName("Nadette"); 
		person.setAddress("123 Main St");
		person.setCity("Pasici");
		person.setZip("12345");
		person.setPhone("123-456-7890");
		person.setEmail("ber.nadette@email.com");
		mockMvc.perform(post("/person") 
				.contentType(MediaType.APPLICATION_JSON) 
				.content(objectMapper.writeValueAsString(person)))
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$.firstName").value("Ber")) 
		.andExpect(jsonPath("$.lastName").value("Nadette"));
	}
	
	@Test
	void testUpdatePerson() throws Exception {
		Person person = new Person();
		person.setFirstName("Ber");
		person.setLastName("Nadette"); 
		person.setAddress("456 Oak St"); 
		person.setCity("New City");
		person.setZip("98765"); 
		person.setPhone("123-456-7891");
		person.setEmail("ber.nadette@newemail.com");
		mockMvc.perform(put("/person") 
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(person)))
		.andExpect(status().isOk()) 
		.andExpect(jsonPath("$.address").value("456 Oak St")) 
		.andExpect(jsonPath("$.city").value("New City"));
	}
	
	@Test
	void testDeletePerson() throws Exception {
		mockMvc.perform(delete("/person") 
				.param("firstName", "Ber")
				.param("lastName", "Nadette"))
		.andExpect(status().isNoContent());
	}
	
	@Test
	void testGetEmailsByCity() throws Exception {
		mockMvc.perform(get("/communityEmail?city=pasici"))
		.andExpect(jsonPath("$.[0]").value("ber.nadette@email.com"));
	}
	
	@Test
	void testGetChildAlertByAddress() throws Exception {
		mockMvc.perform(get("/childAlert?address=892 Downing Ct"))
			.andExpect(jsonPath("$.[0].firstName").value("Zach"));
	}
	
	@Test
	void testGetAllPersonsInfoByLastName() throws Exception {
		mockMvc.perform(get("/personInfo?lastName=cooper"))
			.andExpect(jsonPath("$.[0].lastName").value("Cooper"));
	}
}
