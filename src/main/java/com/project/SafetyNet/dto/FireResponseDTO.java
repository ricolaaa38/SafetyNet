package com.project.SafetyNet.dto;

import java.util.List;

public class FireResponseDTO {

	  	private String stationNumber;
	    private List<FireDTO> houseMembers; 

	    public FireResponseDTO(String stationNumber, List<FireDTO> houseMembers) {
	        this.stationNumber = stationNumber;
	        this.houseMembers = houseMembers;
	    }

	    public String getStationNumber() {
	        return stationNumber;
	    }

	    public List<FireDTO> getHouseMembers() {
	        return houseMembers;
	    }
}
