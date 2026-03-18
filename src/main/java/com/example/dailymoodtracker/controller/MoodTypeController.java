package com.example.dailymoodtracker.controller;

import com.example.dailymoodtracker.dto.MoodTypeDto;
import com.example.dailymoodtracker.model.MoodType;
import com.example.dailymoodtracker.service.MoodTypeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

@RestController
@RequestMapping("/api/mood-types")
public class MoodTypeController {

    private final MoodTypeService service;

    public MoodTypeController(MoodTypeService service) {
        this.service = service;
    }

    @GetMapping
    public List<MoodType> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public MoodType getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public MoodType create(@RequestBody MoodTypeDto dto) {
        MoodType moodType = new MoodType();
        moodType.setName(dto.name());
        moodType.setEmoji(dto.emoji());
        moodType.setDescription(dto.description());
        return service.create(moodType);
    }

    @PutMapping("/{id}")
    public MoodType update(@PathVariable Long id, @RequestBody MoodTypeDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}