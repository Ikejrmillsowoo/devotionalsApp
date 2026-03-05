package com.example.devotionals.service;

import com.example.devotionals.entity.Reminder;
import org.springframework.stereotype.Service;

@Service
public class EmailNotificationService implements NotificationService {

    @Override
    public void send(Reminder reminder) {
        // MVP stub: replace with JavaMailSender or SendGrid.
        // For now just log.
        System.out.println("[EMAIL] To userId=" + reminder.getUserId());
        System.out.println("Title: " + reminder.getTitle());
        System.out.println("Msg: " + reminder.getMessage());
    }
}