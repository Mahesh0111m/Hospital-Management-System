package com.mahesh.HMS.mapper;

import com.mahesh.HMS.dto.AppointmentDTO;
import com.mahesh.HMS.model.Appointment;
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
}

