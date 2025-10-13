package at.technikum.exception.rating;

import at.technikum.exception.DatabaseOperationException;

public class RatingNotFoundException extends DatabaseOperationException {
    public RatingNotFoundException(String username, String mediaTitle) {
        super("Rating by user '" + username + "' for media '" + mediaTitle + "' not found");
    }
}
