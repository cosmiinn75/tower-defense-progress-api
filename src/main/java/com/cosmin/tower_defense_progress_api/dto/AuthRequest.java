package com.cosmin.tower_defense_progress_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AuthRequest(

        @NotBlank
        @Size(min = 3, max = 20)
        @Pattern(
                regexp = "^[a-zA-Z0-9_]+$",
                message = "Username can only contain letters, numbers and underscore"
        )
        String username,

        @NotBlank
        @Size(min = 4, max = 30)
        @Pattern(
                regexp = "^\\S+$",
                message = "Password must not contain spaces"
        )
        String password
) {
}