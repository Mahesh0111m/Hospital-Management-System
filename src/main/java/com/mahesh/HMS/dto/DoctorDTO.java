package com.mahesh.HMS.dto;

public class DoctorDTO {

    private Long id;
    private String name;
    private String speciality;
    private String roleId; // added

    public DoctorDTO() {
    }

    // constructor with roleId
    public DoctorDTO(Long id, String name, String speciality, String roleId) {
        this.id = id;
        this.name = name;
        this.speciality = speciality;
        this.roleId = roleId;
    }

    // getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSpeciality() { return speciality; }
    public void setSpeciality(String speciality) { this.speciality = speciality; }

    public String getRoleId() { return roleId; }
    public void setRoleId(String roleId) { this.roleId = roleId; }
}
