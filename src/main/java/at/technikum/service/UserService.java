package at.technikum.service;

import at.technikum.exception.user.UserAlreadyExistsException;
import at.technikum.exception.user.UserNotFoundException;
import at.technikum.exception.user.usernameNotFoundException;
import at.technikum.model.User;
import at.technikum.repository.UserRepository;
import java.util.List;
import java.util.Optional;


public class UserService {

    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public User registerUser(String username, String password){
        if (userRepository.existsByUsername(username)) {
            throw new UserAlreadyExistsException(username);}

        User user = new User (username, password);
        return userRepository.save (user);
    }



    public User loginUser(String username, String password) throws Exception {
        Optional<User> userOpt = userRepository.findByUsername(username);

        if (userOpt.isEmpty()) {throw new Exception("User not found");}

        User user = userOpt.get();
        if (! user.getPassword().equals(password)) {throw new Exception("Invalid password");}

        String token = generateToken(user.getUsername());
        user.setToken(token);
        userRepository.updateUser(user);

        return user;
    }



    public User getUserProfile(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }


    public User getUserProfileByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new usernameNotFoundException(username));
    }


    public User updateUserProfile(Long userId,  String favoriteGenre) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("User not found"));
        if (favoriteGenre != null) user.setFavoriteGenre(favoriteGenre);
        userRepository.updateUser(user);
        return user;
    }



    public User getUserByToken(String token) throws Exception {
        return userRepository.findByToken(token)
                .orElseThrow(() -> new Exception("Invalid token"));
    }



    public List<User> getAllUsers() {
        return userRepository.findAll();
    }



    public void logoutUser(String token) throws Exception {
        User user = getUserByToken(token);
        user.setToken(null);
        userRepository.updateUser(user);
    }



    private String generateToken(String username) {
        return username + "-mrpToken";
    }

}