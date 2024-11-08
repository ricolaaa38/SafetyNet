package com.project.SafetyNet.model; 
import lombok.Data;

@Data 
public class Person { 
	private String firstName; 
	private String lastName; 
	private String address; 
	private String city; 
	private String zip;
	private String phone;
	private String email;
	
	
	public Object getFirstName() {
		return firstName;
	}


	public Object getLastName() {
		return lastName;
	}
	
	public String getAddress() { 
		return address; 
		} 
	
	public String getCity() { 
		return city; 
		} 
	
	public String getZip() { 
		return zip;
		} 
	
	public String getPhone() { 
		return phone; 
		} 
	
	public String getEmail() {
		return email;
		}


	public void setFirstName(String firstName) {
		this.firstName = firstName; 
		}
	
	public void setLastName(String lastName) { 
		this.lastName = lastName;
		}
	
	public void setAddress(String address) { 
		this.address = address; 
		}
	
	public void setCity(String city) { 
		this.city = city;
		} 
	
	public void setZip(String zip) { 
		this.zip = zip; 
		}
	
	public void setPhone(String phone) { 
		this.phone = phone;
		}
	
	public void setEmail(String email) {
		this.email = email;
		}
}