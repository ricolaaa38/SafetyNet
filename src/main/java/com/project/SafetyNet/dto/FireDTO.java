package com.project.SafetyNet.dto;

import java.util.List;

public class FireDTO {

	private String lastName;
	private String phone;
	private String age;
	private List<String> medications;
	private List<String> allergies;
	
	public FireDTO(String lastName, String phone, String age, List<String> medications, List<String> allergies) {
		this.lastName = lastName;
		this.phone = phone;
		this.age = age;
		this.medications = medications;
		this.allergies = allergies;
	}
	
	public String getLastName() {
		return lastName;
	}

	public String getPhone() {
		return phone;
	}
	
	public String getAge() {
		return age;
	}
	
	public List<String> getMedications() {
		return medications;
	}
	
	public List<String> getAllergies() {
		return allergies;
	}
}
