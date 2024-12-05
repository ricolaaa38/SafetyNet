package com.project.SafetyNet.tool;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class CalculateAge {

	public static int calculateAge(String birthdate) {
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
	    LocalDate birthDate = LocalDate.parse(birthdate, formatter);
	    return Period.between(birthDate, LocalDate.now()).getYears();
	}
	
	public static boolean isOverEighteen(String birthdate) {
		int age = calculateAge(birthdate);
		return age >= 18;
	}
}