package com.example.devotionals.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "preferences")
@Getter
@Setter
@NoArgsConstructor
public class Preferences {
    @Id
    @Column(name = "user_id")
    private UUID userId;

    @Column(nullable = false)
    private String timezone = "America/New_York";

    @Column(nullable = false)
    private String translation = "ESV";

    // Stored as JSONB string (simple approach for skeleton)
    @Column(columnDefinition = "jsonb", nullable = false)
    private String topics = "[]";

    @Column(name = "reminder_start_time", nullable = false)
    private LocalTime reminderStartTime = LocalTime.of(8, 0);

    @Column(name = "reminder_end_time", nullable = false)
    private LocalTime reminderEndTime = LocalTime.of(21, 0);

    @Column(name = "reminders_per_day", nullable = false)
    private int remindersPerDay = 3;

    @Column(name = "delivery_channels", columnDefinition = "jsonb", nullable = false)
    private String deliveryChannels = "[\"EMAIL\"]";
}
