package com.mahesh.HMS.service;

import com.mahesh.HMS.model.Patient;
import com.mahesh.HMS.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.Optional;

@Service
public class PatientService {

    @Autowired
    PatientRepository patientRepository;

    public Patient addPatient(Patient patient){
        try{
           return patientRepository.save(patient);
        }
        catch (Exception e){
            System.out.println("exception" + e.getMessage());
            return null;
        }
    }

    public Page<Patient> getAllPatients(int page , int size){
        try {
//            return patientRepository.findAll();
            Pageable pageable = PageRequest.of(page , size);
            return patientRepository.findAll(pageable);
        }
        catch (Exception e){
            System.out.println("Exception " + e.getMessage());
            return  null;
        }
    }


    public Patient getPatientById(Long id){
        try{
            Optional<Patient> patient = patientRepository.findById(id);
            return patient.orElse(null);
        }
        catch (Exception e){
            System.out.println("Exception" + e.getMessage());
            return null;
        }
    }

    public Patient updatePatientbyID(Long id , Patient updatedPatient){
        try{
            Optional<Patient> existingpatient = patientRepository.findById(id);
            if(existingpatient.isPresent()){
                Patient p = existingpatient.get();
                p.setName(updatedPatient.getName());
                p.setAge(updatedPatient.getAge());
                p.setGender(updatedPatient.getGender());

                Patient savedpatient = patientRepository.save(p);

                return savedpatient;
            }
            else {
                System.out.println("Patient not found");
                return null;
            }
        }
        catch (Exception e){
            System.out.println("Exception"+ e.getMessage());
            return null;
        }
    }

    public void deletePatientbyId(Long id){
        try{
            patientRepository.deleteById(id);
        }
        catch (Exception e){
            System.out.println("Exception " + e.getMessage());

        }
    }
}
