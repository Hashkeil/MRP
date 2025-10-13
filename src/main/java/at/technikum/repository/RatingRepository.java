package at.technikum.repository;

import at.technikum.model.Rating;
import java.util.*;
import java.util.stream.Collectors;

public class RatingRepository {

    private final Map<Long, Rating> ratings = new HashMap<>();
    private final Map<Long, List<Rating>> ratingsByUser = new HashMap<>();
    private final Map<Long, List<Rating>> ratingsByMedia = new HashMap<>();
    private Long nextId = 1L;

    public Rating save(Rating rating) {
        if (rating.getId() == null) {
            rating.setId(nextId++);
        }
        ratings.put(rating.getId(), rating);

        // Index by user
        ratingsByUser.computeIfAbsent(rating.getUserId(), k -> new ArrayList<>()).add(rating);

        // Index by media
        ratingsByMedia.computeIfAbsent(rating.getMediaId(), k -> new ArrayList<>()).add(rating);

        return rating;
    }

    public Optional<Rating> findById(Long id) {
        return Optional.ofNullable(ratings.get(id));
    }

    public List<Rating> findAll() {
        return new ArrayList<>(ratings.values());
    }

    public List<Rating> findByUserId(Long userId) {
        return new ArrayList<>(ratingsByUser.getOrDefault(userId, new ArrayList<>()));
    }

    public List<Rating> findByMediaId(Long mediaId) {
        return new ArrayList<>(ratingsByMedia.getOrDefault(mediaId, new ArrayList<>()));
    }

    public Optional<Rating> findByUserIdAndMediaId(Long userId, Long mediaId) {
        return ratingsByUser.getOrDefault(userId, new ArrayList<>()).stream()
                .filter(r -> r.getMediaId().equals(mediaId))
                .findFirst();
    }

    public List<Rating> findConfirmedByMediaId(Long mediaId) {
        return ratingsByMedia.getOrDefault(mediaId, new ArrayList<>()).stream()
                .filter(Rating::isConfirmed)
                .collect(Collectors.toList());
    }

    public boolean delete(Long id) {
        Rating rating = ratings.remove(id);
        if (rating != null) {
            // Remove from user index
            List<Rating> userRatings = ratingsByUser.get(rating.getUserId());
            if (userRatings != null) {
                userRatings.remove(rating);
            }

            // Remove from media index
            List<Rating> mediaRatings = ratingsByMedia.get(rating.getMediaId());
            if (mediaRatings != null) {
                mediaRatings.remove(rating);
            }
            return true;
        }
        return false;
    }

    public void update(Rating rating) {
        if (rating.getId() != null && ratings.containsKey(rating.getId())) {
            Rating oldRating = ratings.get(rating.getId());

            // Remove old from indexes
            if (!oldRating.getUserId().equals(rating.getUserId())) {
                List<Rating> oldUserRatings = ratingsByUser.get(oldRating.getUserId());
                if (oldUserRatings != null) {
                    oldUserRatings.remove(oldRating);
                }
                ratingsByUser.computeIfAbsent(rating.getUserId(), k -> new ArrayList<>()).add(rating);
            }

            if (!oldRating.getMediaId().equals(rating.getMediaId())) {
                List<Rating> oldMediaRatings = ratingsByMedia.get(oldRating.getMediaId());
                if (oldMediaRatings != null) {
                    oldMediaRatings.remove(oldRating);
                }
                ratingsByMedia.computeIfAbsent(rating.getMediaId(), k -> new ArrayList<>()).add(rating);
            }

            ratings.put(rating.getId(), rating);
        }
    }

    public int countByMediaId(Long mediaId) {
        return ratingsByMedia.getOrDefault(mediaId, new ArrayList<>()).size();
    }

    public int countByUserId(Long userId) {
        return ratingsByUser.getOrDefault(userId, new ArrayList<>()).size();
    }
}