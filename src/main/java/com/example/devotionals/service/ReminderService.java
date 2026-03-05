package com.example.devotionals.service;

import com.example.devotionals.entity.*;
import com.example.devotionals.entity.enums.Channel;
import com.example.devotionals.entity.enums.ReminderStatus;
import com.example.devotionals.repo.ReminderRepository;
import com.example.devotionals.util.TimeUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;

@Service
public class ReminderService {

    private final ReminderRepository reminders;
    private final PreferencesService preferencesService;
    private final ObjectMapper om = new ObjectMapper();

    @Value("${app.reminders.jitterMinutes:10}")
    private int jitterMinutes;

    public ReminderService(ReminderRepository reminders, PreferencesService preferencesService) {
        this.reminders = reminders;
        this.preferencesService = preferencesService;
    }

    public void scheduleRemindersForLesson(Preferences prefs, DailyLesson lesson) {
        // Only schedule if none exist for the lesson/day (basic check)
        // In real version: check for existing reminders between day start/end.
        List<String> channels = readJsonList(prefs.getDeliveryChannels());
        if (channels.isEmpty()) channels = List.of("EMAIL");

        List<String> supportingRefs = readJsonList(lesson.getSupportingReferences());

        ZoneId zone = ZoneId.of(prefs.getTimezone());
        LocalDate date = lesson.getLessonDate();

        int n = Math.max(1, prefs.getRemindersPerDay());
        LocalTime start = prefs.getReminderStartTime();
        LocalTime end = prefs.getReminderEndTime();
        List<OffsetDateTime> times = TimeUtil.evenlySpacedTimes(date, start, end, zone, n, jitterMinutes);

        for (int i = 0; i < times.size(); i++) {
            String supporting = supportingRefs.isEmpty() ? null : supportingRefs.get(i % supportingRefs.size());

            for (String ch : channels) {
                Channel channel = Channel.valueOf(ch);
                Reminder r = new Reminder();
                r.setId(UUID.randomUUID());
                r.setUserId(lesson.getUserId());
                r.setLessonId(lesson.getId());
                r.setScheduledAt(times.get(i));
                r.setChannel(channel);
                r.setTitle("Daily Focus: " + lesson.getTheme());
                r.setMessage(buildMessage(lesson.getTheme(), lesson.getActionStep(), supporting));
                r.setSupportingReference(supporting);
                r.setStatus(ReminderStatus.PENDING);
                reminders.save(r);
            }
        }
    }

    private String buildMessage(String theme, String actionStep, String supportingRef) {
        String base = "Remember today: " + theme + ".";
        if (supportingRef != null) base += " Supporting: " + supportingRef + ".";
        if (actionStep != null && !actionStep.isBlank()) base += " Action: " + actionStep;
        return base;
    }

    public List<Reminder> dueNow(OffsetDateTime now) {
        return reminders.findDue(ReminderStatus.PENDING, now);
    }

    public void markSent(Reminder r) {
        r.setStatus(ReminderStatus.SENT);
        r.setSentAt(OffsetDateTime.now());
        r.setErrorMessage(null);
        reminders.save(r);
    }

    public void markFailed(Reminder r, String error) {
        r.setStatus(ReminderStatus.FAILED);
        r.setErrorMessage(error);
        reminders.save(r);
    }

    private List<String> readJsonList(String json) {
        try {
            if (json == null || json.isBlank()) return List.of();
            return om.readValue(json, om.getTypeFactory().constructCollectionType(List.class, String.class));
        } catch (Exception e) {
            return List.of();
        }
    }
}