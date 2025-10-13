package at.technikum.model.enums;

public enum MediaType {
    MOVIE,
    SERIES,
    GAME;


    public static MediaType fromString(String text) {
        try {
            return MediaType.valueOf(text.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown media type: " + text);
        }
    }

}
