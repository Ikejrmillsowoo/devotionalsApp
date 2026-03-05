package com.example.devotionals.service;

import com.example.devotionals.dto.LessonDto;
import com.example.devotionals.entity.DailyLesson;
import com.example.devotionals.entity.Preferences;
import com.example.devotionals.repo.DailyLessonRepository;
import com.example.devotionals.web.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class LessonService {

    private final DailyLessonRepository lessons;
    private final PreferencesService preferencesService;
    private final AiLessonGenerator ai;
    private final ScriptureValidator scriptureValidator;
    private final ObjectMapper om = new ObjectMapper();

    public LessonService(DailyLessonRepository lessons,
                         PreferencesService preferencesService,
                         AiLessonGenerator ai,
                         ScriptureValidator scriptureValidator) {
        this.lessons = lessons;
        this.preferencesService = preferencesService;
        this.ai = ai;
        this.scriptureValidator = scriptureValidator;
    }

    public DailyLesson getOrGenerate(UUID userId, LocalDate date) {
        return lessons.findByUserIdAndLessonDate(userId, date).orElseGet(() -> generate(userId, date));
    }

    public DailyLesson generate(UUID userId, LocalDate date) {
        Preferences p = preferencesService.getOrCreate(userId);

        List<String> topics = readJsonList(p.getTopics());
        // Optional “avoid repeats” could read past week’s primary refs
        List<String> avoid = List.of();

        AiLessonGenerator.AiLesson aiLesson = ai.generate(date, p.getTranslation(), topics, avoid);
        List<String> supporting = scriptureValidator.validateOrFilter(aiLesson.supportingReferences());

        DailyLesson l = new DailyLesson();
        l.setId(UUID.randomUUID());
        l.setUserId(userId);
        l.setLessonDate(date);
        l.setTheme(aiLesson.theme());
        l.setPrimaryReference(aiLesson.primaryReference());
        l.setSummary(aiLesson.summary());
        l.setPrayer(aiLesson.prayer());
        l.setActionStep(aiLesson.actionStep());
        try {
            l.setBullets(om.writeValueAsString(aiLesson.bullets()));
            l.setReflectionQuestions(om.writeValueAsString(aiLesson.reflectionQuestions()));
            l.setSupportingReferences(om.writeValueAsString(supporting));
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize lesson JSON fields", e);
        }

        return lessons.save(l);
    }

    public LessonDto toDto(DailyLesson l) {
        return new LessonDto(
                l.getId(),
                l.getLessonDate(),
                l.getTheme(),
                l.getPrimaryReference(),
                l.getSummary(),
                readJsonList(l.getBullets()),
                readJsonList(l.getReflectionQuestions()),
                l.getPrayer(),
                l.getActionStep(),
                readJsonList(l.getSupportingReferences())
        );
    }

    public List<LessonDto> history(UUID userId, LocalDate from, LocalDate to) {
        return lessons.findByUserIdAndLessonDateBetweenOrderByLessonDateDesc(userId, from, to)
                .stream().map(this::toDto).toList();
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