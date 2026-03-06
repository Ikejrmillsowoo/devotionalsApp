package com.example.devotionals.controller;

import com.example.devotionals.dto.PreferencesDto;
import com.example.devotionals.entity.Preferences;
import com.example.devotionals.security.RequestUser;
import com.example.devotionals.service.PreferencesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/preferences")
public class PreferencesController {

    private final PreferencesService service;
    private final ObjectMapper om = new ObjectMapper();

    public PreferencesController(PreferencesService service) {
        this.service = service;
    }

    @GetMapping
    public PreferencesDto get(RequestUser user) throws Exception {
        Preferences p = service.getOrCreate(user.userId());
        return new PreferencesDto(
                p.getTimezone(),
                p.getTranslation(),
                readList(p.getTopics()),
                p.getReminderStartTime(),
                p.getReminderEndTime(),
                p.getRemindersPerDay(),
                readList(p.getDeliveryChannels())
        );
    }

    @PutMapping
    public PreferencesDto update(RequestUser user, @Valid @RequestBody PreferencesDto dto) throws Exception {
        Preferences p = service.update(user.userId(), dto);
        return new PreferencesDto(
                p.getTimezone(),
                p.getTranslation(),
                readList(p.getTopics()),
                p.getReminderStartTime(),
                p.getReminderEndTime(),
                p.getRemindersPerDay(),
                readList(p.getDeliveryChannels())
        );
    }

    private List<String> readList(String json) throws Exception {
        if (json == null || json.isBlank()) return List.of();
        return om.readValue(json, om.getTypeFactory().constructCollectionType(List.class, String.class));
    }
}