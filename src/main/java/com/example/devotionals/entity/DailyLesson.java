package com.example.devotionals.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "daily_lessons",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id","lesson_date"}))
@Getter @Setter @NoArgsConstructor
public class DailyLesson {

    @Id
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "lesson_date", nullable = false)
    private LocalDate lessonDate;

    @Column(nullable = false)
    private String theme;

    @Column(name = "primary_reference", nullable = false)
    private String primaryReference;

    @Column(nullable = false, columnDefinition = "text")
    private String summary;

    @Column(columnDefinition = "jsonb", nullable = false)
    private String bullets = "[]";

    @Column(name = "reflection_questions", columnDefinition = "jsonb", nullable = false)
    private String reflectionQuestions = "[]";

    @Column(columnDefinition = "text")
    private String prayer;

    @Column(name = "action_step", columnDefinition = "text")
    private String actionStep;

    @Column(name = "supporting_references", columnDefinition = "jsonb", nullable = false)
    private String supportingReferences = "[]";

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();
}