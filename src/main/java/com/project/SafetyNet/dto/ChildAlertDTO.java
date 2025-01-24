package com.project.SafetyNet.dto;

import java.util.List;

/**
 * Data Transfer Object (DTO) that represents a child alert with the child's information and associated house members.
 */
public class ChildAlertDTO {

    private String firstName;
    private String lastName;
    private int age;
    private List<String> houseMembers;

    /**
     * Constructs a new ChildAlertDTO with the provided information.
     * 
     * @param firstName    The first name of the child.
     * @param lastName     The last name of the child.
     * @param age          The age of the child.
     * @param houseMembers List of names of the house members associated with the child.
     */
    public ChildAlertDTO(String firstName, String lastName, int age, List<String> houseMembers) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.houseMembers = houseMembers;
    }

    /**
     * Gets the first name of the child.
     * 
     * @return The first name of the child.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Gets the last name of the child.
     * 
     * @return The last name of the child.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Gets the age of the child.
     * 
     * @return The age of the child.
     */
    public int getAge() {
        return age;
    }

    /**
     * Gets the list of house members associated with the child.
     * 
     * @return List of names of the house members.
     */
    public List<String> getHouseMembers() {
        return houseMembers;
    }
}

