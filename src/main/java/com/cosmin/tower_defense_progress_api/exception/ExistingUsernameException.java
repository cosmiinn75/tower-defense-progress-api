package com.cosmin.tower_defense_progress_api.exception;

public class ExistingUsernameException extends RuntimeException {
    public ExistingUsernameException(String message) {
        super(message);
    }
}
