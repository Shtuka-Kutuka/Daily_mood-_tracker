package com.example.dailymoodtracker.dto;

import java.util.List;

public record UserWithGoalsDto(
    String username,
    String email,
    List<GoalDto> goals
) { }