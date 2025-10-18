package model;

import Instances.Instances;
import at.technikum.model.MediaEntry;
import at.technikum.model.enums.AgeRestriction;
import at.technikum.model.enums.MediaType;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MediaEntryTest {

    @Test
    void testBasicFields() {
        MediaEntry media = Instances.TEST_MEDIA_1;

        assertEquals("Inception", media.getTitle());
        assertEquals("A mind-bending sci-fi movie", media.getDescription());
        assertEquals(MediaType.MOVIE, media.getType());
        assertEquals(AgeRestriction.AGE_12, media.getAgeRestriction());
        assertEquals(2010, media.getReleaseYear());
        assertEquals(Instances.TEST_USER_1.getId(), media.getCreatorId());
    }

    @Test
    void testCreatedAndModifiedTimestamps() {
        MediaEntry media = new MediaEntry();
        assertNotNull(media.getCreatedDate());
        assertNotNull(media.getLastModified());

        media.setTitle("New Title");
        LocalDateTime modifiedAfter = media.getLastModified();
        assertTrue(modifiedAfter.isAfter(media.getCreatedDate()));
    }

    @Test
    void testUpdateAverageRating() {
        MediaEntry media = new MediaEntry();
        media.updateAverageRating(4.5, 10);

        assertEquals(4.5, media.getAverageRating());
        assertEquals(10, media.getTotalRatings());
    }

    @Test
    void testIncrementAndDecrementFavorites() {
        MediaEntry media = new MediaEntry();
        assertEquals(0, media.getFavoritesCount());

        media.incrementFavorites();
        assertEquals(1, media.getFavoritesCount());

        media.decrementFavorites();
        assertEquals(0, media.getFavoritesCount());

        // Should not go below zero
        media.decrementFavorites();
        assertEquals(0, media.getFavoritesCount());
    }

    @Test
    void testIsCreatedBy() {
        MediaEntry media = Instances.TEST_MEDIA_1;
        assertTrue(media.isCreatedBy(Instances.TEST_USER_1.getId()));
        assertFalse(media.isCreatedBy(Instances.TEST_USER_2.getId()));
    }

    @Test
    void testGenresAreImmutableCopy() {
        MediaEntry media = Instances.TEST_MEDIA_1;
        List<String> genres = media.getGenres();

        assertTrue(genres.contains("Sci-Fi"));
        genres.add("NewGenre");

        assertFalse(media.getGenres().contains("NewGenre"));
    }


}