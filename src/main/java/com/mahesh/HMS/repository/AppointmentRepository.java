package com.mahesh.HMS.repository;

import com.mahesh.HMS.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment , Long> {
    List<Appointment> findByPatientId(Long patientid);

    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId")
    List<Appointment> findAllAppointmentsByDoctorId(@Param("doctorId") Long doctorId);

    @Query("SELECT a FROM Appointment a WHERE a.date > CURRENT_DATE AND a.patient.id = :patientId")
    List<Appointment> findUpcomingAppointmentsByPatientId(@Param("patientId") Long patientId);

}
