package com.example.devotionals.jobs;

import com.example.devotionals.entity.DailyLesson;
import com.example.devotionals.entity.Preferences;
import com.example.devotionals.repo.UserRepository;
import com.example.devotionals.service.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.*;
import java.util.UUID;

@Component
public class DailyLessonJob {

    private final UserRepository users;
    private final PreferencesService preferencesService;
    private final LessonService lessonService;
    private final ReminderService reminderService;

    public DailyLessonJob(UserRepository users,
                          PreferencesService preferencesService,
                          LessonService lessonService,
                          ReminderService reminderService) {
        this.users = users;
        this.preferencesService = preferencesService;
        this.lessonService = lessonService;
        this.reminderService = reminderService;
    }

    @Scheduled(cron = "${app.jobs.dailyLessonCron}")
    public void run() {
        for (var user : users.findAll()) {
            UUID userId = user.getId();
            Preferences prefs = preferencesService.getOrCreate(userId);

            ZoneId zone = ZoneId.of(prefs.getTimezone());
            ZonedDateTime now = ZonedDateTime.now(zone);

            // Generate around early morning (e.g., 5am-10am local)
            int hour = now.getHour();
            if (hour < 5 || hour > 10) continue;

            LocalDate today = now.toLocalDate();
            DailyLesson lesson = lessonService.getOrGenerate(userId, today);

            // naive: always schedule (duplicates possible if job repeats)
            // For production: guard by checking if reminders already exist for lessonId.
            reminderService.scheduleRemindersForLesson(prefs, lesson);
        }
    }
}