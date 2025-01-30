package com.project.SafetyNet.tool;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

/**
 * This class provides utility methods to calculate a person's age from their birthdate
 * and to check if the person is an adult.
 */
public class CalculateAge {
	
	   /**
     * Private default constructor to prevent class instanciation
     */
    private CalculateAge() {}

	 /**
     * Calculates the age in years of a person based on their birthdate.
     *
     * @param birthdate The birthdate in MM/dd/yyyy format.
     * @return The person's age in years.
     */
	public static int calculateAge(String birthdate) {
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
	    LocalDate birthDate = LocalDate.parse(birthdate, formatter);
	    return Period.between(birthDate, LocalDate.now()).getYears();
	}
	
	 /**
     * Checks if a person is an adult based on their birthdate.
     *
     * @param birthdate The birthdate in MM/dd/yyyy format.
     * @return {@code true} if the person is 18 or older, otherwise {@code false}.
     */
	public static boolean isOverEighteen(String birthdate) {
		int age = calculateAge(birthdate);
		return age >= 18;
	}
}