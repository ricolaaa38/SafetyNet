package com.project.SafetyNet.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AllData {
	@JsonProperty("persons") private List<Person> persons;
	@JsonProperty("firestations") private List<Firestations> firestations;
	@JsonProperty("medicalrecords") private List<MedicalRecords> medicalrecords;
	
	public List<Person> getPersons() {
		return persons;
	}

	public void setPersons(List<Person> persons) {
		this.persons = persons;
	}
	
	public List<MedicalRecords> getMedicalRecords() {
		return medicalrecords;
	}
	
	public void setMedicalRecords(List<MedicalRecords> medicalRecords) {
		this.medicalrecords = medicalRecords;
	}
	
	public List<Firestations> getFirestations() {
		return firestations;
	}
	
	public void setFirestations(List<Firestations> firestations) {
		this.firestations = firestations;
	}
	
}
