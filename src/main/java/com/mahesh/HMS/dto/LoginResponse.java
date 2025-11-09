package com.mahesh.HMS.dto;

import com.mahesh.HMS.model.Role;

public class LoginResponse {
    private String token;
    private String message;
    private Role role;
    private String roleId;
    private Long id;   // <-- add this

    public LoginResponse() {}

    public LoginResponse(String token, String message, Role role, String roleId, Long id) {
        this.token = token;
        this.message = message;
        this.role = role;
        this.roleId = roleId;
        this.id = id;
    }

    // getters & setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public String getRoleId() { return roleId; }
    public void setRoleId(String roleId) { this.roleId = roleId; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
}
