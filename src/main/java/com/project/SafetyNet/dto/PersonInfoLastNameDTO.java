package com.project.SafetyNet.dto;

import java.util.List;

public class PersonInfoLastNameDTO {

	private String lastName;
	private String address;
	private int age;
	private String email;
	private List<String> medications;
	private List<String> allergies;
	
	public PersonInfoLastNameDTO(String lastName, String address, int age, String email, List<String> medications, List<String> allergies) {
		this.lastName = lastName;
		this.address = address;
		this.age = age;
		this.email = email;
		this.medications = medications;
		this.allergies = allergies;
	}
	
	public String getLastName() {
		return lastName;
	}

	public String getAddress() {
		return address;
	}
	
	public int getAge() {
		return age;
	}
	
	public String getEmail() {
		return email;
	}
	
	public List<String> getMedications() {
		return medications;
	}
	
	public List<String> getAllergies() {
		return allergies;
	}
}