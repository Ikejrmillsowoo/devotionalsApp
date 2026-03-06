package com.example.devotionals.controller;

import com.example.devotionals.dto.LessonDto;
import com.example.devotionals.security.RequestUser;
import com.example.devotionals.service.LessonService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/lessons")
public class LessonsController {

    private final LessonService lessonService;

    public LessonsController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @GetMapping("/today")
    public LessonDto today(RequestUser user) {
        var lesson = lessonService.getOrGenerate(user.userId(), LocalDate.now());
        return lessonService.toDto(lesson);
    }

    @GetMapping
    public List<LessonDto> history(RequestUser user,
                                   @RequestParam LocalDate from,
                                   @RequestParam LocalDate to) {
        return lessonService.history(user.userId(), from, to);
    }

    // optional: force regenerate (admin/dev)
    @PostMapping("/today/regenerate")
    public LessonDto regenerate(RequestUser user) {
        var lesson = lessonService.generate(user.userId(), LocalDate.now());
        return lessonService.toDto(lesson);
    }
}