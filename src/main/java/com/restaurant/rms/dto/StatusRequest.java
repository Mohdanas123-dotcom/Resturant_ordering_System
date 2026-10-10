package com.restaurant.rms.dto;

import jakarta.validation.constraints.NotNull;

public record StatusRequest(@NotNull String status) {}
