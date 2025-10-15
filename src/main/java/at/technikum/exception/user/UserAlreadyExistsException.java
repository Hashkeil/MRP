package at.technikum.exception.user;

import at.technikum.exception.DatabaseOperationException;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String username) {
        super("User already exists: " + username);
    }
}
