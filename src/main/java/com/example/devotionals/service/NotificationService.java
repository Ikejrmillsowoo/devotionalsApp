package com.example.devotionals.service;

import com.example.devotionals.entity.Reminder;

public interface NotificationService {
    void send(Reminder reminder);
}