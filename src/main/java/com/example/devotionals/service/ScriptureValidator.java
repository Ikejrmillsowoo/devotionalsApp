package com.example.devotionals.service;

import java.util.List;

public interface ScriptureValidator {
    List<String> validateOrFilter(List<String> refs);
}