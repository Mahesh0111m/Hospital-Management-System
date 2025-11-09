package com.mahesh.HMS.dto;

public class PatientDTO {
    private Long id;
    private String name;
    private String gender;
    private int age;
    private String roleId; // add this

    public PatientDTO() {
    }

    // constructor
    public PatientDTO(Long id, String name, String gender, int age, String roleId) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.roleId = roleId;
    }

    // getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getRoleId() { return roleId; }
    public void setRoleId(String roleId) { this.roleId = roleId; }
}
