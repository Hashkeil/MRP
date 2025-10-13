package at.technikum.controller;

import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import at.technikum.server.http.Status;
import at.technikum.service.RecommendationService;
import at.technikum.model.MediaEntry;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.List;

public class RecommendationController {
    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    public Response getRecommendations(Request request) {
        try {
            Long userId = Long.parseLong(request.getPathParam("userId"));
            List<MediaEntry> recommendations = recommendationService.getRecommendations(userId);

            JSONArray jsonArray = new JSONArray();
            for (MediaEntry media : recommendations) {
                JSONObject json = new JSONObject();
                json.put("id", media.getId());
                json.put("title", media.getTitle());
                json.put("description", media.getDescription());
                json.put("type", media.getType().name());
                json.put("averageRating", media.getAverageRating());
                json.put("genres", new JSONArray(media.getGenres()));
                jsonArray.put(json);
            }

            return new Response(Status.OK.getCode(), jsonArray.toString());
        } catch (Exception e) {
            return new Response(Status.NOT_FOUND.getCode(),
                    new JSONObject().put("error", e.getMessage()).toString());
        }
    }
}