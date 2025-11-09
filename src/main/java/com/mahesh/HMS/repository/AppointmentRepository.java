package com.mahesh.HMS.repository;

import com.mahesh.HMS.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("""
SELECT a FROM Appointment a
JOIN FETCH a.doctor d
JOIN FETCH a.patient p
LEFT JOIN FETCH a.availability av
WHERE p.id = :patientId
""")
    List<Appointment> findByPatientIdWithDetails(Long patientId);


    @Query("SELECT a FROM Appointment a WHERE a.doctor.user.id = :userId")
    List<Appointment> findAllAppointmentsByDoctorId(@Param("userId") Long userId);

    @Query("SELECT a FROM Appointment a WHERE a.date > CURRENT_DATE AND a.patient.id = :patientId")
    List<Appointment> findUpcomingAppointmentsByPatientId(@Param("patientId") Long patientId);

    /**
     * Returns true if patient already has an appointment that overlaps the given time range on the same date.
     * It joins appointment -> availability (assumes Appointment has a ManyToOne availability relationship).
     */
    @Query("""
        SELECT CASE WHEN COUNT(a) > 0 THEN TRUE ELSE FALSE END
        FROM Appointment a
        JOIN a.availability av
        WHERE a.patient.id = :patientId
          AND av.date = :date
          AND av.startTime < :newEndTime
          AND av.endTime > :newStartTime
    """)
    boolean existsByPatientIdAndDateAndTimeOverlap(
            @Param("patientId") Long patientId,
            @Param("date") java.time.LocalDate date,
            @Param("newStartTime") java.time.LocalTime newStartTime,
            @Param("newEndTime") java.time.LocalTime newEndTime
    );
}
