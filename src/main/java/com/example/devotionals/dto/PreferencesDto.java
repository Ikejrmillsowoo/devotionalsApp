package com.example.devotionals.dto;

import jakarta.validation.constraints.*;
import java.time.LocalTime;
import java.util.List;

public record PreferencesDto(
        @NotBlank String timezone,
        @NotBlank String translation,
        @NotNull List<String> topics,
        @NotNull LocalTime reminderStartTime,
        @NotNull LocalTime reminderEndTime,
        @Min(1) @Max(10) int remindersPerDay,
        @NotNull List<String> deliveryChannels
) {}