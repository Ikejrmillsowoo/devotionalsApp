package com.example.devotionals.controller;

import com.example.devotionals.dto.DeviceTokenDto;
import com.example.devotionals.entity.DeviceToken;
import com.example.devotionals.repo.DeviceTokenRepository;
import com.example.devotionals.security.RequestUser;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/device-tokens")
public class DeviceTokensController {

    private final DeviceTokenRepository repo;

    public DeviceTokensController(DeviceTokenRepository repo) {
        this.repo = repo;
    }

    @PostMapping
    public DeviceToken register(RequestUser user, @Valid @RequestBody DeviceTokenDto dto) {
        DeviceToken t = new DeviceToken();
        t.setId(UUID.randomUUID());
        t.setUserId(user.userId());
        t.setPlatform(dto.platform());
        t.setToken(dto.token());
        t.setLastSeenAt(OffsetDateTime.now());
        return repo.save(t);
    }

    @DeleteMapping("/{id}")
    public void delete(RequestUser user, @PathVariable UUID id) {
        // For MVP: delete by id; later ensure belongs to user
        repo.deleteById(id);
    }
}