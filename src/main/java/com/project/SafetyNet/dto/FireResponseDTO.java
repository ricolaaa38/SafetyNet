package com.project.SafetyNet.dto;

import java.util.List;

/**
 * Data Transfer Object (DTO) representing the response for fire-related information
 * for a specific station, including the station number and the house members
 */
public class FireResponseDTO {

	  	private String stationNumber;
	    private List<FireDTO> houseMembers; 

	    /**
	     * Constructs a new FireResponseDTO with the provided information.
	     *
	     * @param stationNumber The station number to which the house belongs.
	     * @param houseMembers  A list of {@link FireDTO} objects representing house members
	     */
	    public FireResponseDTO(String stationNumber, List<FireDTO> houseMembers) {
	        this.stationNumber = stationNumber;
	        this.houseMembers = houseMembers;
	    }

	    /**
	     * Gets the station number.
	     * 
	     * @return The station number.
	     */
	    public String getStationNumber() {
	        return stationNumber;
	    }

	    /**
	     * Gets the list of house members associated with the fire station.
	     * 
	     * @return A list of {@link FireDTO} objects representing the house members.
	     */
	    public List<FireDTO> getHouseMembers() {
	        return houseMembers;
	    }
}
