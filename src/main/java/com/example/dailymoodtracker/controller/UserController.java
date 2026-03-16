package com.example.dailymoodtracker.controller;

import com.example.dailymoodtracker.dto.UserDto;
import com.example.dailymoodtracker.exception.ResourceNotFoundException;
import com.example.dailymoodtracker.model.User;
import com.example.dailymoodtracker.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;


import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository repository;

    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    // CREATE
    @PostMapping
    public UserDto create(@RequestBody UserDto dto) {

        User user = new User();
        user.setUsername(dto.username());
        user.setEmail(dto.email());

        User saved = repository.save(user);

        return new UserDto(
            saved.getId(),
            saved.getUsername(),
            saved.getEmail()
        );
    }

    // GET ALL
    @GetMapping
    public List<User> getAll() {
        return repository.findAll();
    }

    // GET BY ID
    @GetMapping("/{id}")
    public User getById(@PathVariable Long id) {
        return repository.findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException("User not found: " + id));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }
}