package at.technikum.util;

import at.technikum.model.User;
import at.technikum.service.AuthService;


public class AuthUtil {


    public static User requireAuth(String authHeader, AuthService authService) throws Exception {
        String token = authService.extractToken(authHeader);
        return authService.validateToken(token);
    }


    public static void requireOwnership(User authenticatedUser, Long resourceOwnerId) throws Exception {
        if (!authenticatedUser.getId().equals(resourceOwnerId)) {
            throw new Exception("Not authorized to access this resource");
        }
    }




}