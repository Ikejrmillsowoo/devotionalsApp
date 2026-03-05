package com.example.devotionals.service;

import com.example.devotionals.dto.PreferencesDto;
import com.example.devotionals.entity.Preferences;
import com.example.devotionals.repo.PreferencesRepository;
import com.example.devotionals.web.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PreferencesService {

    private final PreferencesRepository repo;
    private final ObjectMapper om = new ObjectMapper();

    public PreferencesService(PreferencesRepository repo) {
        this.repo = repo;
    }

    public Preferences getOrCreate(UUID userId) {
        return repo.findById(userId).orElseGet(() -> {
            Preferences p = new Preferences();
            p.setUserId(userId);
            return repo.save(p);
        });
    }

    public Preferences update(UUID userId, PreferencesDto dto) {
        Preferences p = getOrCreate(userId);
        try {
            p.setTimezone(dto.timezone());
            p.setTranslation(dto.translation());
            p.setTopics(om.writeValueAsString(dto.topics()));
            p.setReminderStartTime(dto.reminderStartTime());
            p.setReminderEndTime(dto.reminderEndTime());
            p.setRemindersPerDay(dto.remindersPerDay());
            p.setDeliveryChannels(om.writeValueAsString(dto.deliveryChannels()));
            return repo.save(p);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize preferences JSON", e);
        }
    }
}