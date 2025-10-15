package at.technikum.controller;

import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import at.technikum.server.http.Status;
import at.technikum.service.FavoriteService;
import at.technikum.service.AuthService;
import at.technikum.model.Favorite;
import at.technikum.model.User;
import at.technikum.util.AuthUtil;
import org.json.JSONObject;
import org.json.JSONArray;

import java.util.List;

public class FavoriteController {
    private final FavoriteService favoriteService;
    private final AuthService authService;

    public FavoriteController(FavoriteService favoriteService, AuthService authService) {
        this.favoriteService = favoriteService;
        this.authService = authService;
    }


    // POST /api/media/{mediaId}/favorites
    public Response addFavorite(Request request) {
        try {
            String authHeader = request.getHeader("authorization");
            User authenticatedUser = AuthUtil.requireAuth(authHeader, authService);
            Long mediaId = Long.parseLong(request.getPathParam("mediaId"));
            Favorite favorite = favoriteService.addFavorite(authenticatedUser.getId(), mediaId);

            JSONObject response = new JSONObject();
            response.put("id", favorite.getId());
            response.put("userId", favorite.getUserId());
            response.put("mediaId", favorite.getMediaId());
            response.put("dateAdded", favorite.getDateAdded().toString());
            response.put("message", "Added to favorites");

            return new Response(Status.CREATED.getCode(), response.toString());
        } catch (NumberFormatException e) {
            return new Response(Status.BAD_REQUEST.getCode(),
                    new JSONObject().put("error", "Invalid media ID").toString());
        } catch (Exception e) {
            return new Response(Status.BAD_REQUEST.getCode(),
                    new JSONObject().put("error", e.getMessage()).toString());
        }
    }


    // DELETE /api/media/{mediaId}/favorites
    public Response removeFavorite(Request request) {
        try {
            String authHeader = request.getHeader("authorization");
            User authenticatedUser = AuthUtil.requireAuth(authHeader, authService);
            Long mediaId = Long.parseLong(request.getPathParam("mediaId"));
            favoriteService.removeFavorite(authenticatedUser.getId(), mediaId);

            return new Response(Status.OK.getCode(),
                    new JSONObject().put("message", "Removed from favorites").toString());
        } catch (Exception e) {
            return new Response(Status.BAD_REQUEST.getCode(),
                    new JSONObject().put("error", e.getMessage()).toString());
        }
    }


      // GET /api/users/{userId}/favorites
    public Response getUserFavorites(Request request) {
        try {
            String authHeader = request.getHeader("authorization");
            AuthUtil.requireAuth(authHeader, authService);
            Long userId = Long.parseLong(request.getPathParam("userId"));
            List<Favorite> favorites = favoriteService.getUserFavorites(userId);

            JSONArray jsonArray = new JSONArray();
            for (Favorite favorite : favorites) {
                JSONObject json = new JSONObject();
                json.put("id", favorite.getId());
                json.put("mediaId", favorite.getMediaId());
                json.put("dateAdded", favorite.getDateAdded().toString());
                jsonArray.put(json);
            }

            return new Response(Status.OK.getCode(), jsonArray.toString());
        } catch (Exception e) {
            return new Response(Status.UNAUTHORIZED.getCode(),
                    new JSONObject().put("error", e.getMessage()).toString());
        }
    }
}
