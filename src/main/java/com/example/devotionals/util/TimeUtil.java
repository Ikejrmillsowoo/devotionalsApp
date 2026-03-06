package com.example.devotionals.util;

import java.time.*;
import java.util.*;

public class TimeUtil {

    public static List<OffsetDateTime> evenlySpacedTimes(
            LocalDate date,
            LocalTime start,
            LocalTime end,
            ZoneId zone,
            int count,
            int jitterMinutes
    ) {
        if (count <= 1) {
            return List.of(date.atTime(start).atZone(zone).toOffsetDateTime());
        }

        long startSeconds = start.toSecondOfDay();
        long endSeconds = end.toSecondOfDay();
        if (endSeconds <= startSeconds) {
            // fallback if bad window
            endSeconds = startSeconds + 10 * 60 * 60;
        }
        long span = endSeconds - startSeconds;
        long step = span / (count - 1);

        Random rand = new Random();
        List<OffsetDateTime> out = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            long sec = startSeconds + (step * i);
            LocalTime t = LocalTime.ofSecondOfDay(Math.min(86399, Math.max(0, sec)));
            int jitter = jitterMinutes > 0 ? rand.nextInt(jitterMinutes * 2 + 1) - jitterMinutes : 0;
            LocalDateTime ldt = date.atTime(t).plusMinutes(jitter);
            out.add(ldt.atZone(zone).toOffsetDateTime());
        }
        out.sort(Comparator.naturalOrder());
        return out;
    }
}