package com.example.dailymoodtracker.mapper;

import com.example.dailymoodtracker.dto.GoalDto;
import com.example.dailymoodtracker.model.Goal;
import com.example.dailymoodtracker.model.User;
import org.springframework.stereotype.Component;

@Component
public class GoalMapper {

    public GoalDto toDto(Goal goal) {

        Long userId = null;

        if (goal.getUser() != null) {
            userId = goal.getUser().getId();
        }

        return new GoalDto(
            goal.getId(),
            userId,
            goal.getTitle(),
            goal.getDescription(),
            goal.getTargetDate(),
            goal.isAchieved()
        );
    }

    public Goal toEntity(GoalDto dto) {

        Goal goal = new Goal();

        goal.setTitle(dto.title());
        goal.setDescription(dto.description());
        goal.setTargetDate(dto.targetDate());
        goal.setAchieved(dto.achieved());

        if (dto.userId() != null) {
            User user = new User();
            user.setId(dto.userId());
            goal.setUser(user);
        }

        return goal;
    }
}