package com.mahesh.HMS.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class DoctorAvailabilityDTO {
    private Long id;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private boolean booked;
    private boolean active;
    private Long doctorId;
    private String doctorName;

    public DoctorAvailabilityDTO() {}

    public DoctorAvailabilityDTO(Long id, LocalDate date, LocalTime startTime, LocalTime endTime,
                                 boolean booked, boolean active, Long doctorId, String doctorName) {
        this.id = id;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.booked = booked;
        this.active = active;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
    }

    // getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
    public boolean isBooked() { return booked; }
    public void setBooked(boolean booked) { this.booked = booked; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public Long getDoctorId() { return doctorId; }
    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }
    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }
}
