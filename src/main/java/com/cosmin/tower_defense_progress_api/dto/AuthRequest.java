package com.cosmin.tower_defense_progress_api.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthRequest(

        @NotBlank
        String username,
        @NotBlank
        String password
) {
}
