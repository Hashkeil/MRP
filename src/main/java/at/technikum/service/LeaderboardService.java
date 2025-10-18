package at.technikum.service;

import at.technikum.model.User;
import at.technikum.repository.RatingRepository;
import at.technikum.repository.UserRepository;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class LeaderboardService {
    private final UserRepository userRepository;
    private final RatingRepository ratingRepository;

    public LeaderboardService(UserRepository userRepository, RatingRepository ratingRepository) {
        this.userRepository = userRepository;
        this.ratingRepository = ratingRepository;
    }

    public List<Map<String, Object>> getLeaderboard() throws SQLException {
        List<User> allUsers = userRepository.findAll();

        return allUsers.stream()
                .map(user -> {
                    int ratingCount = ratingRepository.countByUserId(user.getId());
                    Map<String, Object> entry = new HashMap<>();
                    entry.put("userId", user.getId());
                    entry.put("username", user.getUsername());
                    entry.put("totalRatings", ratingCount);
                    return entry;
                })
                .sorted(Comparator.comparingInt((Map<String, Object> e) ->
                        (Integer) e.get("totalRatings")).reversed())
                .limit(20)
                .collect(Collectors.toList());
    }
}