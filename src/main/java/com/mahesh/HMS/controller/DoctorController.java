package com.mahesh.HMS.controller;

import com.mahesh.HMS.dto.AppointmentDTO;
import com.mahesh.HMS.dto.DoctorDTO;
import com.mahesh.HMS.dto.PaginatedResponsesDTO;
import com.mahesh.HMS.dto.PatientDTO;
import com.mahesh.HMS.model.Doctor;
import com.mahesh.HMS.service.AppointmentService;
import com.mahesh.HMS.service.BillService;
import com.mahesh.HMS.service.DoctorService;
import com.mahesh.HMS.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private BillService billService;

    @PostMapping
    public DoctorDTO addDoctor(@RequestBody Doctor doctor) {
        return doctorService.addDoctor(doctor);
    }


    @GetMapping
    public PaginatedResponsesDTO<DoctorDTO> getAllDoctors(@RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "10") int size) {
        Page<DoctorDTO> doctorPage = doctorService.getAllDoctors(page, size);
        return new PaginatedResponsesDTO<>(
                doctorPage.getContent(),
                doctorPage.getNumber(),
                doctorPage.getSize(),
                doctorPage.getTotalElements(),
                doctorPage.getTotalPages()
        );
    }


    @GetMapping("/{id}")
    public DoctorDTO getDoctorById(@PathVariable Long id) {
        return doctorService.getDoctorById(id);
    }


    @PutMapping("/{id}")
    public DoctorDTO updateDoctorbyID(@PathVariable Long id, @RequestBody Doctor doctor) {
        return doctorService.updateDoctorbyID(id, doctor);
    }


    @DeleteMapping("/{id}")
    public void deleteDoctorbyId(@PathVariable Long id) {
        doctorService.deleteDoctorbyId(id);
    }


    @GetMapping("/{doctorId}/patients")
    public List<PatientDTO> getPatientsByDoctor(@PathVariable Long doctorId) {
        return patientService.getPatientsByDoctorId(doctorId);
    }


    @GetMapping("/{doctorId}/revenue")
    public Double getTotalRevenue(@PathVariable Long doctorId) {
        return billService.getTotalRevenueByDoctor(doctorId);
    }


    @DeleteMapping("/{userId}/appointment/{appointmentId}")
    public ResponseEntity<?> deleteAppointmentByDoctor(
            @PathVariable Long userId,
            @PathVariable Long appointmentId) {
        try {
            appointmentService.deleteAppointmentByDoctor(userId, appointmentId);
            return ResponseEntity.ok(Map.of("message", "Appointment deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }





}