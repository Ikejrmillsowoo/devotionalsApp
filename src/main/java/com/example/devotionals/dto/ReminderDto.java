package com.example.devotionals.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ReminderDto(
        UUID id,
        OffsetDateTime scheduledAt,
        String channel,
        String title,
        String message,
        String supportingReference,
        String status
) {}