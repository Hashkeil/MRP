package service;

import Instances.Instances;
import at.technikum.model.Rating;
import at.technikum.model.MediaEntry;
import at.technikum.repository.RatingRepository;
import at.technikum.repository.MediaRepository;
import at.technikum.service.RatingService;
import at.technikum.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RatingServiceTest {

    private RatingRepository ratingRepo;
    private MediaRepository mediaRepo;
    private UserService userService;
    private RatingService ratingService;

    @BeforeEach
    void setUp() {
        ratingRepo = mock(RatingRepository.class);
        mediaRepo = mock(MediaRepository.class);
        userService = mock(UserService.class);
        ratingService = new RatingService(ratingRepo, mediaRepo, userService);
    }

    private void mockMedia(Long mediaId, MediaEntry media) throws SQLException {
        when(mediaRepo.findById(mediaId)).thenReturn(Optional.of(media));
    }

    private void mockRating(Long ratingId, Rating rating) throws SQLException {
        when(ratingRepo.findById(ratingId)).thenReturn(Optional.of(rating));
    }

    @Test
    void testCreateRating_Success() throws Exception {
        mockMedia(Instances.TEST_MEDIA_1.getId(), Instances.TEST_MEDIA_1);
        when(ratingRepo.findByUserIdAndMediaId(Instances.TEST_USER_1.getId(), Instances.TEST_MEDIA_1.getId()))
                .thenReturn(Optional.empty());
        when(ratingRepo.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Rating rating = ratingService.createRating(
                Instances.TEST_USER_1.getId(),
                Instances.TEST_MEDIA_1.getId(),
                5,
                "Amazing movie!"
        );

        assertEquals(5, rating.getStars());
        verify(mediaRepo).update(Instances.TEST_MEDIA_1);
        verify(userService).updateUserStatistics(Instances.TEST_USER_1.getId());
    }


    @Test
    void testCreateRating_AlreadyRated() throws SQLException {
        mockMedia(Instances.TEST_MEDIA_1.getId(), Instances.TEST_MEDIA_1);
        when(ratingRepo.findByUserIdAndMediaId(Instances.TEST_USER_1.getId(), Instances.TEST_MEDIA_1.getId()))
                .thenReturn(Optional.of(Instances.TEST_RATING_1));

        Exception ex = assertThrows(Exception.class, () ->
                ratingService.createRating(Instances.TEST_USER_1.getId(), Instances.TEST_MEDIA_1.getId(), 5, "Duplicate"));
        assertEquals("User already rated this media", ex.getMessage());
    }

    @Test
    void testUpdateRating_Success() throws Exception {
        mockRating(Instances.TEST_RATING_1.getId(), Instances.TEST_RATING_1);
        mockMedia(Instances.TEST_RATING_1.getMediaId(), Instances.TEST_MEDIA_1);

        Rating updated = ratingService.updateRating(
                Instances.TEST_RATING_1.getId(),
                Instances.TEST_USER_1.getId(),
                4,
                "Good movie"
        );

        assertEquals(4, updated.getStars());
        assertEquals("Good movie", updated.getComment());
        verify(ratingRepo).update(Instances.TEST_RATING_1);
        verify(mediaRepo).update(Instances.TEST_MEDIA_1);
        verify(userService).updateUserStatistics(Instances.TEST_USER_1.getId());
    }

    @Test
    void testUpdateRating_NotAuthorized() throws SQLException {
        mockRating(Instances.TEST_RATING_1.getId(), Instances.TEST_RATING_1);

        Exception ex = assertThrows(Exception.class, () ->
                ratingService.updateRating(Instances.TEST_RATING_1.getId(), Instances.TEST_USER_2.getId(), 5, "Test"));
        assertEquals("Not authorized to update this rating", ex.getMessage());
    }

    @Test
    void testDeleteRating_Success() throws Exception {
        mockRating(Instances.TEST_RATING_1.getId(), Instances.TEST_RATING_1);
        mockMedia(Instances.TEST_RATING_1.getMediaId(), Instances.TEST_MEDIA_1);
        when(ratingRepo.delete(Instances.TEST_RATING_1.getId())).thenReturn(true);

        assertTrue(ratingService.deleteRating(Instances.TEST_RATING_1.getId(), Instances.TEST_USER_1.getId()));
        verify(mediaRepo).update(Instances.TEST_MEDIA_1);
        verify(userService).updateUserStatistics(Instances.TEST_USER_1.getId());
    }

    @Test
    void testDeleteRating_NotAuthorized() throws SQLException {
        mockRating(Instances.TEST_RATING_1.getId(), Instances.TEST_RATING_1);
        Exception ex = assertThrows(Exception.class, () ->
                ratingService.deleteRating(Instances.TEST_RATING_1.getId(), Instances.TEST_USER_2.getId()));
        assertEquals("Not authorized to delete this rating", ex.getMessage());
    }

    @Test
    void testLikeRating() throws Exception {
        mockRating(Instances.TEST_RATING_1.getId(), Instances.TEST_RATING_1);
        int before = Instances.TEST_RATING_1.getLikesCount();

        Rating liked = ratingService.likeRating(Instances.TEST_RATING_1.getId());
        assertEquals(before + 1, liked.getLikesCount());
        verify(ratingRepo).update(Instances.TEST_RATING_1);
    }

    @Test
    void testConfirmRating_Success() throws Exception {
        mockRating(Instances.TEST_RATING_1.getId(), Instances.TEST_RATING_1);
        Rating confirmed = ratingService.confirmRating(Instances.TEST_RATING_1.getId(), Instances.TEST_USER_1.getId());
        assertTrue(confirmed.isConfirmed());
        verify(ratingRepo).update(Instances.TEST_RATING_1);
    }

    @Test
    void testConfirmRating_NotAuthorized() throws SQLException {
        mockRating(Instances.TEST_RATING_1.getId(), Instances.TEST_RATING_1);
        Exception ex = assertThrows(Exception.class, () ->
                ratingService.confirmRating(Instances.TEST_RATING_1.getId(), Instances.TEST_USER_2.getId()));
        assertEquals("Not authorized to confirm this rating", ex.getMessage());
    }

    @Test
    void testGetRatingsByUserId() throws SQLException {
        when(ratingRepo.findByUserId(Instances.TEST_USER_1.getId()))
                .thenReturn(List.of(Instances.TEST_RATING_1));

        List<Rating> ratings = ratingService.getRatingsByUserId(Instances.TEST_USER_1.getId());
        assertEquals(1, ratings.size());
    }

    @Test
    void testGetRatingsByMediaId() throws SQLException {
        when(ratingRepo.findByMediaId(Instances.TEST_MEDIA_1.getId()))
                .thenReturn(List.of(Instances.TEST_RATING_1));

        List<Rating> ratings = ratingService.getRatingsByMediaId(Instances.TEST_MEDIA_1.getId());
        assertEquals(1, ratings.size());
    }
}
