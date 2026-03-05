package com.example.devotionals.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "device_tokens",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id","token"}))
@Getter @Setter @NoArgsConstructor
public class DeviceToken {

    @Id
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private String platform; // WEB/IOS/ANDROID

    @Column(nullable = false)
    private String token;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(name = "last_seen_at")
    private OffsetDateTime lastSeenAt;
}