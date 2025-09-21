package com.mahesh.HMS.service;

import com.mahesh.HMS.dto.DoctorDTO;
import com.mahesh.HMS.mapper.DoctorMapper;
import com.mahesh.HMS.model.Doctor;
import com.mahesh.HMS.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private DoctorMapper doctorMapper;

    public DoctorDTO addDoctor(Doctor doctor) {
        Doctor saved = doctorRepository.save(doctor);
        return doctorMapper.toDTO(saved);
    }

    public Page<DoctorDTO> getAllDoctors(int page, int size) {
        return doctorRepository.findAll(PageRequest.of(page, size))
                .map(doctorMapper::toDTO);
    }

    public DoctorDTO getDoctorById(Long id) {
        Optional<Doctor> doctor = doctorRepository.findById(id);
        return doctor.map(doctorMapper::toDTO).orElse(null);
    }

    public DoctorDTO updateDoctorbyID(Long id, Doctor updatedDoctor) {
        Optional<Doctor> existingDoctor = doctorRepository.findById(id);
        if (existingDoctor.isPresent()) {
            Doctor d = existingDoctor.get();
            d.setName(updatedDoctor.getName());
            d.setSpeciality(updatedDoctor.getSpeciality());
            return doctorMapper.toDTO(doctorRepository.save(d));
        }
        return null;
    }

    public void deleteDoctorbyId(Long id) {
        doctorRepository.deleteById(id);
    }
}
