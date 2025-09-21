package com.mahesh.HMS.mapper;

import com.mahesh.HMS.dto.DoctorDTO;
import com.mahesh.HMS.model.Doctor;
import org.springframework.stereotype.Component;

@Component
public class DoctorMapper {

    public DoctorDTO toDTO(Doctor doctor) {
        if (doctor == null) return null;
        return new DoctorDTO(
                doctor.getId(),
                doctor.getName(),
                doctor.getSpeciality()
        );
    }

    public Doctor toEntity(DoctorDTO dto) {
        if (dto == null) return null;
        Doctor doctor = new Doctor();
        doctor.setId(dto.getId());
        doctor.setName(dto.getName());
        doctor.setSpeciality(dto.getSpeciality());
        return doctor;
    }
}
