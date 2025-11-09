package com.mahesh.HMS.mapper;

import com.mahesh.HMS.dto.AppointmentDTO;
import com.mahesh.HMS.model.Appointment;
import com.mahesh.HMS.model.Doctor;
import com.mahesh.HMS.model.Patient;
import com.mahesh.HMS.model.DoctorAvailability;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AppointmentMapper {

    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    public AppointmentDTO toDTO(Appointment appointment, boolean includeBill) {
        if (appointment == null) return null;

        Long billId = null;
        Double billAmount = null;

        if (includeBill && appointment.getBill() != null) {
            billId = appointment.getBill().getId();
            billAmount = appointment.getBill().getAmount();
        }

        AppointmentDTO dto = new AppointmentDTO(
                appointment.getId(),
                appointment.getDate(),
                appointment.getPatient() != null ? appointment.getPatient().getId() : null,
                appointment.getPatient() != null ? appointment.getPatient().getName() : null,
                appointment.getDoctor() != null ? appointment.getDoctor().getId() : null,
                appointment.getDoctor() != null ? appointment.getDoctor().getName() : null,
                billId,
                billAmount
        );

        // appointment code
        dto.setAppointmentCode(appointment.getAppointmentCode());

        // availability details (if present)
        DoctorAvailability av = appointment.getAvailability();
        if (av != null) {
            dto.setAvailabilityId(av.getId());
            dto.setAvailabilityDate(av.getDate() != null ? av.getDate().toString() : null);
            dto.setStartTime(av.getStartTime() != null ? av.getStartTime().format(timeFormatter) : null);
            dto.setEndTime(av.getEndTime() != null ? av.getEndTime().format(timeFormatter) : null);
        }

        return dto;
    }

    /** default includes bill */
    public AppointmentDTO toDTO(Appointment appointment) {
        return toDTO(appointment, true);
    }

    public List<AppointmentDTO> toDTOList(List<Appointment> appointments, boolean includeBill) {
        return appointments.stream().map(a -> toDTO(a, includeBill)).collect(Collectors.toList());
    }

    public Appointment toEntity(AppointmentDTO dto, Patient patient, Doctor doctor) {
        if (dto == null) return null;
        Appointment appointment = new Appointment();
        appointment.setId(dto.getId());
        appointment.setDate(dto.getDate());
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentCode(dto.getAppointmentCode());
        // availability should be set in service using managed entity (do not create new availability here)
        return appointment;
    }
}
