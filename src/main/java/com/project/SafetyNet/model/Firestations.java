package com.project.SafetyNet.model;

/**
 * Represents a firestation with its addresses and station number.
 */
public class Firestations {
	private String address;
    private String station;
    
    /**
     * Gets the address of the fire station.
     * 
     * @return Address of the fire station.
     */
    public String getAddress() {
    	return address;
    }
    
    /**
     * Gets the station number.
     * 
     * @return Station number.
     */
    public String getStation() {
    	return station;
    }
    
    /**
     * Sets the address of the fire station.
     * 
     * @param address Address of the fire station.
     */
    public void setAddress(String address) {
    	this.address = address;
    }
    
    /**
     * Sets the station number.
     * 
     * @param station Station number.
     */
    public void setStation(String station) {
    	this.station = station;
    }
}

