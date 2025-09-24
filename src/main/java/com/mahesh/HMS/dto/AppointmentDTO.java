package com.mahesh.HMS.dto;

import java.time.LocalDate;

public class AppointmentDTO {
    private Long id;
    private LocalDate date;

    private Long patientId;
    private String patientName;

    private Long doctorId;
    private String doctorName;

    private Long billId;
    private Double billAmount;

    public AppointmentDTO(Long id, LocalDate date, Long patientId, String patientName,
                          Long doctorId, String doctorName, Long billId, Double billAmount) {
        this.id = id;
        this.date = date;
        this.patientId = patientId;
        this.patientName = patientName;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.billId = billId;
        this.billAmount = billAmount;
    }

    // getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }

    public Double getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(Double billAmount) {
        this.billAmount = billAmount;
    }
}

