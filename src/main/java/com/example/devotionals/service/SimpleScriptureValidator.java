package com.example.devotionals.service;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Pattern;

@Component
public class SimpleScriptureValidator implements ScriptureValidator {

    // Very basic: "Book 1:2" or "Book 1:2-3"
    private static final Pattern REF = Pattern.compile("^[1-3]?\\s?[A-Za-z]+\\s\\d+:\\d+(?:-\\d+)?$");

    @Override
    public List<String> validateOrFilter(List<String> refs) {
        return refs.stream()
                .filter(r -> r != null && REF.matcher(r.trim()).matches())
                .map(String::trim)
                .toList();
    }
}