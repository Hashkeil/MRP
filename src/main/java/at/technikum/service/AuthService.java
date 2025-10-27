package at.technikum.service;

import at.technikum.exception.user.UserAlreadyExistsException;
import at.technikum.model.User;
import at.technikum.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.SQLException;
import java.util.Optional;

public class AuthService {
    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public User register(String username, String password) throws SQLException {
        if (userRepository.existsByUsername(username)) {
            throw new UserAlreadyExistsException(username);}
     String hashedPaassword = BCrypt.hashpw(password, BCrypt.gensalt(12));
        User user = new User(username, hashedPaassword);
        return userRepository.save(user);
    }


    public User login(String username, String password) throws Exception {
        Optional<User> userOpt = userRepository.findByUsername(username);

        if (userOpt.isEmpty()) {throw new Exception("Invalid username or password");}

        User user = userOpt.get();
        if (!BCrypt.checkpw(password,user.getPassword())) {throw new Exception("Invalid username or password");}

        String token = generateToken(user.getUsername());
        user.setToken(token);
        userRepository.updateUser(user);

        return user;
    }


    public void logout(String token) throws Exception {
        User user = validateToken(token);
        user.setToken(null);
        userRepository.updateUser(user);
    }


    public User validateToken(String token) throws Exception {
        if (token == null || token.isEmpty()) {throw new Exception("Authentication required");}

        return userRepository.findByToken(token)
                .orElseThrow(() -> new Exception("Invalid or expired token"));
    }


    public String extractToken(String authHeader) throws Exception {
        if (authHeader == null || authHeader.isEmpty()) {
            throw new Exception("Authorization header missing");}

        if (!authHeader.startsWith("Bearer ")) {
            throw new Exception("Invalid authorization format. Expected: Bearer <token>");
        }

        return authHeader.substring(7);
    }




    private String generateToken(String username) {
        return username + "-mrpToken";
    }
}