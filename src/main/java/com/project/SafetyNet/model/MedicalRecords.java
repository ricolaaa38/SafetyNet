package com.project.SafetyNet.model;

import java.util.List;
import lombok.Data;

@Data
public class MedicalRecords {
	 	private String firstName;
	    private String lastName;
	    private String birthdate;
	    private List<String> medications;
	    private List<String> allergies;
}
