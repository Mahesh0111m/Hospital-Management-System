package com.mahesh.HMS.controller;

import com.mahesh.HMS.model.Doctor;
import com.mahesh.HMS.model.Patient;
import com.mahesh.HMS.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import java.util.*;

import javax.print.Doc;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @PostMapping
    public Doctor addDoctor(@RequestBody Doctor doctor){
        return doctorService.addDoctor(doctor);
    }

    @GetMapping
    public Page<Doctor> getAllDoctors(@RequestParam(defaultValue = "0") int page ,
                                      @RequestParam(defaultValue = "10") int size){
        return doctorService.getAllDoctors(page , size);
    }

    @GetMapping("/{id}")
    public Doctor getDoctorById(@PathVariable Long id){
        return doctorService.getDoctorById(id);
    }

    @PutMapping("/{id}")
    public Doctor updateDoctorbyID(@PathVariable Long id , @RequestBody Doctor doctor){
        return doctorService.updateDoctorbyID(id , doctor);
    }

    @DeleteMapping("/{id}")
    public void deleteDoctorbyId(@PathVariable Long id){
         doctorService.deleteDoctorbyId(id);
    }
}