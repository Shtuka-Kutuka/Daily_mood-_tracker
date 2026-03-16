package com.example.dailymoodtracker.dto;

public record UserDto(
    Long id,
    String username,
    String email
) { }