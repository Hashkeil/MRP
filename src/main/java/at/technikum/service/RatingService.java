package at.technikum.service;

import at.technikum.model.Rating;
import at.technikum.model.MediaEntry;
import at.technikum.repository.RatingRepository;
import at.technikum.repository.MediaRepository;
import java.util.List;

public class RatingService {
    private final RatingRepository ratingRepository;
    private final MediaRepository mediaRepository;

    public RatingService(RatingRepository ratingRepository, MediaRepository mediaRepository) {
        this.ratingRepository = ratingRepository;
        this.mediaRepository = mediaRepository;
    }

    public Rating createRating(Long userId, Long mediaId, Integer stars, String comment) throws Exception {
        MediaEntry media = mediaRepository.findById(mediaId)
                .orElseThrow(() -> new Exception("Media not found"));

        if (ratingRepository.findByUserIdAndMediaId(userId, mediaId).isPresent()) {
            throw new Exception("User already rated this media");
        }

        Rating rating = new Rating(userId, mediaId, stars, comment);
        rating = ratingRepository.save(rating);
        updateMediaRating(mediaId);
        return rating;
    }

    public Rating updateRating(Long ratingId, Long userId, Integer stars, String comment) throws Exception {
        Rating rating = ratingRepository.findById(ratingId)
                .orElseThrow(() -> new Exception("Rating not found"));

        if (!rating.isOwnedBy(userId)) {
            throw new Exception("Not authorized to update this rating");
        }

        rating.updateRating(stars, comment);
        ratingRepository.update(rating);
        updateMediaRating(rating.getMediaId());
        return rating;
    }

    public boolean deleteRating(Long ratingId, Long userId) throws Exception {
        Rating rating = ratingRepository.findById(ratingId)
                .orElseThrow(() -> new Exception("Rating not found"));

        if (!rating.isOwnedBy(userId)) {
            throw new Exception("Not authorized to delete this rating");
        }

        Long mediaId = rating.getMediaId();
        boolean deleted = ratingRepository.delete(ratingId);
        if (deleted) {
            updateMediaRating(mediaId);
        }
        return deleted;
    }

    public Rating likeRating(Long ratingId) throws Exception {
        Rating rating = ratingRepository.findById(ratingId)
                .orElseThrow(() -> new Exception("Rating not found"));

        rating.incrementLikes();
        ratingRepository.update(rating);
        return rating;
    }

    public Rating confirmRating(Long ratingId) throws Exception {
        Rating rating = ratingRepository.findById(ratingId)
                .orElseThrow(() -> new Exception("Rating not found"));

        rating.confirmRating();
        ratingRepository.update(rating);
        return rating;
    }

    public List<Rating> getRatingsByUserId(Long userId) {
        return ratingRepository.findByUserId(userId);
    }

    public List<Rating> getRatingsByMediaId(Long mediaId) {
        return ratingRepository.findByMediaId(mediaId);
    }

    private void updateMediaRating(Long mediaId) throws Exception {
        MediaEntry media = mediaRepository.findById(mediaId)
                .orElseThrow(() -> new Exception("Media not found"));

        List<Rating> ratings = ratingRepository.findByMediaId(mediaId);
        if (ratings.isEmpty()) {
            media.updateAverageRating(0.0, 0);
        } else {
            double avg = ratings.stream().mapToInt(Rating::getStars).average().orElse(0.0);
            media.updateAverageRating(avg, ratings.size());
        }
        mediaRepository.update(media);
    }
}