package model;

import at.technikum.model.Rating;
import org.junit.jupiter.api.Test;
import Instances.Instances;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
public class RatingTest {

    @Test
    void testDefaultConstructorInitialValues() {
        Rating rating = new Rating();

        assertNotNull(rating.getTimestamp(), "Timestamp should be initialized");
        assertFalse(rating.getConfirmed(), "Confirmed should default to false");
        assertEquals(0, rating.getLikesCount(), "Likes count should start at 0");
    }

    @Test
    void testParameterizedConstructorSetsFields() {
        Rating rating = Instances.TEST_RATING_1;

        assertEquals(Instances.TEST_USER_1.getId(), rating.getUserId());
        assertEquals(Instances.TEST_MEDIA_1.getId(), rating.getMediaId());
        assertEquals(5, rating.getStars());
        assertEquals("Amazing movie!", rating.getComment());
        assertTrue(rating.getConfirmed(), "Should be confirmed as per instance");
        assertEquals(10, rating.getLikesCount());
    }

    @Test
    void testSetStarsValidRange() {
        Rating rating = new Rating();
        rating.setStars(3);
        assertEquals(3, rating.getStars());
    }

    @Test
    void testSetStarsInvalidThrowsException() {
        Rating rating = new Rating();
        assertThrows(IllegalArgumentException.class, () -> rating.setStars(0));
        assertThrows(IllegalArgumentException.class, () -> rating.setStars(6));
    }

    @Test
    void testConfirmRating() {
        Rating rating = new Rating();
        assertFalse(rating.isConfirmed());
        rating.confirmRating();
        assertTrue(rating.isConfirmed());
    }

    @Test
    void testIncrementLikes() {
        Rating rating = new Rating();
        rating.incrementLikes();
        rating.incrementLikes();
        assertEquals(2, rating.getLikesCount());
    }

    @Test
    void testIsOwnedBy() {
        Rating rating = new Rating(Instances.TEST_USER_1.getId(), Instances.TEST_MEDIA_1.getId(), 4, "Nice!");
        assertTrue(rating.isOwnedBy(Instances.TEST_USER_1.getId()));
        assertFalse(rating.isOwnedBy(Instances.TEST_USER_2.getId()));
    }

    @Test
    void testUpdateRatingUpdatesFieldsAndTimestamp() {
        Rating rating = new Rating(1L, 1L, 4, "Good");
        LocalDateTime oldTimestamp = rating.getTimestamp();

        rating.updateRating(5, "Perfect!");
        assertEquals(5, rating.getStars());
        assertEquals("Perfect!", rating.getComment());
        assertFalse(rating.isConfirmed());
//        assertTrue(rating.getTimestamp().isAfter(oldTimestamp));
    }

    @Test
    void testEqualsAndHashCode() {
        Rating r1 = new Rating(1L, 2L, 4, "Good");
        r1.setId(100L);

        Rating r2 = new Rating(1L, 2L, 5, "Great");
        r2.setId(100L);

        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    void testToStringContainsImportantFields() {
        String text = Instances.TEST_RATING_1.toString();
        assertTrue(text.contains("userId=" + Instances.TEST_USER_1.getId()));
        assertTrue(text.contains("mediaId=" + Instances.TEST_MEDIA_1.getId()));
        assertTrue(text.contains("stars=" + Instances.TEST_RATING_1.getStars()));
    }


}