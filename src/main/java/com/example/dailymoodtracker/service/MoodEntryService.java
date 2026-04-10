package com.example.dailymoodtracker.service;

import com.example.dailymoodtracker.cache.MoodEntryQueryKey;
import com.example.dailymoodtracker.dto.MoodEntryDto;
import com.example.dailymoodtracker.exception.DataConflictException;
import com.example.dailymoodtracker.exception.ResourceNotFoundException;
import com.example.dailymoodtracker.model.MoodEntry;
import com.example.dailymoodtracker.model.MoodType;
import com.example.dailymoodtracker.model.Tag;
import com.example.dailymoodtracker.model.User;
import com.example.dailymoodtracker.repository.MoodEntryRepository;
import com.example.dailymoodtracker.repository.MoodTypeRepository;
import com.example.dailymoodtracker.repository.TagRepository;
import com.example.dailymoodtracker.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class MoodEntryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MoodEntryService.class);

    private final MoodEntryRepository repository;
    private final MoodTypeRepository moodTypeRepo;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;

    private final Map<MoodEntryQueryKey, Page<MoodEntry>> cache = new ConcurrentHashMap<>();

    public MoodEntryService(
        MoodEntryRepository repository,
        MoodTypeRepository moodTypeRepo,
        UserRepository userRepository,
        TagRepository tagRepository
    ) {
        this.repository = repository;
        this.moodTypeRepo = moodTypeRepo;
        this.userRepository = userRepository;
        this.tagRepository = tagRepository;
    }

    public Page<MoodEntry> findComplexNative(Long userId, String moodName, Pageable pageable) {

        MoodEntryQueryKey key = new MoodEntryQueryKey(
            userId, moodName, pageable.getPageNumber(), pageable.getPageSize()
        );

        return cache.computeIfAbsent(key, k -> {

            LOGGER.debug("DB NATIVE query executed: userId={}, moodName={}, page={}, size={}",
                userId, moodName, pageable.getPageNumber(), pageable.getPageSize());

            Page<MoodEntry> page = repository.findComplexNative(userId, moodName, pageable);
            List<MoodEntry> entries = page.getContent();

            if (entries.isEmpty()) {
                return page;
            }

            Map<Long, User> users = userRepository.findAllById(
                entries.stream()
                    .map(MoodEntry::getUserId)
                    .distinct()
                    .toList()
            ).stream().collect(Collectors.toMap(User::getId, u -> u));

            Map<Long, MoodType> moods = moodTypeRepo.findAllById(
                entries.stream()
                    .map(MoodEntry::getMoodTypeId)
                    .distinct()
                    .toList()
            ).stream().collect(Collectors.toMap(MoodType::getId, m -> m));

            Map<Long, Set<Tag>> tagsMap = loadTags(entries);

            for (MoodEntry e : entries) {
                e.setUser(users.get(e.getUserId()));
                e.setMoodType(moods.get(e.getMoodTypeId()));
                e.setTags(tagsMap.getOrDefault(e.getId(), Collections.emptySet()));
            }

            return page;
        });
    }

    private Map<Long, Set<Tag>> loadTags(List<MoodEntry> entries) {

        List<Long> ids = entries.stream()
            .map(MoodEntry::getId)
            .distinct()
            .toList();

        if (ids.isEmpty()) {
            return Collections.emptyMap();
        }

        List<Object[]> rows = tagRepository.findTagsByMoodEntryIds(ids);

        Map<Long, Set<Tag>> result = new HashMap<>();

        for (Object[] row : rows) {
            Long moodEntryId = (Long) row[0];
            Tag tag = (Tag) row[1];

            result.computeIfAbsent(moodEntryId, k -> new HashSet<>()).add(tag);
        }

        return result;
    }

    public List<MoodEntry> findAll() {
        return repository.findAll();
    }

    public List<MoodEntry> findByUserId(Long userId) {
        return repository.findByUserId(userId);
    }

    public Page<MoodEntry> findComplex(Long userId, String moodName, Pageable pageable) {

        MoodEntryQueryKey key = new MoodEntryQueryKey(
            userId, moodName, pageable.getPageNumber(), pageable.getPageSize()
        );

        if (cache.containsKey(key)) {
            LOGGER.debug("CACHE HIT (JPQL): userId={}, moodName={}, page={}, size={}",
                userId, moodName, pageable.getPageNumber(), pageable.getPageSize());
            return cache.get(key);
        }

        LOGGER.debug("CACHE MISS → DB JPQL: userId={}, moodName={}, page={}, size={}",
            userId, moodName, pageable.getPageNumber(), pageable.getPageSize());

        Page<MoodEntry> page = repository.findComplex(userId, moodName, pageable);

        cache.put(key, page);

        return page;
    }

    public MoodEntry save(MoodEntry entry, MoodEntryDto dto) {

        if (entry.getEntryDate() == null) {
            throw new DataConflictException("Entry date cannot be null");
        }

        Long userId = entry.getUser().getId();

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        entry.setUser(user);

        if (dto.mood() != null) {
            MoodType mt = moodTypeRepo.findByName(dto.mood())
                .orElseGet(() -> moodTypeRepo.save(new MoodType(dto.mood(), null, null)));
            entry.setMoodType(mt);
        }

        if (dto.tagIds() != null) {
            Set<Tag> tags = dto.tagIds().stream()
                .map(id -> tagRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Tag not found: " + id)))
                .collect(Collectors.toSet());

            entry.setTags(tags);
        }

        cache.clear();
        LOGGER.debug("Cache cleared after SAVE");

        return repository.save(entry);
    }

    @Transactional
    public MoodEntry update(Long id, MoodEntryDto dto) {

        MoodEntry entry = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Mood not found: " + id));

        entry.setEntryDate(dto.date());

        cache.clear();
        LOGGER.debug("Cache cleared after UPDATE");

        return repository.save(entry);
    }

    public void delete(Long id) {

        MoodEntry entry = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Mood not found: " + id));

        repository.delete(entry);

        cache.clear();
        LOGGER.debug("Cache cleared after DELETE");
    }
}