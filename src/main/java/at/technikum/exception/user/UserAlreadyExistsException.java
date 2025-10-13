package at.technikum.exception.user;

import at.technikum.exception.DatabaseOperationException;

public class UserAlreadyExistsException extends DatabaseOperationException {
    public UserAlreadyExistsException(String username) {
        super("Username '" + username + "' already exists");
    }

}
