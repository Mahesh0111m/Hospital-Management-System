package com.mahesh.HMS;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;

import java.security.Principal;

public class UserInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        // Check if the command is CONNECT
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {

            // Get the value of the 'login' header sent by the client
            String userLogin = accessor.getFirstNativeHeader("login");

            if (userLogin != null && !userLogin.isEmpty()) {
                // Set a simple Principal using the username
                Principal principal = () -> userLogin;
                accessor.setUser(principal);

                System.out.println("STOMP Session mapped to user: " + userLogin);
            }
        }
        return message;
    }
}