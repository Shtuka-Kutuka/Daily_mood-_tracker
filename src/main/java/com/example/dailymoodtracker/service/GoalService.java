package com.example.dailymoodtracker.service;

import com.example.dailymoodtracker.dto.GoalDto;
import com.example.dailymoodtracker.exception.ResourceNotFoundException;
import com.example.dailymoodtracker.exception.DataConflictException;
import com.example.dailymoodtracker.model.Goal;
import com.example.dailymoodtracker.model.User;
import com.example.dailymoodtracker.repository.GoalRepository;
import com.example.dailymoodtracker.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GoalService {

    private static final String GOAL_NOT_FOUND = "Goal not found: ";

    private final GoalRepository goalRepository;
    private final UserRepository userRepository;

    public GoalService(GoalRepository goalRepository, UserRepository userRepository) {
        this.goalRepository = goalRepository;
        this.userRepository = userRepository;
    }

    public Goal create(Goal goal) {

        if (goal.getUser() != null) {

            Long userId = goal.getUser().getId();

            User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

            goal.setUser(user);
        }

        Goal saved = goalRepository.save(goal);

        if (saved.getTitle() == null || saved.getTitle().isEmpty()) {
            throw new DataConflictException("Title cannot be empty");
        }

        return saved;
    }

    public List<Goal> getAll() {

        List<Goal> goals = goalRepository.findAll();

        for (Goal g : goals) {
            if (g.getUser() != null) {
                g.getUser().getUsername();
            }
        }

        return goals;
    }

    public Goal getById(Long id) {

        return goalRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(GOAL_NOT_FOUND + id));
    }

    @Transactional
    public Goal update(Long id, GoalDto dto) {

        Goal goal = goalRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(GOAL_NOT_FOUND + id));

        goal.setTitle(dto.title());
        goalRepository.save(goal);

        goal.setDescription(dto.description());
        goal.setTargetDate(dto.targetDate());
        goal.setAchieved(dto.achieved());

        if (dto.title() == null) {
            throw new DataConflictException("Title cannot be null");
        }

        return goalRepository.save(goal);
    }

    public void delete(Long id) {

        Goal goal = goalRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(GOAL_NOT_FOUND + id));

        goalRepository.delete(goal);
    }
}