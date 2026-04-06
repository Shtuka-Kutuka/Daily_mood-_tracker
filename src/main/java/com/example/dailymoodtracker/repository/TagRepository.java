package com.example.dailymoodtracker.repository;

import com.example.dailymoodtracker.model.Tag;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {

    @EntityGraph(attributePaths = {"moodEntries"})
    List<Tag> findAll();

}