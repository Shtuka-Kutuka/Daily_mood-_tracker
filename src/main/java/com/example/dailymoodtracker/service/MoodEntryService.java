package com.example.dailymoodtracker.service;

import com.example.dailymoodtracker.model.MoodEntry;
import com.example.dailymoodtracker.repository.MoodEntryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MoodEntryService {

    private final MoodEntryRepository repository;

    private final List<MoodEntry> fakeData = List.of(
        new MoodEntry(1L, "happy",      LocalDate.of(2026, 3, 1)),
        new MoodEntry(2L, "tired",      LocalDate.of(2026, 3, 1)),
        new MoodEntry(3L, "focused",    LocalDate.of(2026, 3, 1)),
        new MoodEntry(4L, "motivated",  LocalDate.of(2026, 3, 2)),
        new MoodEntry(5L, "excited",    LocalDate.of(2026, 3, 2)),
        new MoodEntry(6L, "stressed",   LocalDate.of(2026, 3, 3)),
        new MoodEntry(7L, "calm",       LocalDate.of(2026, 3, 3)),
        new MoodEntry(8L, "sleepy",     LocalDate.of(2026, 3, 4)),
        new MoodEntry(9L, "productive", LocalDate.of(2026, 3, 4)),
        new MoodEntry(10L, "chill",     LocalDate.of(2026, 3, 4)),
        new MoodEntry(11L, "anxious",   LocalDate.of(2026, 3, 4))
    );

    public MoodEntryService(MoodEntryRepository repository) {
        this.repository = repository;
    }

    public List<MoodEntry> getByDate(LocalDate date) {
        if (date == null) {
            return List.of();
        }

        return fakeData.stream()
            .filter(entry -> date.equals(entry.getDate()))
            .toList();
    }

    public MoodEntry getById(Long id) {
        if (id == null) {
            return null;
        }

        return fakeData.stream()
            .filter(entry -> id.equals(entry.getId()))
            .findFirst()
            .orElse(null);
    }
}