package com.example.devotionals.controller;

import com.example.devotionals.dto.ReminderDto;
import com.example.devotionals.repo.ReminderRepository;
import com.example.devotionals.security.RequestUser;
import org.springframework.web.bind.annotation.*;

import java.time.*;
import java.util.List;

@RestController
@RequestMapping("/api/reminders")
public class RemindersController {

    private final ReminderRepository reminders;

    public RemindersController(ReminderRepository reminders) {
        this.reminders = reminders;
    }

    @GetMapping("/today")
    public List<ReminderDto> today(RequestUser user) {
        OffsetDateTime start = LocalDate.now().atStartOfDay().atOffset(ZoneOffset.UTC);
        OffsetDateTime end = start.plusDays(1);

        return reminders.findByUserIdAndScheduledAtBetweenOrderByScheduledAtAsc(user.userId(), start, end)
                .stream()
                .map(r -> new ReminderDto(
                        r.getId(),
                        r.getScheduledAt(),
                        r.getChannel().name(),
                        r.getTitle(),
                        r.getMessage(),
                        r.getSupportingReference(),
                        r.getStatus().name()
                ))
                .toList();
    }
}