package com.mahesh.HMS.mapper;

import com.mahesh.HMS.dto.PatientDTO;
import com.mahesh.HMS.model.Patient;
import org.springframework.stereotype.Component;

@Component
public class PatientMapper {

    public PatientDTO toDTO(Patient patient) {
        if (patient == null) return null;
        return new PatientDTO(
                patient.getId(),
                patient.getName(),
                patient.getGender(),
                patient.getAge(),
                patient.getUser() != null ? patient.getUser().getRoleId() : null // add roleId
        );
    }

    public Patient toEntity(PatientDTO dto) {
        if (dto == null) return null;
        Patient patient = new Patient();
        patient.setId(dto.getId());
        patient.setName(dto.getName());
        patient.setGender(dto.getGender());
        patient.setAge(dto.getAge());
        // roleId belongs to User entity, so not set here
        return patient;
    }
}
