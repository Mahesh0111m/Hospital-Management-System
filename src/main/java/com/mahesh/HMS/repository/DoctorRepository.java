package com.mahesh.HMS.repository;

import com.mahesh.HMS.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor , Long> {

    Optional<Doctor> findByUserId(Long userId);

    @Query("SELECT DISTINCT d FROM Doctor d JOIN d.appointments a WHERE a.patient.id = :patientId")
    List<Doctor> findAllDoctorsByPatientId(@Param("patientId") Long patientId);


}