package com.example.devotionals.service;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class StubAiLessonGenerator implements AiLessonGenerator {
    @Override
    public AiLesson generate(LocalDate date, String translation, List<String> topics, List<String> avoidPrimaryRefs) {
        // Replace this with real LLM call later.
        return new AiLesson(
                "Trust God with direction",
                "Proverbs 3:5-6",
                "Today’s focus is learning to rely on God’s wisdom rather than your own understanding.",
                List.of(
                        "Trust is a choice before it is a feeling.",
                        "God’s direction becomes clearer when we surrender control.",
                        "Small obedience today builds confidence for tomorrow."
                ),
                List.of(
                        "What decision am I leaning on my own understanding for?",
                        "What would trust look like in one practical step today?"
                ),
                "Lord, help me trust You and acknowledge You in my choices today.",
                "Write down one decision and pray over it before acting.",
                List.of("James 1:5", "Psalm 37:5", "Isaiah 26:3", "Romans 8:28")
        );
    }
}