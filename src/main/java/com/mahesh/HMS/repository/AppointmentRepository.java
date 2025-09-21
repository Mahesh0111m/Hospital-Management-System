package com.mahesh.HMS.repository;

import com.mahesh.HMS.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment , Long> {
    List<Appointment> findByPatientId(Long patientid);
}
