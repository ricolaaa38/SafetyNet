package com.project.SafetyNet.dto;

import java.util.List;

public class FloodDTO {

	 	private String address;
	    private List<FireDTO> residents;

	    public FloodDTO(String address, List<FireDTO> residents) {
	        this.address = address;
	        this.residents = residents;
	    }

	    public String getAddress() {
	        return address;
	    }

	    public void setAddress(String address) {
	        this.address = address;
	    }

	    public List<FireDTO> getResidents() {
	        return residents;
	    }

	    public void setResidents(List<FireDTO> residents) {
	        this.residents = residents;
	    }
}
