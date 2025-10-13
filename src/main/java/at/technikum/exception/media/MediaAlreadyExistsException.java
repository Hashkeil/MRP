package at.technikum.exception.media;

import at.technikum.exception.DatabaseOperationException;

public class MediaAlreadyExistsException extends DatabaseOperationException {
    public MediaAlreadyExistsException(String title) {
        super("Media with title '" + title + "' already exists");
    }
}
