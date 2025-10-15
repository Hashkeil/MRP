package at.technikum.controller;

import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import at.technikum.server.http.Status;
import at.technikum.service.RecommendationService;
import at.technikum.service.AuthService;
import at.technikum.model.MediaEntry;
import at.technikum.model.User;
import at.technikum.util.AuthUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.List;

public class RecommendationController {
    private final RecommendationService recommendationService;
    private final AuthService authService;

    public RecommendationController(RecommendationService recommendationService, AuthService authService) {
        this.recommendationService = recommendationService;
        this.authService = authService;
    }

     // GET /api/users/{userId}/recommendations
    public Response getRecommendations(Request request) {
        try {
            String authHeader = request.getHeader("authorization");
            User authenticatedUser = AuthUtil.requireAuth(authHeader, authService);

            Long userId = Long.parseLong(request.getPathParam("userId"));

            if (!authenticatedUser.getId().equals(userId)) {
                throw new Exception("Not authorized to view recommendations for this user");
            }

            List<MediaEntry> recommendations = recommendationService.getRecommendations(userId);

            JSONArray jsonArray = new JSONArray();
            for (MediaEntry media : recommendations) {
                JSONObject json = new JSONObject();
                json.put("id", media.getId());
                json.put("title", media.getTitle());
                json.put("description", media.getDescription());
                json.put("type", media.getType().name());
                json.put("releaseYear", media.getReleaseYear());
                json.put("averageRating", media.getAverageRating());
                json.put("totalRatings", media.getTotalRatings());
                json.put("genres", new JSONArray(media.getGenres()));
                jsonArray.put(json);
            }

            return new Response(Status.OK.getCode(), jsonArray.toString());

        } catch (NumberFormatException e) {
            return new Response(Status.BAD_REQUEST.getCode(),
                    new JSONObject().put("error", "Invalid user ID format").toString());
        } catch (Exception e) {
            int statusCode = e.getMessage().contains("Authentication") || e.getMessage().contains("token")
                    ? Status.UNAUTHORIZED.getCode()
                    : e.getMessage().contains("authorized")
                    ? Status.FORBIDDEN.getCode()
                    : Status.NOT_FOUND.getCode();
            return new Response(statusCode,
                    new JSONObject().put("error", e.getMessage()).toString());
        }
    }
}
