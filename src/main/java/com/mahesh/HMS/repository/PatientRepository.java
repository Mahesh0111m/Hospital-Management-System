package com.mahesh.HMS.repository;

import com.mahesh.HMS.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient , Long> {
    @Query("SELECT DISTINCT p FROM Patient p JOIN p.appointments a WHERE a.doctor.id = :doctorId")
    List<Patient> findAllPatientsByDoctorId(@Param("doctorId") Long doctorId);
}