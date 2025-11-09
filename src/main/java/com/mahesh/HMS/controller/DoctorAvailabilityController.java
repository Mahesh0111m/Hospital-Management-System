package com.mahesh.HMS.controller;

import com.mahesh.HMS.dto.DoctorAvailabilityDTO;
import com.mahesh.HMS.service.DoctorAvailabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/availability")
public class DoctorAvailabilityController {

    @Autowired
    private DoctorAvailabilityService availabilityService;

    // Get all slots (doctor or admin)
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<DoctorAvailabilityDTO>> getAllSlots(
            @PathVariable Long doctorId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        if (date != null) {
            return ResponseEntity.ok(availabilityService.getSlotsByDate(doctorId, date));
        }
        return ResponseEntity.ok(availabilityService.getAllSlotsForDoctor(doctorId));
    }

    // Get only available slots for patient UI
    @GetMapping("/doctor/{doctorId}/available")
    public ResponseEntity<List<DoctorAvailabilityDTO>> getAvailableSlots(@PathVariable Long doctorId) {
        return ResponseEntity.ok(availabilityService.getAvailableSlotsForDoctor(doctorId));
    }

    // Add one or multiple slots (doctor or admin)
    @PostMapping("/doctor/{doctorId}")
    @PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN')")
    public ResponseEntity<?> addSlots(@PathVariable Long doctorId, @RequestBody List<DoctorAvailabilityDTO> slots) {
        List<DoctorAvailabilityDTO> saved = availabilityService.addMultipleSlots(doctorId, slots);
        return ResponseEntity.ok(saved);
    }

    // Add a single slot convenience endpoint
    @PostMapping("/doctor/{doctorId}/single")
    @PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN')")
    public ResponseEntity<DoctorAvailabilityDTO> addSingleSlot(@PathVariable Long doctorId, @RequestBody DoctorAvailabilityDTO slot) {
        return ResponseEntity.ok(availabilityService.addSlot(doctorId, slot));
    }

    // Update a slot
    @PutMapping("/{availabilityId}")
    @PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN')")
    public ResponseEntity<DoctorAvailabilityDTO> updateSlot(@PathVariable Long availabilityId, @RequestBody DoctorAvailabilityDTO dto) {
        return ResponseEntity.ok(availabilityService.updateSlot(availabilityId, dto));
    }

    // Toggle active/inactive (doctor marking unavailable)
    @PatchMapping("/{availabilityId}/toggle")
    @PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> toggleActive(@PathVariable Long availabilityId, @RequestParam boolean active) {
        boolean ok = availabilityService.setSlotActiveFlag(availabilityId, active);
        return ResponseEntity.ok(Map.of("success", ok));
    }

    // Delete slot
    @DeleteMapping("/{availabilityId}")
    @PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> deleteSlot(@PathVariable Long availabilityId) {
        availabilityService.deleteSlot(availabilityId);
        return ResponseEntity.ok(Map.of("deleted", true));
    }
}
