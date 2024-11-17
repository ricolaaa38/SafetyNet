package com.project.SafetyNet.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.SafetyNet.model.AllData;
import com.project.SafetyNet.model.Firestations;
import com.project.SafetyNet.model.MedicalRecords;
import com.project.SafetyNet.model.Person;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;

@Repository
@PropertySource("classpath:application.properties")
public class JsonFileConnect {
	
	@Value("${json.data.path}")
	private String jsonFilePath;
	private final ObjectMapper objectMapper = new ObjectMapper();
	
	private AllData readJson() throws IOException {
		return objectMapper.readValue(new File(jsonFilePath), AllData.class);
	}
	
	private void writeJson(AllData allData) throws IOException {
		objectMapper.writeValue(new File(jsonFilePath), allData);
	}
	
	public List<Person> getAllPersons() throws IOException {
		return readJson().getPersons();
	}
	
	public void saveAllPersons(List<Person> persons) throws IOException {
		AllData allData = readJson();
		allData.setPersons(persons);
		writeJson(allData);
	}
	
	public List<MedicalRecords> getAllMedicalRecords() throws IOException {
		return readJson().getMedicalRecords();
	}
	
	public void saveAllMedicalRecords(List<MedicalRecords> medicalrecords) throws IOException {
		AllData allData = readJson();
		allData.setMedicalRecords(medicalrecords);
		writeJson(allData);
	}
	
	public List<Firestations> getAllFirestations() throws IOException {
		return readJson().getFirestations();
	}
	
	public void saveAllFirestations(List<Firestations> firestations) throws IOException {
		AllData allData = readJson();
		allData.setFirestations(firestations);
		writeJson(allData);
	}

}
