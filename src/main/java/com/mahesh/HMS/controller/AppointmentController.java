package com.mahesh.HMS.controller;

import com.mahesh.HMS.dto.AppointmentDTO;
import com.mahesh.HMS.dto.PaginatedResponsesDTO;
import com.mahesh.HMS.model.Appointment;
import com.mahesh.HMS.model.Doctor;
import com.mahesh.HMS.model.Patient;
import com.mahesh.HMS.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    public PaginatedResponsesDTO<AppointmentDTO> getAllAppointments(@RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "10") int size) {
        Page<AppointmentDTO> appointmentPage = appointmentService.getAllAppointments(page, size);
        return new PaginatedResponsesDTO<>(
                appointmentPage.getContent(),
                appointmentPage.getNumber(),
                appointmentPage.getSize(),
                appointmentPage.getTotalElements(),
                appointmentPage.getTotalPages()
        );
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

    @GetMapping("/doctor/{userId}")
    public List<AppointmentDTO> getAppointmentsByDoctorId(@PathVariable Long userId){
        return appointmentService.getAppointmentsByDoctorId(userId); // bill excluded
    }

    @PostMapping("/book/{patientId}/{doctorId}/{availabilityId}")
    public ResponseEntity<?> bookAppointment(
            @PathVariable Long patientId,
            @PathVariable Long doctorId, // kept for URL consistency
            @PathVariable Long availabilityId) {
    System.out.println(patientId + "" + doctorId + "" + availabilityId);
        try {
            // 🔹 doctorId is not needed by service, but we keep it for consistency with frontend URLs
            AppointmentDTO dto = appointmentService.bookAppointment(availabilityId, patientId);
            return ResponseEntity.ok(dto);

        } catch (RuntimeException e) {
            // return a friendly message if conflict or invalid
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // AppointmentController.java
    @PutMapping("/{appointmentId}/reschedule/{availabilityId}")
    public AppointmentDTO rescheduleAppointment(
            @PathVariable Long appointmentId,
            @PathVariable Long availabilityId) {
        return appointmentService.rescheduleAppointment(appointmentId, availabilityId);
    }



}