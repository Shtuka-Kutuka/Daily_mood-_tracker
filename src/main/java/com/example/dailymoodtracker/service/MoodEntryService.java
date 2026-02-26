package com.example.dailymoodtracker.service;
import com.example.dailymoodtracker.repository.MoodEntryRepository;
import com.example.dailymoodtracker.model.MoodEntry;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
@Service
public class MoodEntryService {
    private final MoodEntryRepository repository;
    public MoodEntryService(MoodEntryRepository repository) {
        this.repository = repository;
    }
    public List<MoodEntry> getByDate(LocalDate date) {
        return repository.findByDate(date);
    }
    public MoodEntry getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("MoodEntry not found"));
    }
}