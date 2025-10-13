package at.technikum.controller;

import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import at.technikum.server.http.Status;
import at.technikum.service.FavoriteService;
import at.technikum.model.Favorite;
import org.json.JSONObject;

public class FavoriteController {
    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    public Response addFavorite(Request request) {
        try {
            Long mediaId = Long.parseLong(request.getPathParam("mediaId"));
            JSONObject json = new JSONObject(request.getBody());
            Long userId = json.getLong("userId");

            Favorite favorite = favoriteService.addFavorite(userId, mediaId);

            JSONObject response = new JSONObject();
            response.put("id", favorite.getId());
            response.put("userId", favorite.getUserId());
            response.put("mediaId", favorite.getMediaId());
            response.put("dateAdded", favorite.getDateAdded().toString());

            return new Response(Status.CREATED.getCode(), response.toString());
        } catch (Exception e) {
            return new Response(Status.BAD_REQUEST.getCode(),
                    new JSONObject().put("error", e.getMessage()).toString());
        }
    }

    public Response removeFavorite(Request request) {
        try {
            Long mediaId = Long.parseLong(request.getPathParam("mediaId"));
            Long userId = Long.parseLong(request.getPathParam("userId"));

            favoriteService.removeFavorite(userId, mediaId);
            return new Response(Status.OK.getCode(),
                    new JSONObject().put("message", "Favorite removed").toString());
        } catch (Exception e) {
            return new Response(Status.BAD_REQUEST.getCode(),
                    new JSONObject().put("error", e.getMessage()).toString());
        }
    }
}