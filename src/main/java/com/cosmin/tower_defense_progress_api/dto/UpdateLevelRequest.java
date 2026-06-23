package com.cosmin.tower_defense_progress_api.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateLevelRequest(
        @NotNull
        @Min(1)
        @Max(3)
        Integer stars
) {
}
