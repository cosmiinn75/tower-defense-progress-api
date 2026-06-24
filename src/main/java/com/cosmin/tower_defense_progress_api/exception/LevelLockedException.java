package com.cosmin.tower_defense_progress_api.exception;

public class LevelLockedException extends RuntimeException {
    public LevelLockedException(String message) {
        super(message);
    }
}
