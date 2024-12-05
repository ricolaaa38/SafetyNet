package com.project.SafetyNet.dto;

import java.util.List;

public class ChildAlertDTO {

	private String firstName;
	private String lastName;
	private int age;
	private List<String> houseMembers;
	
	public ChildAlertDTO(String firstName, String lastName, int age, List<String> houseMembers) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.houseMembers = houseMembers;
    }
	
	public String getFirstName() {
	        return firstName;
	   }


	public String getLastName() {
	        return lastName;
	    }    
   

	public int getAge() {
	        return age;
	    }   
 

	public List<String> getHouseMembers() {
	        return houseMembers;
	    }    
  
	
}
