package at.technikum.controller;

import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import at.technikum.server.http.Status;
import at.technikum.service.RatingService;
import at.technikum.model.Rating;
import org.json.JSONObject;

public class RatingController {
    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    public Response rateMedia(Request request) {
        try {
            Long mediaId = Long.parseLong(request.getPathParam("mediaId"));
            JSONObject json = new JSONObject(request.getBody());

            Long userId = json.getLong("userId");
            Integer stars = json.getInt("stars");
            String comment = json.optString("comment", null);

            Rating rating = ratingService.createRating(userId, mediaId, stars, comment);
            JSONObject response = buildRatingJson(rating);
            return new Response(Status.CREATED.getCode(), response.toString());
        } catch (Exception e) {
            return new Response(Status.BAD_REQUEST.getCode(),
                    new JSONObject().put("error", e.getMessage()).toString());
        }
    }

    public Response updateRating(Request request) {
        try {
            Long ratingId = Long.parseLong(request.getPathParam("ratingId"));
            JSONObject json = new JSONObject(request.getBody());

            Long userId = json.getLong("userId");
            Integer stars = json.getInt("stars");
            String comment = json.optString("comment", null);

            Rating rating = ratingService.updateRating(ratingId, userId, stars, comment);
            JSONObject response = buildRatingJson(rating);
            return new Response(Status.OK.getCode(), response.toString());
        } catch (Exception e) {
            return new Response(Status.BAD_REQUEST.getCode(),
                    new JSONObject().put("error", e.getMessage()).toString());
        }
    }

    public Response deleteRating(Request request) {
        try {
            Long ratingId = Long.parseLong(request.getPathParam("ratingId"));
            Long userId = Long.parseLong(request.getPathParam("userId"));

            ratingService.deleteRating(ratingId, userId);
            return new Response(Status.OK.getCode(),
                    new JSONObject().put("message", "Rating deleted").toString());
        } catch (Exception e) {
            return new Response(Status.BAD_REQUEST.getCode(),
                    new JSONObject().put("error", e.getMessage()).toString());
        }
    }

    public Response likeRating(Request request) {
        try {
            Long ratingId = Long.parseLong(request.getPathParam("ratingId"));
            Rating rating = ratingService.likeRating(ratingId);
            JSONObject response = buildRatingJson(rating);
            return new Response(Status.OK.getCode(), response.toString());
        } catch (Exception e) {
            return new Response(Status.NOT_FOUND.getCode(),
                    new JSONObject().put("error", e.getMessage()).toString());
        }
    }

    public Response confirmRating(Request request) {
        try {
            Long ratingId = Long.parseLong(request.getPathParam("ratingId"));
            Rating rating = ratingService.confirmRating(ratingId);
            JSONObject response = buildRatingJson(rating);
            return new Response(Status.OK.getCode(), response.toString());
        } catch (Exception e) {
            return new Response(Status.NOT_FOUND.getCode(),
                    new JSONObject().put("error", e.getMessage()).toString());
        }
    }

    private JSONObject buildRatingJson(Rating rating) {
        JSONObject json = new JSONObject();
        json.put("id", rating.getId());
        json.put("userId", rating.getUserId());
        json.put("mediaId", rating.getMediaId());
        json.put("stars", rating.getStars());
        json.put("comment", rating.getComment());
        json.put("timestamp", rating.getTimestamp().toString());
        json.put("confirmed", rating.isConfirmed());
        json.put("likesCount", rating.getLikesCount());
        return json;
    }
}