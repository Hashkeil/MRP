package at.technikum.service;

import at.technikum.exception.user.UserNotFoundException;
import at.technikum.exception.user.usernameNotFoundException;
import at.technikum.model.User;
import at.technikum.repository.UserRepository;
import at.technikum.repository.RatingRepository;
import java.util.List;

public class UserService {

    private final UserRepository userRepository;
    private final RatingRepository ratingRepository;

    public UserService(UserRepository userRepository, RatingRepository ratingRepository) {
        this.userRepository = userRepository;
        this.ratingRepository = ratingRepository;
    }


    public User getUserProfile(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }


    public User getUserProfileByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new usernameNotFoundException(username));
    }

    public User updateUserProfile(Long userId, String favoriteGenre) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("User not found"));

        if (favoriteGenre != null) {user.setFavoriteGenre(favoriteGenre);}

        userRepository.updateUser(user);
        return user;
    }


    public void updateUserStatistics(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return;

        List<at.technikum.model.Rating> userRatings = ratingRepository.findByUserId(userId);

        user.setTotalRatings(userRatings.size());

        if (!userRatings.isEmpty()) {
            double avgRating = userRatings.stream()
                    .mapToInt(at.technikum.model.Rating::getStars)
                    .average()
                    .orElse(0.0);
            user.setAverageGivenRating(avgRating);
        } else {
            user.setAverageGivenRating(0.0);
        }

        userRepository.updateUser(user);
    }


    public List<User> getAllUsers() {return userRepository.findAll();}

    public User getUserByToken(String token) throws Exception {
        return userRepository.findByToken(token)
                .orElseThrow(() -> new Exception("Invalid token"));
    }
}