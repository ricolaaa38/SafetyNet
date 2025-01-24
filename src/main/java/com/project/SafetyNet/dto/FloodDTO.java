package com.project.SafetyNet.dto;

import java.util.List;

/**
 * Data Transfer Object (DTO) representing the flood-related information
 * for a specific address, including the list of residents details
 */
public class FloodDTO {

    private String address;
    private List<FireDTO> residents;

    /**
     * Constructs a new FloodDTO with the provided address and resident details.
     *
     * @param address  The address that is affected by the flood.
     * @param residents A list of FireDTO objects representing residents at the given address
     */
    public FloodDTO(String address, List<FireDTO> residents) {
        this.address = address;
        this.residents = residents;
    }

    /**
     * Gets the address affected by the flood.
     * 
     * @return The address of the flooded location.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the address of the flooded location.
     * 
     * @param address The new address to set.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Gets the list of residents at the flooded address.
     * 
     * @return A list of FireDTO objects representing the residents.
     */
    public List<FireDTO> getResidents() {
        return residents;
    }

    /**
     * Sets the list of residents at the flooded address.
     * 
     * @param residents The new list of residents to set.
     */
    public void setResidents(List<FireDTO> residents) {
        this.residents = residents;
    }
}
