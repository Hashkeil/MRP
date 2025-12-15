package service;

import Instances.Instances;
import at.technikum.model.MediaEntry;
import at.technikum.model.enums.AgeRestriction;
import at.technikum.model.enums.MediaType;
import at.technikum.repository.MediaRepository;
import at.technikum.service.MediaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MediaServiceTest {

    private MediaRepository mediaRepository;
    private MediaService mediaService;

    @BeforeEach
    void setUp() {
        mediaRepository = mock(MediaRepository.class);
        mediaService = new MediaService(mediaRepository);
    }

    @Test
    void testCreateMedia() throws SQLException {
        when(mediaRepository.save(any(MediaEntry.class))).thenReturn(Instances.TEST_MEDIA_1);

        MediaEntry media = mediaService.createMedia(
                "Inception 2",
                "Mind-bending sci-fi movie",
                MediaType.MOVIE,
                2010,
                AgeRestriction.AGE_12,
                Instances.TEST_USER_1.getId(),
                List.of("Sci-Fi", "Thriller")
        );

        assertEquals("Inception 2", media.getTitle());
        assertEquals(MediaType.MOVIE, media.getType());
        verify(mediaRepository, times(1)).save(any(MediaEntry.class));
    }

    @Test
    void testGetMediaById_Success() throws Exception {
        when(mediaRepository.findById(100L)).thenReturn(Optional.of(Instances.TEST_MEDIA_1));

        MediaEntry media = mediaService.getMediaById(100L);

        assertEquals("Inception 2", media.getTitle());
        verify(mediaRepository, times(1)).findById(100L);
    }

    @Test
    void testGetMediaById_NotFound() throws SQLException {
        when(mediaRepository.findById(999L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> mediaService.getMediaById(999L));
        assertEquals("Media not found", exception.getMessage());
    }




    @Test
    void testUpdateMedia() throws Exception {
        when(mediaRepository.findById(100L)).thenReturn(Optional.of(Instances.TEST_MEDIA_1));

        MediaEntry updated = mediaService.updateMedia(100L, "Inception 2", null, 2011, List.of("Action"));

        assertEquals("Inception 2", updated.getTitle());
        assertEquals(2011, updated.getReleaseYear());
        assertEquals(List.of("Action"), updated.getGenres());
        verify(mediaRepository, times(1)).update(updated);
    }

    @Test
    void testDeleteMedia_Authorized() throws Exception {
        when(mediaRepository.findById(100L)).thenReturn(Optional.of(Instances.TEST_MEDIA_1));
        when(mediaRepository.delete(100L)).thenReturn(true);

        boolean result = mediaService.deleteMedia(100L, Instances.TEST_USER_1.getId());

        assertTrue(result);
        verify(mediaRepository, times(1)).delete(100L);
    }

    @Test
    void testDeleteMedia_NotAuthorized() throws SQLException {
        when(mediaRepository.findById(100L)).thenReturn(Optional.of(Instances.TEST_MEDIA_1));

        Exception exception = assertThrows(Exception.class,
                () -> mediaService.deleteMedia(100L, Instances.TEST_USER_2.getId()));

        assertEquals("Not authorized to delete this media", exception.getMessage());
    }

    @Test
    void testSearchByTitle() throws SQLException {
        when(mediaRepository.findAll()).thenReturn(List.of(Instances.TEST_MEDIA_1, Instances.TEST_MEDIA_2));

        List<MediaEntry> results = mediaService.searchByTitle("inception");

        assertEquals(1, results.size());
        assertEquals("Inception 2", results.get(0).getTitle());
    }

    @Test
    void testFilterByGenre() throws SQLException {
        when(mediaRepository.findAll()).thenReturn(List.of(Instances.TEST_MEDIA_1, Instances.TEST_MEDIA_2));

        List<MediaEntry> results = mediaService.filterByGenre("Drama");

        assertEquals(1, results.size());
        assertEquals("Breaking Bad", results.get(0).getTitle());
    }



}
