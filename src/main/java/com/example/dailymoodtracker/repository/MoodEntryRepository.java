package com.example.dailymoodtracker.repository;

import com.example.dailymoodtracker.model.MoodEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MoodEntryRepository extends JpaRepository<MoodEntry, Long> {

    List<MoodEntry> findByUserId(Long userId);

    @Query("""
        SELECT m FROM MoodEntry m
        JOIN FETCH m.user u
        JOIN FETCH m.moodType mt
        LEFT JOIN FETCH m.tags t
        WHERE u.id = :userId AND mt.name = :moodName
        """)
    Page<MoodEntry> findComplex(
        @Param("userId") Long userId,
        @Param("moodName") String moodName,
        Pageable pageable
    );

    @Query(value = """
        SELECT m.* FROM mood_entry m
        JOIN users u ON m.user_id = u.id
        JOIN mood_type mt ON m.mood_type_id = mt.id
        WHERE u.id = :userId AND mt.name = :moodName
        """,
        countQuery = """
        SELECT count(*) FROM mood_entry m
        JOIN users u ON m.user_id = u.id
        JOIN mood_type mt ON m.mood_type_id = mt.id
        WHERE u.id = :userId AND mt.name = :moodName
        """,
        nativeQuery = true)
    Page<MoodEntry> findComplexNative(
        @Param("userId") Long userId,
        @Param("moodName") String moodName,
        Pageable pageable
    );
}