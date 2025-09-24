package com.mahesh.HMS.repository;

import com.mahesh.HMS.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill , Long> {

    @Query("SELECT b FROM Bill b JOIN b.appointment a WHERE a.patient.id = :patientId")
    List<Bill> findAllByPatientId(@Param("patientId") Long patientId);

    @Query("SELECT SUM(b.amount) FROM Bill b JOIN b.appointment a WHERE a.patient.id = :patientId")
    Double getTotalBilledAmountByPatientId(@Param("patientId") Long patientId);

    @Query("SELECT SUM(b.amount) FROM Bill b JOIN b.appointment a WHERE a.doctor.id = :doctorId")
    Double getTotalRevenueByDoctor(@Param("doctorId") Long doctorId);


}