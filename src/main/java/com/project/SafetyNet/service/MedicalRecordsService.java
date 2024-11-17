package com.project.SafetyNet.service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.SafetyNet.model.MedicalRecords;
import com.project.SafetyNet.repository.JsonFileConnect;

@Service
public class MedicalRecordsService {
		
	private final JsonFileConnect jsonFileConnect;
	
	@Autowired
	public MedicalRecordsService(JsonFileConnect jsonFileConnect) {
		this.jsonFileConnect = jsonFileConnect;
	}
	
	public List<MedicalRecords> getAllMedicalRecords() {
		try {
			return jsonFileConnect.getAllMedicalRecords();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void addMedicalRecord(MedicalRecords medicalRecord) {
		try {
			List<MedicalRecords> medicalRecords = jsonFileConnect.getAllMedicalRecords();
			medicalRecords.add(medicalRecord);
			saveMedicalRecord(medicalRecords);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public MedicalRecords updateMedicalRecord(MedicalRecords medicalRecord) {
		List<MedicalRecords> medicalRecords = extractedMedicalRecord();
			Optional<MedicalRecords> optionalMedicalRecord = medicalRecords.stream().filter(p -> p.getFirstName().equals(medicalRecord.getFirstName()) && p.getLastName().equals(medicalRecord.getLastName())).findFirst();
			if (optionalMedicalRecord.isPresent()) {
				MedicalRecords p = rewriteMedicalRecord(medicalRecord, optionalMedicalRecord);
				saveMedicalRecord(medicalRecords);
				return p;
			};
			return null;
	}

	private List<MedicalRecords> extractedMedicalRecord() {
		try {
			 return jsonFileConnect.getAllMedicalRecords();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
	}

	private MedicalRecords rewriteMedicalRecord(MedicalRecords medicalRecord, Optional<MedicalRecords> optionalMedicalRecord) {
		MedicalRecords p = optionalMedicalRecord.get();
		p.setBirthdate(medicalRecord.getBirthdate());
		p.setMedications(medicalRecord.getMedications());
		p.setAllergies(medicalRecord.getAllergies());
		return p;
	}

	private void saveMedicalRecord(List<MedicalRecords> medicalRecords) {
		try {
			jsonFileConnect.saveAllMedicalRecords(medicalRecords);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean deleteMedicalRecord(String firstName, String lastName) {
		try {
			List<MedicalRecords> medicalRecords = jsonFileConnect.getAllMedicalRecords();
			for (int i = 0; i < medicalRecords.size(); i++) {
				MedicalRecords p = medicalRecords.get(i);
				if (p.getFirstName().equals(firstName) && p.getLastName().equals(lastName)) {
					medicalRecords.remove(i);
					saveMedicalRecord(medicalRecords);
					return true;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}
