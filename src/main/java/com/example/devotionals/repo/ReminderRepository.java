package com.example.devotionals.repo;

import com.example.devotionals.entity.Reminder;
import com.example.devotionals.entity.enums.ReminderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.OffsetDateTime;
import java.util.*;

public interface ReminderRepository extends JpaRepository<Reminder, UUID> {

    @Query("""
    select r from Reminder r
    where r.status = :status and r.scheduledAt <= :now
    order by r.scheduledAt asc
  """)
    List<Reminder> findDue(ReminderStatus status, OffsetDateTime now);

    List<Reminder> findByUserIdAndScheduledAtBetweenOrderByScheduledAtAsc(
            UUID userId, OffsetDateTime start, OffsetDateTime end);
}