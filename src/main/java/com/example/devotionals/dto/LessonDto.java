package com.example.devotionals.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record LessonDto(
        UUID id,
        LocalDate lessonDate,
        String theme,
        String primaryReference,
        String summary,
        List<String> bullets,
        List<String> reflectionQuestions,
        String prayer,
        String actionStep,
        List<String> supportingReferences
) {}