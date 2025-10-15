package at.technikum.repository;

import at.technikum.model.Rating;
import java.util.*;

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
        ratingsByUser.computeIfAbsent(rating.getUserId(), k -> new ArrayList<>()).add(rating);
        ratingsByMedia.computeIfAbsent(rating.getMediaId(), k -> new ArrayList<>()).add(rating);

        return rating;
    }

    public Optional<Rating> findById(Long id) {
        return Optional.ofNullable(ratings.get(id));
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

    public boolean delete(Long id) {
        Rating rating = ratings.remove(id);
        if (rating != null) {
            List<Rating> userRatings = ratingsByUser.get(rating.getUserId());
            if (userRatings != null) {
                userRatings.remove(rating);
            }

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

    public int countByUserId(Long userId) {
        return ratingsByUser.getOrDefault(userId, new ArrayList<>()).size();
    }
}