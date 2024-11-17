package com.project.SafetyNet.service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.SafetyNet.model.Firestations;
import com.project.SafetyNet.repository.JsonFileConnect;

@Service
public class FirestationsService {

	private final JsonFileConnect jsonFileConnect;
	
	@Autowired
	public FirestationsService(JsonFileConnect jsonFileConnect) {
		this.jsonFileConnect = jsonFileConnect;
	}
	
	public List<Firestations> getAllFirestations() {
		try {
			return jsonFileConnect.getAllFirestations();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void saveFirestationMapping(List<Firestations> firestations) {
		try {
			jsonFileConnect.saveAllFirestations(firestations);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addFirestationMapping(Firestations firestation) {
		try {
			List<Firestations> firestations = jsonFileConnect.getAllFirestations();
			firestations.add(firestation);
			saveFirestationMapping(firestations);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Firestations updateFirestationNumber(Firestations firestation) {
		List<Firestations> firestations = extractedFirestation();
		Optional<Firestations> optionalFirestation = firestations.stream().filter(p -> p.getAddress().equals(firestation.getAddress())).findFirst();
		if (optionalFirestation.isPresent()) {
			Firestations p = rewriteFirestation(firestation, optionalFirestation);
			saveFirestationMapping(firestations);
			return p;	
		}
		return null;
	}

	private Firestations rewriteFirestation(Firestations firestation, Optional<Firestations> optionalFirestation) {
		Firestations p = optionalFirestation.get();
		p.setStation(firestation.getStation());
		return p;
	}

	private List<Firestations> extractedFirestation() {
		try {
			return jsonFileConnect.getAllFirestations();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
	}
	
	public boolean deleteFirestationMapping(String address) {
		try {
			List<Firestations> firestations = jsonFileConnect.getAllFirestations();
			for (int i = 0; i < firestations.size(); i++) {
				Firestations p = firestations.get(i);
				if (p.getAddress().equals(address)) {
					firestations.remove(i);
					saveFirestationMapping(firestations);
					return true;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}
