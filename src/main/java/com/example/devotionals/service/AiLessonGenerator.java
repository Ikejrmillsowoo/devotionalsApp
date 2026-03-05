package com.example.devotionals.service;

import java.time.LocalDate;
import java.util.List;

public interface AiLessonGenerator {

    record AiLesson(
            String theme,
            String primaryReference,
            String summary,
            List<String> bullets,
            List<String> reflectionQuestions,
            String prayer,
            String actionStep,
            List<String> supportingReferences
    ) {}

    AiLesson generate(LocalDate date, String translation, List<String> topics, List<String> avoidPrimaryRefs);
}