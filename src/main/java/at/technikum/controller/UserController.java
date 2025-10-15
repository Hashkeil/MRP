package at.technikum.controller;

import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import at.technikum.server.http.Status;
import at.technikum.service.UserService;
import at.technikum.service.AuthService;
import at.technikum.model.User;
import at.technikum.util.AuthUtil;
import org.json.JSONObject;

public class UserController {
    private final UserService userService;
    private final AuthService authService;

    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }


     //GET /api/users/{userId}/profile
    public Response getProfile(Request request) {
        try {
            String authHeader = request.getHeader("authorization");
            User authenticatedUser = AuthUtil.requireAuth(authHeader, authService);

            Long userId = Long.parseLong(request.getPathParam("userId"));
            User user = userService.getUserProfile(userId);

            JSONObject response = new JSONObject();
            response.put("id", user.getId());
            response.put("username", user.getUsername());
            response.put("totalRatings", user.getTotalRatings());
            response.put("averageGivenRating", user.getAverageGivenRating());
            response.put("favoriteGenre", user.getFavoriteGenre());

            return new Response(Status.OK.getCode(), response.toString());
        } catch (NumberFormatException e) {
            return new Response(Status.BAD_REQUEST.getCode(),
                    new JSONObject().put("error", "Invalid user ID").toString());
        } catch (Exception e) {
            return new Response(Status.UNAUTHORIZED.getCode(),
                    new JSONObject().put("error", e.getMessage()).toString());
        }
    }


     // PUT /api/users/{userId}/profile
    public Response updateProfile(Request request) {
        try {
            String authHeader = request.getHeader("authorization");
            User authenticatedUser = AuthUtil.requireAuth(authHeader, authService);

            Long userId = Long.parseLong(request.getPathParam("userId"));

            AuthUtil.requireOwnership(authenticatedUser, userId);

            JSONObject json = new JSONObject(request.getBody());
            String genre = json.has("favoriteGenre") ? json.getString("favoriteGenre") : null;

            User user = userService.updateUserProfile(userId, genre);

            JSONObject response = new JSONObject();
            response.put("id", user.getId());
            response.put("username", user.getUsername());
            response.put("favoriteGenre", user.getFavoriteGenre());
            response.put("message", "Profile updated successfully");

            return new Response(Status.OK.getCode(), response.toString());
        } catch (NumberFormatException e) {
            return new Response(Status.BAD_REQUEST.getCode(),
                    new JSONObject().put("error", "Invalid user ID").toString());
        } catch (Exception e) {
            int statusCode = e.getMessage().contains("Not authorized")
                    ? Status.FORBIDDEN.getCode()
                    : Status.UNAUTHORIZED.getCode();
            return new Response(statusCode,
                    new JSONObject().put("error", e.getMessage()).toString());
        }
    }


     //GET /api/users/{userId}/stats
    public Response getRatings(Request request) {
        try {
            String authHeader = request.getHeader("authorization");
            AuthUtil.requireAuth(authHeader, authService);

            Long userId = Long.parseLong(request.getPathParam("userId"));
            User user = userService.getUserProfile(userId);

            JSONObject stats = new JSONObject();
            stats.put("totalRatings", user.getTotalRatings());
            stats.put("averageRating", user.getAverageGivenRating());

            return new Response(Status.OK.getCode(), stats.toString());
        } catch (Exception e) {
            return new Response(Status.UNAUTHORIZED.getCode(),
                    new JSONObject().put("error", e.getMessage()).toString());
        }
    }

     //GET /api/users/{userId}/favorites
    public Response getFavorites(Request request) {
        try {
            String authHeader = request.getHeader("authorization");
            AuthUtil.requireAuth(authHeader, authService);

            Long userId = Long.parseLong(request.getPathParam("userId"));
            User user = userService.getUserProfile(userId);

            JSONObject favorites = new JSONObject();
            favorites.put("favoriteGenre", user.getFavoriteGenre());

            return new Response(Status.OK.getCode(), favorites.toString());
        } catch (Exception e) {
            return new Response(Status.UNAUTHORIZED.getCode(),
                    new JSONObject().put("error", e.getMessage()).toString());
        }
    }
}