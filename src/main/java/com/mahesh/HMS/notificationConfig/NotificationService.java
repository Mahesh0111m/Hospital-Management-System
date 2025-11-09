package com.mahesh.HMS.notificationConfig;

import com.mahesh.HMS.dto.NotificationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sendNotification(NotificationDTO notification) {
        // Sends to /topic/notifications/<role>
        String topic = "/topic/notifications/" + notification.getRole();
        System.out.println("Sending notification to topic: " + topic);
        messagingTemplate.convertAndSend(topic, notification);
    }
}
