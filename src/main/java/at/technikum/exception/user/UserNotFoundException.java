package at.technikum.exception.user;

import at.technikum.exception.DatabaseOperationException;

public class UserNotFoundException extends DatabaseOperationException {
    public UserNotFoundException(Long userId) {
        super("User with ID '" + userId + "' not found");
    }
}

