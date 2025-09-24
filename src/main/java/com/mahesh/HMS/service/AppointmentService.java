package com.mahesh.HMS.service;

import com.mahesh.HMS.dto.AppointmentDTO;
import com.mahesh.HMS.mapper.AppointmentMapper;
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

    @Autowired
    private AppointmentMapper appointmentMapper;

    public AppointmentDTO addAppointment(Long patientId , Long doctorId , AppointmentDTO appointmentDTO){
        try{
            Patient patient = patientRepository.findById(patientId).orElseThrow(()->new RuntimeException("No Patient found"));
            Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(()-> new RuntimeException("No Doctor found"));

            Appointment appointment = appointmentMapper.toEntity(appointmentDTO , patient , doctor);
            Appointment savedAppointment = appointmentRepository.save(appointment);
             return appointmentMapper.toDTO(savedAppointment);
        }
        catch (Exception e){
            System.out.println("Exception "+ e.getMessage());
            return null;
        }
    }


    public Page<AppointmentDTO> getAllAppointments(int page , int size){
        try {
            Pageable pageable = PageRequest.of(page , size);
            Page<Appointment> appointments = appointmentRepository.findAll(pageable);
            return appointments.map(appointmentMapper::toDTO);
        }
        catch (Exception e){
            System.out.println("Exception "+ e.getMessage());
            return null;
        }
    }

    public AppointmentDTO getAppointmentById(Long id){
        try {
            Appointment appointment = appointmentRepository.findById(id).orElse(null);
            return appointmentMapper.toDTO(appointment);
        }
        catch (Exception e){
            System.out.println("Exception "+ e.getMessage());
            return null;
        }
    }

    public AppointmentDTO updateAppointmentbyID(Long id , AppointmentDTO appointmentDTO){
        try {
            Optional<Appointment> existingAppointment = appointmentRepository.findById(id);
            if(existingAppointment.isPresent()){
                Appointment a = existingAppointment.get();

                Patient p = patientRepository.findById(appointmentDTO.getPatientId())
                                            .orElseThrow(()->new RuntimeException("No Patientfound"));

                Doctor d = doctorRepository.findById(appointmentDTO.getDoctorId())
                                .orElseThrow(()-> new RuntimeException("Doctor not found"));

                a.setPatient(p);
                a.setDoctor(d);
                a.setDate(appointmentDTO.getDate());

                Appointment  savedAppointment = appointmentRepository.save(a);
                return appointmentMapper.toDTO(savedAppointment);
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

    public List<AppointmentDTO> getAppointmentsByPatientId(Long patientId){
        List<Appointment> savedAppointment = appointmentRepository.findByPatientId(patientId);
        return appointmentMapper.toDTOList(savedAppointment , false);
    }

    public List<AppointmentDTO> getAppointmentsByDoctorId(Long doctorId) {
        List<Appointment> appointments = appointmentRepository.findAllAppointmentsByDoctorId(doctorId);
        return appointmentMapper.toDTOList(appointments, false); // exclude bill
    }

    public List<AppointmentDTO> getUpcomingAppointmentsForPatient(Long patientId) {
        List<Appointment> appointments = appointmentRepository.findUpcomingAppointmentsByPatientId(patientId);
        return appointmentMapper.toDTOList(appointments, false); // exclude bill info
    }

}