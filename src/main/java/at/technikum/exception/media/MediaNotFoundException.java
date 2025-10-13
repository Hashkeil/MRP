package at.technikum.exception.media;

import at.technikum.exception.DatabaseOperationException;

public class MediaNotFoundException extends DatabaseOperationException {
    public MediaNotFoundException(String title) {
        super("Media with title '" + title + "' not found");
    }
}
