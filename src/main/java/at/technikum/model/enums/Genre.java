package at.technikum.model.enums;

public enum Genre {
    ACTION,
    ADVENTURE,
    COMEDY,
    DRAMA,
    HORROR,
    ROMANCE,
    THRILLER;

    public static Genre fromString(String text) {

        return Genre.valueOf(text.toUpperCase().replace(" ", "_"));
    }
}
