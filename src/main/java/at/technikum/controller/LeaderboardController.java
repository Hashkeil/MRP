package at.technikum.controller;

import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import at.technikum.server.http.Status;
import at.technikum.service.LeaderboardService;
import at.technikum.service.AuthService;
import at.technikum.util.AuthUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class LeaderboardController {
    private final LeaderboardService leaderboardService;
    private final AuthService authService;

    public LeaderboardController(LeaderboardService leaderboardService, AuthService authService) {
        this.leaderboardService = leaderboardService;
        this.authService = authService;
    }


    //  GET /api/leaderboard
    public Response getLeaderboard(Request request) {
        try {
            String authHeader = request.getHeader("authorization");
            AuthUtil.requireAuth(authHeader, authService);

            List<Map<String, Object>> leaderboard = leaderboardService.getLeaderboard();

            JSONArray jsonArray = new JSONArray();
            int rank = 1;
            for (Map<String, Object> entry : leaderboard) {
                JSONObject json = new JSONObject();
                json.put("rank", rank++);
                json.put("userId", entry.get("userId"));
                json.put("username", entry.get("username"));
                json.put("totalRatings", entry.get("totalRatings"));
                jsonArray.put(json);
            }

            return new Response(Status.OK.getCode(), jsonArray.toString());
        } catch (Exception e) {
            return new Response(Status.UNAUTHORIZED.getCode(),
                    new JSONObject().put("error", e.getMessage()).toString());
        }
    }
}
