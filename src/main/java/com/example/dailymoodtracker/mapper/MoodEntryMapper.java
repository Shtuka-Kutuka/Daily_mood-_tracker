package com.example.dailymoodtracker.mapper;
import com.example.dailymoodtracker.model.MoodEntry;
import com.example.dailymoodtracker.dto.MoodEntryDto;
import org.springframework.stereotype.Component;
@Component
public class MoodEntryMapper {
    public MoodEntryDto toDto(MoodEntry entry) {
        return new MoodEntryDto(
            entry.getId(),
            entry.getMood(),
            entry.getDate()
        );
    }
}