package com.mahesh.HMS.service;

import com.mahesh.HMS.dto.AppointmentDTO;
import com.mahesh.HMS.dto.NotificationDTO;
import com.mahesh.HMS.mapper.AppointmentMapper;
import com.mahesh.HMS.model.Appointment;
import com.mahesh.HMS.model.Doctor;
import com.mahesh.HMS.model.DoctorAvailability;
import com.mahesh.HMS.model.Patient;
import com.mahesh.HMS.notificationConfig.NotificationService;
import com.mahesh.HMS.repository.AppointmentRepository;
import com.mahesh.HMS.repository.DoctorAvailabilityRepository;
import com.mahesh.HMS.repository.DoctorRepository;
import com.mahesh.HMS.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    @Autowired
    private DoctorAvailabilityRepository doctorAvailabilityRepository;

    @Autowired
    private NotificationService notificationService;


    public AppointmentDTO addAppointment(Long patientId , Long doctorId , AppointmentDTO appointmentDTO){
        try{
            Patient patient = patientRepository.findById(patientId).orElseThrow(()->new RuntimeException("No Patient found"));
            Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(()-> new RuntimeException("No Doctor found"));

            Appointment appointment = appointmentMapper.toEntity(appointmentDTO , patient , doctor);
            Appointment savedAppointment = appointmentRepository.save(appointment);
            return appointmentMapper.toDTO(savedAppointment);
        }
        catch (Exception e){
            throw new RuntimeException("Error adding appointment: " + e.getMessage(), e);
        }
    }

    public Page<AppointmentDTO> getAllAppointments(int page , int size){
        Pageable pageable = PageRequest.of(page , size);
        Page<Appointment> appointments = appointmentRepository.findAll(pageable);
        return appointments.map(appointmentMapper::toDTO);
    }

    public AppointmentDTO getAppointmentById(Long id){
        Appointment appointment = appointmentRepository.findById(id).orElse(null);
        return appointmentMapper.toDTO(appointment);
    }

    public AppointmentDTO updateAppointmentbyID(Long id , AppointmentDTO appointmentDTO){
        Optional<Appointment> existingAppointment = appointmentRepository.findById(id);
        if(existingAppointment.isPresent()){
            Appointment a = existingAppointment.get();

            Patient p = patientRepository.findById(appointmentDTO.getPatientId())
                    .orElseThrow(()->new RuntimeException("No Patient found"));

            Doctor d = doctorRepository.findById(appointmentDTO.getDoctorId())
                    .orElseThrow(()-> new RuntimeException("Doctor not found"));

            a.setPatient(p);
            a.setDoctor(d);
            a.setDate(appointmentDTO.getDate());

            Appointment savedAppointment = appointmentRepository.save(a);
            return appointmentMapper.toDTO(savedAppointment);
        } else {
            throw new RuntimeException("No appointment found with id " + id);
        }
    }

    @Transactional
    public void deleteAppointmentbyId(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        // Free up the associated slot
        Doctor doctor = appointment.getDoctor();
        LocalDate date = appointment.getDate();

        doctorAvailabilityRepository.findByDoctorIdAndDate(doctor.getId(), date)
                .stream()
                .filter(DoctorAvailability::isBooked)
                .forEach(slot -> {
                    slot.setBooked(false);
                    doctorAvailabilityRepository.save(slot);
                });

        // Delete appointment
        appointmentRepository.deleteById(id);
    }


    public List<AppointmentDTO> getAppointmentsByPatientId(Long patientId){
        List<Appointment> savedAppointment = appointmentRepository.findByPatientIdWithDetails(patientId);
        return appointmentMapper.toDTOList(savedAppointment , false);
    }

    public List<AppointmentDTO> getAppointmentsByDoctorId(Long userId) {
        List<Appointment> appointments = appointmentRepository.findAllAppointmentsByDoctorId(userId);
        return appointmentMapper.toDTOList(appointments, false);
    }



    public List<AppointmentDTO> getUpcomingAppointmentsForPatient(Long patientId) {
        List<Appointment> appointments = appointmentRepository.findUpcomingAppointmentsByPatientId(patientId);
        return appointmentMapper.toDTOList(appointments, false);
    }

    /**
     * Book appointment using an availability slot.
     * This method is transactional: saves appointment and marks slot booked atomically.
     */
    @Transactional
    public AppointmentDTO bookAppointment(Long availabilityId, Long patientId) {
        // 1) load availability (correct id param usage)
        DoctorAvailability availability = doctorAvailabilityRepository.findById(availabilityId)
                .orElseThrow(() -> new RuntimeException("Availability not found"));

        if (!availability.isActive()) {
            throw new RuntimeException("Slot is inactive");
        }
        if (availability.isBooked()) {
            throw new RuntimeException("Slot is already booked");
        }

        // 2) load patient
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        // 3) doctor associated with availability
        Doctor doctor = availability.getDoctor();
        if (doctor == null) {
            throw new RuntimeException("Doctor not found for this availability");
        }

        // 4) prevent double booking for patient in same time window
        boolean alreadyHasAppointment = appointmentRepository.existsByPatientIdAndDateAndTimeOverlap(
                patientId,
                availability.getDate(),
                availability.getStartTime(),
                availability.getEndTime()
        );
        if (alreadyHasAppointment) {
            throw new RuntimeException("You already have an appointment during this time slot.");
        }

        // 5) mark slot booked (optimistic update)
        int updated = doctorAvailabilityRepository.markSlotBooked(availabilityId);
        if (updated <= 0) {
            throw new RuntimeException("Slot already booked (concurrency).");
        }

        // Refresh availability to ensure managed state (optional)
        availability = doctorAvailabilityRepository.findById(availabilityId)
                .orElseThrow(() -> new RuntimeException("Availability not found after marking"));

        // 6) create appointment and associate availability
        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setDate(availability.getDate());
        appointment.setAvailability(availability);
        appointment.setAppointmentCode(generateNextAppointmentCode());

        Appointment saved = appointmentRepository.save(appointment);

        // 7) send notifications: doctor and patient
        try {
            // Notify doctor
            NotificationDTO docNotif = new NotificationDTO(
                    "DOCTOR",
                    "New Appointment Booked",
                    "Patient " + patient.getName() + " (ID: " + patient.getId() + ") booked an appointment on "
                            + availability.getDate() + " at " + availability.getStartTime() + ".",
                    "DOCTOR",
                    doctor.getId()
            );
            notificationService.sendNotification(docNotif);

            // Notify patient
            NotificationDTO patNotif = new NotificationDTO(
                    "PATIENT",
                    "Appointment Confirmed",
                    "Your appointment with Dr. " + doctor.getName() + " (ID: " + doctor.getId() + ") is confirmed for "
                            + availability.getDate() + " at " + availability.getStartTime() + ".",
                    "PATIENT",
                    patient.getId()
            );
            notificationService.sendNotification(patNotif);
        } catch (Exception e) {
            // don't fail booking if notification fails — log only
            System.err.println("Notification send failed: " + e.getMessage());
        }

        // 8) return mapped DTO
        return appointmentMapper.toDTO(saved, false);
    }






    /**
     * Generates next appointment code (APT0001, APT0002, ...)
     */
    private String generateNextAppointmentCode() {
        Pageable topOne = PageRequest.of(0, 1, org.springframework.data.domain.Sort.by("id").descending());
        Page<Appointment> page = appointmentRepository.findAll(topOne);

        if (page.hasContent()) {
            String lastCode = page.getContent().get(0).getAppointmentCode();
            if (lastCode != null && lastCode.startsWith("APT")) {
                int number = Integer.parseInt(lastCode.substring(3));
                return String.format("APT%04d", number + 1);
            }
        }
        return "APT0001";
    }

    @Transactional
    public AppointmentDTO rescheduleAppointment(Long appointmentId, Long newAvailabilityId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        DoctorAvailability newSlot = doctorAvailabilityRepository.findById(newAvailabilityId)
                .orElseThrow(() -> new RuntimeException("New slot not found"));

        if (!newSlot.isActive()) {
            throw new RuntimeException("New slot is not active");
        }
        if (newSlot.isBooked()) {
            throw new RuntimeException("New slot already booked");
        }

        // Check cutoff: must be at least 6 hours before the current appointment time
        LocalDate today = LocalDate.now();
        if (appointment.getDate().equals(today)) {
            throw new RuntimeException("Cannot reschedule within 6 hours of the appointment time");
        }

        // Free up the old slot (if exists)
        Doctor oldDoctor = appointment.getDoctor();
        DoctorAvailability oldSlot = doctorAvailabilityRepository.findByDoctorIdAndDate(
                        oldDoctor.getId(),
                        appointment.getDate()
                ).stream()
                .filter(DoctorAvailability::isBooked)
                .findFirst()
                .orElse(null);

        if (oldSlot != null) {
            oldSlot.setBooked(false);
            doctorAvailabilityRepository.save(oldSlot);
        }

        // Mark new slot as booked
        boolean marked = doctorAvailabilityRepository.markSlotBooked(newAvailabilityId) > 0;
        if (!marked) {
            throw new RuntimeException("New slot was booked concurrently");
        }

        // Update appointment info
        appointment.setDoctor(newSlot.getDoctor());
        appointment.setDate(newSlot.getDate());

        Appointment updated = appointmentRepository.save(appointment);

        return new AppointmentDTO(
                updated.getId(),
                updated.getDate(),
                updated.getPatient().getId(), updated.getPatient().getName(),
                updated.getDoctor().getId(), updated.getDoctor().getName(),
                updated.getBill() != null ? updated.getBill().getId() : null,
                updated.getBill() != null ? updated.getBill().getAmount() : null
        );
    }


    @Transactional
    public void deleteAppointmentByDoctor(Long userId, Long appointmentId) {
        // ✅ Step 1: Map userId → doctorId
        Doctor doctor = doctorRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Doctor not found for user ID: " + userId));
        Long doctorId = doctor.getId();

        // ✅ Step 2: Find appointment
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        // ✅ Step 3: Ensure correct ownership
        if (!appointment.getDoctor().getId().equals(doctorId)) {
            throw new RuntimeException("You are not authorized to delete this appointment.");
        }

        // ✅ Step 4: Handle availability + timing check
        DoctorAvailability availability = appointment.getAvailability();
        if (availability == null) {
            throw new RuntimeException("Associated slot not found.");
        }

        LocalDateTime slotDateTime = LocalDateTime.of(
                availability.getDate(),
                availability.getStartTime()
        );
        if (slotDateTime.isBefore(LocalDateTime.now().plusHours(6))) {
            throw new RuntimeException("Cannot delete appointment less than 6 hours before start.");
        }

        // ✅ Step 5: Free slot + delete appointment
        availability.setBooked(false);
        doctorAvailabilityRepository.save(availability);
        appointmentRepository.delete(appointment);
    }





}
