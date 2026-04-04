package org.goros.habit_tracker.model.entity;

import lombok.Data;

import java.util.UUID;

@Data
public class Achievement {
    private UUID achievementId;
    private String title;
    private String description;
    private String badge;
    private Integer xpRequired;
}
