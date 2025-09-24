package com.mahesh.HMS.controller;

import com.mahesh.HMS.dto.*;
import com.mahesh.HMS.model.Patient;
import com.mahesh.HMS.service.AppointmentService;
import com.mahesh.HMS.service.BillService;
import com.mahesh.HMS.service.DoctorService;
import com.mahesh.HMS.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patient")
public class PatientController {

    @Autowired
    private BillService billService;


    @Autowired
    private PatientService patientService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping
    public PatientDTO addPatient(@RequestBody Patient patient) {
        return patientService.addPatient(patient);
    }

    @GetMapping
    public PaginatedResponsesDTO<PatientDTO> getAllPatients(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size) {
        Page<PatientDTO> patientPage = patientService.getAllPatients(page, size);
        return new PaginatedResponsesDTO<>(
                patientPage.getContent(),
                patientPage.getNumber(),
                patientPage.getSize(),
                patientPage.getTotalElements(),
                patientPage.getTotalPages()
        );
    }


    @GetMapping("/{id}")
    public PatientDTO getPatientById(@PathVariable Long id) {
        return patientService.getPatientById(id);
    }

    @PutMapping("/{id}")
    public PatientDTO updatePatientbyID(@PathVariable Long id, @RequestBody Patient patient) {
        return patientService.updatePatientbyID(id, patient);
    }

    @DeleteMapping("/{id}")
    public void deletePatientbyId(@PathVariable Long id) {
        patientService.deletePatientbyId(id);
    }


    @GetMapping("/patient/{patientId}")
    public List<BillDTO> getBillsByPatientId(@PathVariable Long patientId){
        return billService.getBillsByPatientId(patientId);
    }

    @GetMapping("/{patientId}/doctors")
    public List<DoctorDTO> getDoctorsConsulted(@PathVariable Long patientId) {
        return doctorService.getDoctorsConsultedByPatient(patientId);
    }

    @GetMapping("/{patientId}/appointments/upcoming")
    public List<AppointmentDTO> getUpcomingAppointments(@PathVariable Long patientId) {
        return appointmentService.getUpcomingAppointmentsForPatient(patientId);
    }


}
