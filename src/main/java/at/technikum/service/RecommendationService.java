package at.technikum.service;

import at.technikum.model.MediaEntry;
import at.technikum.model.Rating;
import at.technikum.model.User;
import at.technikum.repository.MediaRepository;
import at.technikum.repository.RatingRepository;
import at.technikum.repository.UserRepository;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class RecommendationService {

    private final MediaRepository mediaRepository;
    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;

    public RecommendationService(MediaRepository mediaRepository,
                                 RatingRepository ratingRepository,
                                 UserRepository userRepository) {
        this.mediaRepository = mediaRepository;
        this.ratingRepository = ratingRepository;
        this.userRepository = userRepository;
    }

    public List<MediaEntry> getRecommendations(Long userId) throws Exception {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("User not found"));

        // 1️ Get all ratings of the user
        List<Rating> userRatings = ratingRepository.findByUserId(userId);

        // Media already rated by the user
        Set<Long> ratedMediaIds = userRatings.stream()
                .map(Rating::getMediaId)
                .collect(Collectors.toSet());

        // 2️ Derive liked genres from rating history
        Set<String> likedGenres = userRatings.stream()
                .filter(r -> r.getStars() >= 1)
                .map(r -> {
                    try {
                        return mediaRepository.findById(r.getMediaId()).orElse(null);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                })
                .filter(Objects::nonNull)
                .flatMap(m -> m.getGenres().stream())
                .collect(Collectors.toSet());

        // 3️ Get all media
        List<MediaEntry> allMedia = mediaRepository.findAll();

        // 4️ Recommend similar, unrated media
        return allMedia.stream()
                .filter(m -> !ratedMediaIds.contains(m.getId()))
                .filter(m -> {
                    if (!likedGenres.isEmpty()) {
                        return m.getGenres().stream().anyMatch(likedGenres::contains);
                    }
                    return user.getFavoriteGenre() == null
                            || m.getGenres().contains(user.getFavoriteGenre());
                })
                .sorted(Comparator.comparing(MediaEntry::getAverageRating).reversed()
                        .thenComparing(MediaEntry::getTotalRatings, Comparator.reverseOrder()))
                .limit(10)
                .collect(Collectors.toList());
    }
}
