package at.technikum.exception.user;

import at.technikum.exception.DatabaseOperationException;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long userId) {
        super("User not found with ID: " + userId);
    }
}
