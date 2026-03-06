package com.example.devotionals.jobs;

import com.example.devotionals.entity.Reminder;
import com.example.devotionals.entity.enums.Channel;
import com.example.devotionals.service.EmailNotificationService;
import com.example.devotionals.service.ReminderService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;

@Component
public class ReminderDispatchJob {

    private final ReminderService reminderService;
    private final EmailNotificationService email;

    public ReminderDispatchJob(ReminderService reminderService, EmailNotificationService email) {
        this.reminderService = reminderService;
        this.email = email;
    }

    @Scheduled(cron = "${app.jobs.reminderDispatchCron}")
    public void run() {
        List<Reminder> due = reminderService.dueNow(OffsetDateTime.now());
        for (Reminder r : due) {
            try {
                if (r.getChannel() == Channel.EMAIL) {
                    email.send(r);
                } else {
                    // PUSH/SMS later
                    throw new UnsupportedOperationException("Channel not implemented: " + r.getChannel());
                }
                reminderService.markSent(r);
            } catch (Exception ex) {
                reminderService.markFailed(r, ex.getMessage());
            }
        }
    }
}