package com.example.dailymoodtracker.controller;
import org.springframework.web.bind.annotation.*;
import com.example.dailymoodtracker.dto.MoodEntryDto;
import com.example.dailymoodtracker.mapper.MoodEntryMapper;
import com.example.dailymoodtracker.service.MoodEntryService;
import java.time.LocalDate;
import java.util.List;
@RestController
@RequestMapping("/api/moods")
public class MoodEntryController {
    private final MoodEntryService service;
    private final MoodEntryMapper mapper;
    public MoodEntryController(MoodEntryService service, MoodEntryMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }
    @GetMapping
    public List<MoodEntryDto> getByDate(@RequestParam LocalDate date) {
        return service.getByDate(date)
            .stream()
            .map(mapper::toDto)
            .toList();
    }
    @GetMapping("/{id}")
    public MoodEntryDto getById(@PathVariable Long id) {
        return mapper.toDto(service.getById(id));
    }
}