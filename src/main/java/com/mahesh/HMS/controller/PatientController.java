package com.mahesh.HMS.controller;

import com.mahesh.HMS.model.Patient;
import com.mahesh.HMS.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patient")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @PostMapping
    public Patient addPatient(@RequestBody Patient patient){
        return patientService.addPatient(patient);
    }

    @GetMapping
    public Page<Patient> getAllPatients(@RequestParam(defaultValue = "0") int page ,
                                        @RequestParam(defaultValue = "10") int size){
        return patientService.getAllPatients(page , size);
    }

    @GetMapping("/{id}")
    public Patient getPatientById(@PathVariable Long id){
        return patientService.getPatientById(id);
    }

    @PutMapping("/{id}")
    public Patient updatePatientbyID(@PathVariable Long id , @RequestBody Patient patient){
        return patientService.updatePatientbyID(id , patient);
    }

    @DeleteMapping("/{id}")
    public void deletePatientbyId(@PathVariable Long id){

    }
}