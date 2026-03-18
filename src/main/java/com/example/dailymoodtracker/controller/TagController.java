package com.example.dailymoodtracker.controller;

import com.example.dailymoodtracker.dto.TagDto;
import com.example.dailymoodtracker.model.Tag;
import com.example.dailymoodtracker.service.TagService;
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
@RequestMapping("/api/tags")
public class TagController {

    private final TagService service;

    public TagController(TagService service) {
        this.service = service;
    }

    @GetMapping
    public List<Tag> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Tag getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public Tag create(@RequestBody TagDto dto) {
        Tag tag = new Tag();
        tag.setName(dto.name());
        tag.setColor(dto.color());
        return service.create(tag);
    }

    @PutMapping("/{id}")
    public Tag update(@PathVariable Long id, @RequestBody TagDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}