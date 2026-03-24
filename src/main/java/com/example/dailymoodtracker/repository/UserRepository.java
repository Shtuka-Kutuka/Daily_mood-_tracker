package com.example.dailymoodtracker.repository;

import com.example.dailymoodtracker.dto.UserDto;
import com.example.dailymoodtracker.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Override
    @EntityGraph(attributePaths = {"moodEntries", "goals"})
    List<User> findAll();

    @Query("SELECT new com.example.dailymoodtracker.dto.UserDto(u.id, u.username, u.email) FROM User u")
    List<UserDto> findAllDto();
}