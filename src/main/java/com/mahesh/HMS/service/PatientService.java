package com.mahesh.HMS.service;

import com.mahesh.HMS.dto.PatientDTO;
import com.mahesh.HMS.mapper.PatientMapper;
import com.mahesh.HMS.model.Patient;
import com.mahesh.HMS.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PatientMapper patientMapper;

    public PatientDTO addPatient(Patient patient) {
        try {
            Patient saved = patientRepository.save(patient);
            return patientMapper.toDTO(saved);
        } catch (Exception e) {
            System.out.println("Exception " + e.getMessage());
            return null;
        }
    }

    public Page<PatientDTO> getAllPatients(int page, int size) {
        try {
            return patientRepository.findAll(PageRequest.of(page, size))
                    .map(patientMapper::toDTO);
        } catch (Exception e) {
            System.out.println("Exception " + e.getMessage());
            return Page.empty();
        }
    }

    public PatientDTO getPatientById(Long id) {
        try {
            Optional<Patient> patient = patientRepository.findById(id);
            return patient.map(patientMapper::toDTO).orElse(null);
        } catch (Exception e) {
            System.out.println("Exception " + e.getMessage());
            return null;
        }
    }

    public PatientDTO updatePatientbyID(Long id, Patient updatedPatient) {
        try {
            Optional<Patient> existingPatient = patientRepository.findById(id);
            if (existingPatient.isPresent()) {
                Patient p = existingPatient.get();
                p.setName(updatedPatient.getName());
                p.setAge(updatedPatient.getAge());
                p.setGender(updatedPatient.getGender());

                Patient savedPatient = patientRepository.save(p);
                return patientMapper.toDTO(savedPatient);
            } else {
                System.out.println("Patient not found");
                return null;
            }
        } catch (Exception e) {
            System.out.println("Exception " + e.getMessage());
            return null;
        }
    }

    public void deletePatientbyId(Long id) {
        try {
            patientRepository.deleteById(id);
        } catch (Exception e) {
            System.out.println("Exception " + e.getMessage());
        }
    }

    public List<PatientDTO> getPatientsByDoctorId(Long doctorId) {
        List<Patient> patients = patientRepository.findAllPatientsByDoctorId(doctorId);
        return patients.stream()
                .map(patientMapper::toDTO)
                .toList();
    }

}
