package com.mahesh.HMS.service;

import com.mahesh.HMS.model.Doctor;
import com.mahesh.HMS.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import java.util.*;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;
    public Doctor addDoctor(Doctor doctor){
        try{
            return doctorRepository.save(doctor);
        }
        catch (Exception e){
            System.out.println("Exception"+e.getMessage());
            return null;
        }
    }

    public Page<Doctor> getAllDoctors(int page , int size){
        try{
            Pageable pageable = PageRequest.of(page , size);
            return doctorRepository.findAll(pageable);
        }
        catch (Exception e){
            System.out.println("Exception"+e.getMessage());
            return null;
        }
    }

    public Doctor getDoctorById(Long id){
        try{
            return doctorRepository.findById(id).orElse(null);
        }
        catch (Exception e){
            System.out.println("Exception"+e.getMessage());
            return null;
        }
    }

    public Doctor updateDoctorbyID(Long id , Doctor updatedDoctor){
        try{
            Optional<Doctor> existingDoctor = doctorRepository.findById(id);
            if(existingDoctor.isPresent()){
                Doctor d = existingDoctor.get();
                d.setName(updatedDoctor.getName());
                d.setSpeciality(updatedDoctor.getSpeciality());

                return updatedDoctor;
            }
            else{
                System.out.println("No doctor found");
                return null;
            }
        }
        catch (Exception e){
            System.out.println("Exception"+e.getMessage());
            return null;
        }
    }

    public void deleteDoctorbyId(Long id){
        try{
            doctorRepository.deleteById(id);
        }
        catch (Exception e){
            System.out.println("Exception"+e.getMessage());

        }
    }
}
