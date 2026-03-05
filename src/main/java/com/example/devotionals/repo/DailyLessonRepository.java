package com.example.devotionals.repo;

import com.example.devotionals.entity.DailyLesson;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.*;

public interface DailyLessonRepository extends JpaRepository<DailyLesson, UUID> {
    Optional<DailyLesson> findByUserIdAndLessonDate(UUID userId, LocalDate lessonDate);
    List<DailyLesson> findByUserIdAndLessonDateBetweenOrderByLessonDateDesc(UUID userId, LocalDate from, LocalDate to);
}