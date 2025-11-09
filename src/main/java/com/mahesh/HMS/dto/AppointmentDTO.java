package com.mahesh.HMS.dto;

import java.time.LocalDate;

public class AppointmentDTO {

    private Long id;
    private LocalDate date;
    private Long patientId;
    private String patientName;
    private Long doctorId;
    private String doctorName;
    private Long availabilityId;
    private String availabilityDate; // optional string presentation
    private String startTime;
    private String endTime;
    private Long billId;
    private Double billAmount;
    private String appointmentCode;

    public AppointmentDTO() {}

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

    // --- getters & setters for every field ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public Long getDoctorId() { return doctorId; }
    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }

    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }

    public Long getAvailabilityId() { return availabilityId; }
    public void setAvailabilityId(Long availabilityId) { this.availabilityId = availabilityId; }

    public String getAvailabilityDate() { return availabilityDate; }
    public void setAvailabilityDate(String availabilityDate) { this.availabilityDate = availabilityDate; }

    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }

    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }

    public Long getBillId() { return billId; }
    public void setBillId(Long billId) { this.billId = billId; }

    public Double getBillAmount() { return billAmount; }
    public void setBillAmount(Double billAmount) { this.billAmount = billAmount; }

    public String getAppointmentCode() { return appointmentCode; }
    public void setAppointmentCode(String appointmentCode) { this.appointmentCode = appointmentCode; }
}
