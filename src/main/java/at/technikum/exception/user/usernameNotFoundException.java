package at.technikum.exception.user;

public class usernameNotFoundException extends RuntimeException {
    public usernameNotFoundException(String username) {
        super("User not found: " + username);
    }}


