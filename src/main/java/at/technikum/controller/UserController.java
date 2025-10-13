package at.technikum.controller;

import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import at.technikum.server.http.Status;
import at.technikum.service.UserService;
import at.technikum.model.User;
import org.json.JSONObject;

@SuppressWarnings("checkstyle:Indentation")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    public Response register(Request request) {
        try {
            JSONObject json = new JSONObject(request.getBody());
            String username = json.getString("username");
            String password = json.getString("password");

            User user = userService.registerUser(username, password);

            JSONObject response = new JSONObject(user); // Converts user to JSON
            return new Response(Status.CREATED.getCode(), response.toString());
        } catch (Exception e) {
            return new Response(Status.BAD_REQUEST.getCode(), new JSONObject().put("error", e.getMessage()).toString());
        }
    }

    public Response login(Request request) {
        try {
            JSONObject json = new JSONObject(request.getBody());
            String username = json.getString("username");
            String password = json.getString("password");

            User user = userService.loginUser(username, password);

            JSONObject response = new JSONObject(user);
            return new Response(Status.OK.getCode(), response.toString());
        } catch (Exception e) {
            return new Response(Status.UNAUTHORIZED.getCode(), new JSONObject().put("error", e.getMessage()).toString());
        }
    }

    public Response getProfile(Request request) {
        try {
            Long userId = Long.parseLong(request.getPathParam("userId"));
            User user = userService.getUserProfile(userId);

            JSONObject response = new JSONObject(user);
            return new Response(Status.OK.getCode(), response.toString());
        } catch (Exception e) {
            return new Response(Status.NOT_FOUND.getCode(), new JSONObject().put("error", "User not found").toString());
        }
    }

    public Response updateProfile(Request request) {
        try {
            Long userId = Long.parseLong(request.getPathParam("userId"));
            JSONObject json = new JSONObject(request.getBody());
            String genre = json.has("favoriteGenre") ? json.getString("favoriteGenre") : null;

            User user = userService.updateUserProfile(userId, genre);
            JSONObject response = new JSONObject(user);
            return new Response(Status.OK.getCode(), response.toString());
        } catch (Exception e) {
            return new Response(Status.BAD_REQUEST.getCode(), new JSONObject().put("error", e.getMessage()).toString());
        }
    }

    public Response getRatings(Request request) {
        try {
            Long userId = Long.parseLong(request.getPathParam("userId"));
            User user = userService.getUserProfile(userId);

            JSONObject stats = new JSONObject();
            stats.put("totalRatings", user.getTotalRatings());
            stats.put("averageRating", user.getAverageGivenRating());

            return new Response(Status.OK.getCode(), stats.toString());
        } catch (Exception e) {
            return new Response(Status.NOT_FOUND.getCode(), new JSONObject().put("error", "User not found").toString());
        }
    }

    public Response getFavorites(Request request) {
        try {
            Long userId = Long.parseLong(request.getPathParam("userId"));
            User user = userService.getUserProfile(userId);

            JSONObject favorites = new JSONObject();
            favorites.put("favoriteGenre", user.getFavoriteGenre());

            return new Response(Status.OK.getCode(), favorites.toString());
        } catch (Exception e) {
            return new Response(Status.NOT_FOUND.getCode(), new JSONObject().put("error", "User not found").toString());
        }
    }
}
