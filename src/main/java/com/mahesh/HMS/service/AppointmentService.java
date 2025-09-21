package com.mahesh.HMS.service;

import com.mahesh.HMS.model.Appointment;
import com.mahesh.HMS.model.Doctor;
import com.mahesh.HMS.model.Patient;
import com.mahesh.HMS.repository.AppointmentRepository;
import com.mahesh.HMS.repository.DoctorRepository;
import com.mahesh.HMS.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import java.util.*;

@Service
public class AppointmentService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;
    public Appointment addAppointment(Long patientId , Long doctorId , Appointment appointment){
        try{
            Patient patient = patientRepository.findById(patientId).orElseThrow(()->new RuntimeException("No Patient found"));
            Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(()-> new RuntimeException("No Doctor found"));

            appointment.setPatient(patient);
            appointment.setDoctor(doctor);
            return appointmentRepository.save(appointment);
        }
        catch (Exception e){
            System.out.println("Exception "+ e.getMessage());
            return null;
        }
    }


    public Page<Appointment> getAllAppointments(int page , int size){
        try {
            Pageable pageable = PageRequest.of(page , size);
            return appointmentRepository.findAll(pageable);
        }
        catch (Exception e){
            System.out.println("Exception "+ e.getMessage());
            return null;
        }
    }

    public Appointment getAppointmentById(Long id){
        try {
            return appointmentRepository.findById(id).orElse(null);
        }
        catch (Exception e){
            System.out.println("Exception "+ e.getMessage());
            return null;
        }
    }

    public Appointment updateAppointmentbyID(Long id , Appointment updatedAppointment){
        try {
            Optional<Appointment> existingAppointment = appointmentRepository.findById(id);
            if(existingAppointment.isPresent()){
                Appointment a = existingAppointment.get();

                Patient p = patientRepository.findById(updatedAppointment.getPatient().getId())
                                            .orElseThrow(()->new RuntimeException("No Patientfound"));

                Doctor d = doctorRepository.findById(updatedAppointment.getDoctor().getId())
                                .orElseThrow(()-> new RuntimeException("Doctor not found"));

                a.setPatient(p);
                a.setDoctor(d);
                a.setDate(updatedAppointment.getDate());

                return appointmentRepository.save(a);
            }
            else {
                System.out.println("No appointment found");
                return null;
            }
        }
        catch (Exception e){
            System.out.println("Exception "+ e.getMessage());
            return null;
        }
    }

    public void deleteAppointmentbyId(Long id){
        try {
            appointmentRepository.deleteById(id);
            System.out.println("deleted appointment");
        }
        catch (Exception e){
            System.out.println("Exception "+ e.getMessage());

        }
    }

    public List<Appointment> getAppointmentsByPatientId(Long patientId){
        return appointmentRepository.findByPatientId(patientId);
    }
}