package at.technikum.exception.rating;

import at.technikum.exception.DatabaseOperationException;

public class RatingAlreadyExistsException extends DatabaseOperationException {
    public RatingAlreadyExistsException(String username, String mediaTitle) {
        super("Rating by user '" + username + "' for media '" + mediaTitle + "' already exists");
    }
}
