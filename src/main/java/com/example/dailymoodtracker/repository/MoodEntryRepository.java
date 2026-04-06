package com.example.dailymoodtracker.repository;

import com.example.dailymoodtracker.model.MoodEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MoodEntryRepository extends JpaRepository<MoodEntry, Long> {

    @Override
    @EntityGraph(attributePaths = {"user", "moodType", "tags"})
    List<MoodEntry> findAll();

    @EntityGraph(attributePaths = {"moodType", "tags"})
    List<MoodEntry> findByUserId(Long userId);

    @Query("""
        SELECT m.id FROM MoodEntry m
        WHERE m.user.id = :userId AND m.moodType.name = :moodName
        """)
    Page<Long> findIds(Long userId, String moodName, Pageable pageable);

    @Query("""
        SELECT DISTINCT m FROM MoodEntry m
        JOIN FETCH m.user
        JOIN FETCH m.moodType
        LEFT JOIN FETCH m.tags
        WHERE m.id IN :ids
        """)
    List<MoodEntry> findWithRelations(List<Long> ids);
}