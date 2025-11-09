package com.mahesh.HMS.model;

import jakarta.persistence.*;

import javax.print.Doc;
import java.time.LocalDate;

@Entity
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 👇 Add this field
    @Column(unique = true, nullable = false)
    private String appointmentCode;

    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "patientId", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctorId", nullable = false)
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)   // optional=false if you insist it must be present
    @JoinColumn(name = "availabilityId", nullable = false)
    private DoctorAvailability availability;


    @OneToOne(
            mappedBy = "appointment",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private Bill bill;

    public Bill getBill() { return bill; }
    public void setBill(Bill bill) { this.bill = bill; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAppointmentCode() { return appointmentCode; }
    public void setAppointmentCode(String appointmentCode) { this.appointmentCode = appointmentCode; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }

    public Doctor getDoctor() { return doctor; }
    public void setDoctor(Doctor doctor) { this.doctor = doctor; }

    public DoctorAvailability getAvailability() {
        return availability;
    }

    public void setAvailability(DoctorAvailability availability) {
        this.availability = availability;
    }

}
