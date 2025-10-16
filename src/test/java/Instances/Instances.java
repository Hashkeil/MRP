package Instances;

import at.technikum.model.*;
import at.technikum.model.enums.AgeRestriction;
import at.technikum.model.enums.MediaType;
import java.time.LocalDateTime;
import java.util.List;


public final class Instances {

    public static final User TEST_USER_1;
    public static final User TEST_USER_2;

    public static final MediaEntry TEST_MEDIA_1;
    public static final MediaEntry TEST_MEDIA_2;

    public static final Favorite TEST_FAVORITE_1;

    public static final Rating TEST_RATING_1;
    public static final Rating TEST_RATING_2;

    static {
        TEST_USER_1 = new User("string1", "string1");
        TEST_USER_1.setId(1L);
        TEST_USER_1.setToken("string1-mrpToken");
        TEST_USER_1.setFavoriteGenre("Action");
        TEST_USER_1.setTotalRatings(3);
        TEST_USER_1.setAverageGivenRating(4.3);

        TEST_USER_2 = new User("string2", "string2");
        TEST_USER_2.setId(2L);
        TEST_USER_2.setToken("string2-mrpToken");
        TEST_USER_2.setFavoriteGenre("Drama");
        TEST_USER_2.setTotalRatings(1);
        TEST_USER_2.setAverageGivenRating(5.0);

        TEST_MEDIA_1 = new MediaEntry(
                "Inception",
                "A mind-bending sci-fi movie",
                MediaType.MOVIE,
                2010,
                AgeRestriction.AGE_12,
                TEST_USER_1.getId()
        );
        TEST_MEDIA_1.setId(100L);
        TEST_MEDIA_1.setGenres(List.of("Sci-Fi", "Thriller"));
        TEST_MEDIA_1.setAverageRating(4.8);
        TEST_MEDIA_1.setTotalRatings(200);
        TEST_MEDIA_1.setFavoritesCount(50);

        TEST_MEDIA_2 = new MediaEntry(
                "Breaking Bad",
                "A chemistry teacher turns to crime",
                MediaType.SERIES,
                2008,
                AgeRestriction.AGE_18,
                TEST_USER_2.getId()
        );
        TEST_MEDIA_2.setId(101L);
        TEST_MEDIA_2.setGenres(List.of("Drama", "Crime"));
        TEST_MEDIA_2.setAverageRating(4.9);
        TEST_MEDIA_2.setTotalRatings(500);
        TEST_MEDIA_2.setFavoritesCount(100);

        TEST_FAVORITE_1 = new Favorite(TEST_USER_1.getId(), TEST_MEDIA_1.getId());
        TEST_FAVORITE_1.setId(500L);
        TEST_FAVORITE_1.setDateAdded(LocalDateTime.of(2025, 1, 10, 14, 0));

        TEST_RATING_1 = new Rating(TEST_USER_1.getId(), TEST_MEDIA_1.getId(), 5, "Amazing movie!");
        TEST_RATING_1.setId(1000L);
        TEST_RATING_1.setConfirmed(true);
        TEST_RATING_1.setLikesCount(10);

        TEST_RATING_2 = new Rating(TEST_USER_2.getId(), TEST_MEDIA_2.getId(), 4, "Very good show.");
        TEST_RATING_2.setId(1001L);
        TEST_RATING_2.setConfirmed(false);
        TEST_RATING_2.setLikesCount(3);
    }

    private Instances() {}
}
