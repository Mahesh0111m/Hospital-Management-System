package com.mahesh.HMS.controller;

import com.mahesh.HMS.dto.AppointmentDTO;
import com.mahesh.HMS.model.Appointment;
import com.mahesh.HMS.model.Doctor;
import com.mahesh.HMS.model.Patient;
import com.mahesh.HMS.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointment")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping("/{patientId}/{doctorId}")
    public AppointmentDTO addAppointment(@PathVariable Long patientId , @PathVariable Long doctorId , @RequestBody AppointmentDTO appointmentDTO){
        return appointmentService.addAppointment(patientId , doctorId ,appointmentDTO);
    }

    @GetMapping
    public Page<AppointmentDTO> getAllAppointments(@RequestParam(defaultValue = "0") int page ,
                                                @RequestParam(defaultValue = "10") int size){
        return appointmentService.getAllAppointments(page , size);
    }

    @GetMapping("/{id}")
    public AppointmentDTO getAppointmentById(@PathVariable Long id){
        return appointmentService.getAppointmentById(id);
    }

    @PutMapping("/{id}")
    public AppointmentDTO updateAppointmentbyID(@PathVariable Long id , @RequestBody AppointmentDTO appointmentDTO){
        return appointmentService.updateAppointmentbyID(id , appointmentDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteAppointmentbyId(@PathVariable Long id){
         appointmentService.deleteAppointmentbyId(id);
    }

    @GetMapping("/patient/{patientId}")
    public List<AppointmentDTO> getAppointmentsByPatientId(@PathVariable Long patientId){
       return appointmentService.getAppointmentsByPatientId(patientId);
    }
}