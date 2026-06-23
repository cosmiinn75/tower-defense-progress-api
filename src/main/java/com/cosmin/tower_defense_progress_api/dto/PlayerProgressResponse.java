package com.cosmin.tower_defense_progress_api.dto;

import java.util.List;

public record PlayerProgressResponse(
        int maxLevelUnlocked,
        List<LevelProgressResponse> levels
) {
}
