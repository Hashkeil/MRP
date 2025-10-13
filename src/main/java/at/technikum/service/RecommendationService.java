package at.technikum.service;

import at.technikum.model.MediaEntry;
import at.technikum.model.Rating;
import at.technikum.model.User;
import at.technikum.repository.MediaRepository;
import at.technikum.repository.RatingRepository;
import at.technikum.repository.UserRepository;
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

        List<Rating> userRatings = ratingRepository.findByUserId(userId);
        Set<Long> ratedMediaIds = userRatings.stream()
                .map(Rating::getMediaId)
                .collect(Collectors.toSet());

        List<MediaEntry> allMedia = mediaRepository.findAll();

        String favoriteGenre = user.getFavoriteGenre();

        return allMedia.stream()
                .filter(m -> !ratedMediaIds.contains(m.getId()))
                .filter(m -> favoriteGenre == null || m.getGenres().contains(favoriteGenre))
                .sorted(Comparator.comparing(MediaEntry::getAverageRating).reversed()
                        .thenComparing(MediaEntry::getTotalRatings, Comparator.reverseOrder()))
                .limit(10)
                .collect(Collectors.toList());
    }
}