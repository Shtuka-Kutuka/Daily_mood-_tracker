package com.example.dailymoodtracker.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDate;

@Entity
public class MoodEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mood;
    private LocalDate date;

    public MoodEntry() {
    }

    public MoodEntry(String mood, LocalDate date) {
        this.mood = mood;
        this.date = date;
    }

    // ДОБАВИТЬ ЭТОТ КОНСТРУКТОР
    public MoodEntry(Long id, String mood, LocalDate date) {
        this.id = id;
        this.mood = mood;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}