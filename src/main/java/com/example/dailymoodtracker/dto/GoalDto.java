package com.example.dailymoodtracker.dto;

import java.time.LocalDate;

public record GoalDto(
    Long id,
    Long userId,
    String title,
    String description,
    LocalDate targetDate,
    boolean achieved
) { }