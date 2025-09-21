package com.mahesh.HMS.mapper;

import com.mahesh.HMS.dto.AppointmentDTO;
import com.mahesh.HMS.model.Appointment;
import com.mahesh.HMS.model.Doctor;
import com.mahesh.HMS.model.Patient;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AppointmentMapper {

    public AppointmentDTO toDTO(Appointment appointment) {
        return new AppointmentDTO(
                appointment.getId(),
                appointment.getDate(),
                appointment.getPatient() != null ? appointment.getPatient().getId() : null,
                appointment.getPatient() != null ? appointment.getPatient().getName() : null,
                appointment.getDoctor() != null ? appointment.getDoctor().getId() : null,
                appointment.getDoctor() != null ? appointment.getDoctor().getName() : null,
                appointment.getBill() != null ? appointment.getBill().getId() : null,
                appointment.getBill() != null ? appointment.getBill().getAmount() : null
        );
    }

    public List<AppointmentDTO> toDTOList(List<Appointment> appointments) {
        return appointments.stream().map(this::toDTO).toList();
    }
    public Appointment toEntity(AppointmentDTO dto, Patient patient, Doctor doctor) {
        if (dto == null) return null;

        Appointment appointment = new Appointment();
        appointment.setId(dto.getId());
        appointment.setDate(dto.getDate());
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        return appointment;
    }


}

