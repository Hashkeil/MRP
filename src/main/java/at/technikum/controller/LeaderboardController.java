package at.technikum.controller;

import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import at.technikum.server.http.Status;
import at.technikum.service.LeaderboardService;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.List;
import java.util.Map;

public class LeaderboardController {
    private final LeaderboardService leaderboardService;

    public LeaderboardController(LeaderboardService leaderboardService) {
        this.leaderboardService = leaderboardService;
    }

    public Response getLeaderboard(Request request) {
        try {
            List<Map<String, Object>> leaderboard = leaderboardService.getLeaderboard();
            JSONArray jsonArray = new JSONArray();

            for (Map<String, Object> entry : leaderboard) {
                JSONObject json = new JSONObject();
                json.put("userId", entry.get("userId"));
                json.put("username", entry.get("username"));
                json.put("totalRatings", entry.get("totalRatings"));
                jsonArray.put(json);
            }

            return new Response(Status.OK.getCode(), jsonArray.toString());
        } catch (Exception e) {
            return new Response(Status.INTERNAL_SERVER_ERROR.getCode(),
                    new JSONObject().put("error", e.getMessage()).toString());
        }
    }
}