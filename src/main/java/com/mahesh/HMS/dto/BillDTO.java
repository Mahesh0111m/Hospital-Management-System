package com.mahesh.HMS.dto;

public class BillDTO {
    private Long id;
    private double amount;
    private String status;
    private Long appointmentId;

    // constructor
    public BillDTO(Long id, double amount, String status, Long appointmentId) {
        this.id = id;
        this.amount = amount;
        this.status = status;
        this.appointmentId = appointmentId;
    }

    // getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Long getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Long appointmentId) { this.appointmentId = appointmentId; }
}
