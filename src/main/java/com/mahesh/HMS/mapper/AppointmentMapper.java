package com.mahesh.HMS.mapper;

import com.mahesh.HMS.dto.AppointmentDTO;
import com.mahesh.HMS.model.Appointment;
import com.mahesh.HMS.model.Doctor;
import com.mahesh.HMS.model.Patient;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AppointmentMapper {


    public AppointmentDTO toDTO(Appointment appointment, boolean includeBill) {
        if (appointment == null) return null;

        Long billId = null;
        Double billAmount = null;

        if (includeBill && appointment.getBill() != null) {
            billId = appointment.getBill().getId();
            billAmount = appointment.getBill().getAmount();
        }

        return new AppointmentDTO(
                appointment.getId(),
                appointment.getDate(),
                appointment.getPatient() != null ? appointment.getPatient().getId() : null,
                appointment.getPatient() != null ? appointment.getPatient().getName() : null,
                appointment.getDoctor() != null ? appointment.getDoctor().getId() : null,
                appointment.getDoctor() != null ? appointment.getDoctor().getName() : null,
                billId,
                billAmount
        );
    }

    /** Default method includes bill */
    public AppointmentDTO toDTO(Appointment appointment) {
        return toDTO(appointment, true);
    }

    public List<AppointmentDTO> toDTOList(List<Appointment> appointments, boolean includeBill) {
        return appointments.stream().map(a -> toDTO(a, includeBill)).toList();
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
