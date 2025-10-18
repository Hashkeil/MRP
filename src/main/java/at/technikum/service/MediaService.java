package at.technikum.service;

import at.technikum.model.MediaEntry;
import at.technikum.model.enums.AgeRestriction;
import at.technikum.model.enums.MediaType;
import at.technikum.repository.MediaRepository;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MediaService {

    private final MediaRepository mediaRepository;

    public MediaService(MediaRepository mediaRepository) {
        this.mediaRepository = mediaRepository;
    }

    public MediaEntry createMedia(String title, String description, MediaType type,
                                  Integer releaseYear, AgeRestriction ageRestriction,
                                  Long creatorId, List<String> genres) throws SQLException {
        MediaEntry media = new MediaEntry(title, description, type, releaseYear,
                ageRestriction, creatorId);
        if (genres != null) {
            media.setGenres(genres);
        }
        return mediaRepository.save(media);
    }

    public MediaEntry getMediaById(Long id) throws Exception {
        return mediaRepository.findById(id)
                .orElseThrow(() -> new Exception("Media not found"));
    }

    public List<MediaEntry> getAllMedia() throws SQLException {
        return mediaRepository.findAll();
    }

    public List<MediaEntry> getMediaByCreator(Long creatorId) throws SQLException {
        return mediaRepository.findByCreatorId(creatorId);
    }

    public MediaEntry updateMedia(Long id, String title, String description,
                                  Integer releaseYear, List<String> genres) throws Exception {
        MediaEntry media = getMediaById(id);

        if (title != null) media.setTitle(title);
        if (description != null) media.setDescription(description);
        if (releaseYear != null) media.setReleaseYear(releaseYear);
        if (genres != null) media.setGenres(genres);

        mediaRepository.update(media);
        return media;
    }

    public boolean deleteMedia(Long id, Long userId) throws Exception {
        MediaEntry media = getMediaById(id);
        if (!media.isCreatedBy(userId)) {
            throw new Exception("Not authorized to delete this media");
        }
        return mediaRepository.delete(id);
    }



    public List<MediaEntry> searchByTitle(String query) throws SQLException {
        return mediaRepository.findAll().stream()
                .filter(m -> m.getTitle().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<MediaEntry> filterByGenre(String genre) throws SQLException {
        return mediaRepository.findAll().stream()
                .filter(m -> m.getGenres().contains(genre))
                .collect(Collectors.toList());
    }

    public List<MediaEntry> filterByType(MediaType type) throws SQLException {
        return mediaRepository.findAll().stream()
                .filter(m -> m.getType() == type)
                .collect(Collectors.toList());
    }

    public List<MediaEntry> filterByYear(Integer year) throws SQLException {
        return mediaRepository.findAll().stream()
                .filter(m -> m.getReleaseYear().equals(year))
                .collect(Collectors.toList());
    }

    public List<MediaEntry> filterByAgeRestriction(AgeRestriction restriction) throws SQLException {
        return mediaRepository.findAll().stream()
                .filter(m -> m.getAgeRestriction() == restriction)
                .collect(Collectors.toList());
    }

    public List<MediaEntry> sortMedia(List<MediaEntry> media, String sortBy) {
        switch(sortBy) {
            case "title":
                return media.stream()
                        .sorted(Comparator.comparing(MediaEntry::getTitle))
                        .collect(Collectors.toList());
            case "year":
                return media.stream()
                        .sorted(Comparator.comparing(MediaEntry::getReleaseYear).reversed())
                        .collect(Collectors.toList());
            case "rating":
                return media.stream()
                        .sorted(Comparator.comparing(MediaEntry::getAverageRating).reversed())
                        .collect(Collectors.toList());
            default:
                return media;
        }
    }
}