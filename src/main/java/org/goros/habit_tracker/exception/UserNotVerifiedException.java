package org.goros.habit_tracker.exception;

public class UserNotVerifiedException extends RuntimeException{
    public UserNotVerifiedException (String message) {
        super(message);
    }
}
