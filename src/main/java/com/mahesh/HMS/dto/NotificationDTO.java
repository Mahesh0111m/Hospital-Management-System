package com.mahesh.HMS.dto;

public class NotificationDTO {
    private String role;        // the role/topic to send
    private String subject;     // title of notification
    private String content;     // message
    private String entityType;  // PATIENT / DOCTOR
    private Long entityId;      // optional: id of entity

    public NotificationDTO(String role, String subject, String content, String entityType, Long entityId) {
        this.role = role;
        this.subject = subject;
        this.content = content;
        this.entityType = entityType;
        this.entityId = entityId;
    }

    // Getters
    public String getRole() { return role; }
    public String getSubject() { return subject; }
    public String getContent() { return content; }
    public String getEntityType() { return entityType; }
    public Long getEntityId() { return entityId; }

    // Setters (optional)
    public void setRole(String role) { this.role = role; }
    public void setSubject(String subject) { this.subject = subject; }
    public void setContent(String content) { this.content = content; }
    public void setEntityType(String entityType) { this.entityType = entityType; }
    public void setEntityId(Long entityId) { this.entityId = entityId; }
}
