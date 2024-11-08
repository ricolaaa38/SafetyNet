package com.project.SafetyNet.repository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.SafetyNet.model.AllData;
import com.project.SafetyNet.model.Person;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public class JsonFileConnect {
	
	private static final String JSON_FILE_PATH = "src/main/resources/data.json";
	private final ObjectMapper objectMapper = new ObjectMapper();
	
	public AllData readJson() throws IOException {
		return objectMapper.readValue(new File(JSON_FILE_PATH), AllData.class);
	}
	
	public void writeJson(AllData allData) throws IOException {
		objectMapper.writeValue(new File(JSON_FILE_PATH), allData);
	}
	
	public List<Person> getAllPersons() throws IOException {
		return readJson().getPersons();
	}
	
	public void saveAllPersons(List<Person> persons) throws IOException {
		AllData allData = readJson();
		allData.setPersons(persons);
		writeJson(allData);
	}

}
