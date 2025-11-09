package com.mahesh.HMS.service;

import com.mahesh.HMS.dto.DoctorAvailabilityDTO;
import com.mahesh.HMS.model.Doctor;
import com.mahesh.HMS.model.DoctorAvailability;
import com.mahesh.HMS.repository.DoctorAvailabilityRepository;
import com.mahesh.HMS.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorAvailabilityService {

    @Autowired
    private DoctorAvailabilityRepository availabilityRepo;

    @Autowired
    private DoctorRepository doctorRepo;

    public List<DoctorAvailabilityDTO> getAllSlotsForDoctor(Long doctorId) {
        return availabilityRepo.findByDoctorId(doctorId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<DoctorAvailabilityDTO> getAvailableSlotsForDoctor(Long doctorId) {
        return availabilityRepo.findFutureActiveAndAvailableSlots(doctorId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }


    public List<DoctorAvailabilityDTO> getSlotsByDate(Long doctorId, java.time.LocalDate date) {
        return availabilityRepo.findByDoctorIdAndDate(doctorId, date).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public DoctorAvailabilityDTO addSlot(Long doctorId, DoctorAvailabilityDTO dto) {
        Doctor doctor = doctorRepo.findById(doctorId).orElseThrow(() -> new RuntimeException("Doctor not found"));
        DoctorAvailability slot = new DoctorAvailability();
        slot.setDoctor(doctor);
        slot.setDate(dto.getDate());
        slot.setStartTime(dto.getStartTime());
        slot.setEndTime(dto.getEndTime());
        slot.setBooked(false);
        slot.setActive(true);
        DoctorAvailability saved = availabilityRepo.save(slot);
        return toDTO(saved);
    }

    public List<DoctorAvailabilityDTO> addMultipleSlots(Long doctorId, List<DoctorAvailabilityDTO> dtos) {
        Doctor doctor = doctorRepo.findById(doctorId).orElseThrow(() -> new RuntimeException("Doctor not found"));
        List<DoctorAvailability> slots = dtos.stream().map(dto -> {
            DoctorAvailability s = new DoctorAvailability();
            s.setDoctor(doctor);
            s.setDate(dto.getDate());
            s.setStartTime(dto.getStartTime());
            s.setEndTime(dto.getEndTime());
            s.setBooked(false);
            s.setActive(true);
            return s;
        }).collect(Collectors.toList());

        List<DoctorAvailability> saved = availabilityRepo.saveAll(slots);
        return saved.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public DoctorAvailabilityDTO updateSlot(Long availabilityId, DoctorAvailabilityDTO dto) {
        DoctorAvailability slot = availabilityRepo.findById(availabilityId)
                .orElseThrow(() -> new RuntimeException("Slot not found"));
        slot.setDate(dto.getDate());
        slot.setStartTime(dto.getStartTime());
        slot.setEndTime(dto.getEndTime());
        // don't automatically change booked/active here unless requested explicitly
        DoctorAvailability updated = availabilityRepo.save(slot);
        return toDTO(updated);
    }

    public void deleteSlot(Long availabilityId) {
        availabilityRepo.deleteById(availabilityId);
    }

    @Transactional
    public boolean markSlotBooked(Long availabilityId) {
        int updated = availabilityRepo.markSlotBooked(availabilityId);
        return updated > 0;
    }

    @Transactional
    public boolean setSlotActiveFlag(Long availabilityId, boolean active) {
        int updated = availabilityRepo.setActiveFlag(availabilityId, active);
        return updated > 0;
    }

    private DoctorAvailabilityDTO toDTO(DoctorAvailability slot) {
        if (slot == null) return null;
        return new DoctorAvailabilityDTO(
                slot.getId(),
                slot.getDate(),
                slot.getStartTime(),
                slot.getEndTime(),
                slot.isBooked(),
                slot.isActive(),
                slot.getDoctor() != null ? slot.getDoctor().getId() : null,
                slot.getDoctor() != null ? slot.getDoctor().getName() : null
        );
    }
}
