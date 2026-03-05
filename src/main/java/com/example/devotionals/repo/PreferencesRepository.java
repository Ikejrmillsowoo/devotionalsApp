package com.example.devotionals.repo;

import com.example.devotionals.entity.Preferences;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface PreferencesRepository extends JpaRepository<Preferences, UUID> {}