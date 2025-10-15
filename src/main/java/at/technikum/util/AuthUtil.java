package at.technikum.util;

import at.technikum.model.User;
import at.technikum.service.AuthService;


public class AuthUtil {


    //  Validates the authorization header and returns the authenticated user
    public static User requireAuth(String authHeader, AuthService authService) throws Exception {
        String token = authService.extractToken(authHeader);
        return authService.validateToken(token);
    }


    //  Checks if the user is the owner of a resource (using User object)
    public static void requireOwnership(User authenticatedUser, Long resourceOwnerId) throws Exception {
        if (!authenticatedUser.getId().equals(resourceOwnerId)) {
            throw new Exception("Not authorized to access this resource");
        }
    }

     // Checks if the user is the owner of a resource (using user ID)
    public static void requireOwnership(Long authenticatedUserId, Long resourceOwnerId) throws Exception {
        if (!authenticatedUserId.equals(resourceOwnerId)) {
            throw new Exception("Not authorized to perform this action");
        }
    }


    public static User tryAuth(String authHeader, AuthService authService) {
        try {
            String token = authService.extractToken(authHeader);
            return authService.validateToken(token);
        } catch (Exception e) {
            return null;
        }
    }
}