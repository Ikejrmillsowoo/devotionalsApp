package com.example.devotionals.dto;

import jakarta.validation.constraints.NotBlank;

public record DeviceTokenDto(
        @NotBlank String platform,
        @NotBlank String token
) {}