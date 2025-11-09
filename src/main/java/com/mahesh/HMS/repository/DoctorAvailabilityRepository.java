package com.mahesh.HMS.repository;

import com.mahesh.HMS.model.DoctorAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorAvailabilityRepository extends JpaRepository<DoctorAvailability, Long> {

    // all slots for a doctor
    List<DoctorAvailability> findByDoctorId(Long doctorId);

    // all slots for a doctor on a given date
    List<DoctorAvailability> findByDoctorIdAndDate(Long doctorId, LocalDate date);

    // patient-facing: active and not booked
    List<DoctorAvailability> findByDoctorIdAndActiveTrueAndBookedFalse(Long doctorId);

    // mark a slot booked (transactional modifying query)
    @Modifying
    @Transactional
    @Query("UPDATE DoctorAvailability da SET da.booked = true WHERE da.id = :availabilityId AND da.booked = false")
    int markSlotBooked(Long availabilityId);

    // toggle active flag
    @Modifying
    @Transactional
    @Query("UPDATE DoctorAvailability da SET da.active = :active WHERE da.id = :availabilityId")
    int setActiveFlag(Long availabilityId, boolean active);

    @Query("""
    SELECT da
    FROM DoctorAvailability da
    WHERE da.doctor.id = :doctorId
      AND da.active = true
      AND da.booked = false
      AND da.date >= CURRENT_DATE
""")
    List<DoctorAvailability> findFutureActiveAndAvailableSlots(Long doctorId);

    Optional<DoctorAvailability> findByDoctorIdAndDateAndStartTimeAndEndTime(
            Long doctorId,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime
    );


}
