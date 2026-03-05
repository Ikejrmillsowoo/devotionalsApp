package com.example.devotionals.entity;

import com.example.devotionals.entity.enums.Channel;
import com.example.devotionals.entity.enums.ReminderStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "reminders")
@Getter @Setter @NoArgsConstructor
public class Reminder {

    @Id
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "lesson_id", nullable = false)
    private UUID lessonId;

    @Column(name = "scheduled_at", nullable = false)
    private OffsetDateTime scheduledAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Channel channel;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "text")
    private String message;

    @Column(name = "supporting_reference")
    private String supportingReference;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReminderStatus status = ReminderStatus.PENDING;

    @Column(name = "sent_at")
    private OffsetDateTime sentAt;

    @Column(name = "error_message", columnDefinition = "text")
    private String errorMessage;
}