package com.example.devotionals.repo;

import com.example.devotionals.entity.DeviceToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface DeviceTokenRepository extends JpaRepository<DeviceToken, UUID> {
    List<DeviceToken> findByUserId(UUID userId);
}