package at.technikum.controller;

import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import at.technikum.server.http.Status;
import at.technikum.service.RatingService;
import at.technikum.service.AuthService;
import at.technikum.model.Rating;
import at.technikum.model.User;
import at.technikum.util.AuthUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class RatingController {
    private final RatingService ratingService;
    private final AuthService authService;

    public RatingController(RatingService ratingService, AuthService authService) {
        this.ratingService = ratingService;
        this.authService = authService;
    }


     // POST /api/media/{mediaId}/ratings
    public Response rateMedia(Request request) {
        try {
            String authHeader = request.getHeader("authorization");
            User authenticatedUser = AuthUtil.requireAuth(authHeader, authService);

            Long mediaId = Long.parseLong(request.getPathParam("mediaId"));
            JSONObject json = new JSONObject(request.getBody());

            Integer stars = json.getInt("stars");
            String comment = json.optString("comment", null);

            Rating rating = ratingService.createRating(authenticatedUser.getId(), mediaId, stars, comment);
            JSONObject response = buildRatingJson(rating);
            return new Response(Status.CREATED.getCode(), response.toString());
        } catch (NumberFormatException e) {
            return new Response(Status.BAD_REQUEST.getCode(),
                    new JSONObject().put("error", "Invalid media ID format").toString());
        } catch (Exception e) {
            return new Response(Status.BAD_REQUEST.getCode(),
                    new JSONObject().put("error", e.getMessage()).toString());
        }
    }

     // PUT /api/ratings/{ratingId}
    public Response updateRating(Request request) {
        try {
            String authHeader = request.getHeader("authorization");
            User authenticatedUser = AuthUtil.requireAuth(authHeader, authService);

            Long ratingId = Long.parseLong(request.getPathParam("ratingId"));
            JSONObject json = new JSONObject(request.getBody());

            Integer stars = json.getInt("stars");
            String comment = json.optString("comment", null);

            Rating rating = ratingService.updateRating(ratingId, authenticatedUser.getId(), stars, comment);
            JSONObject response = buildRatingJson(rating);
            return new Response(Status.OK.getCode(), response.toString());
        } catch (NumberFormatException e) {
            return new Response(Status.BAD_REQUEST.getCode(),
                    new JSONObject().put("error", "Invalid rating ID format").toString());
        } catch (Exception e) {
            return new Response(Status.BAD_REQUEST.getCode(),
                    new JSONObject().put("error", e.getMessage()).toString());
        }
    }

     // DELETE /api/ratings/{ratingId}
    public Response deleteRating(Request request) {
        try {
            String authHeader = request.getHeader("authorization");
            User authenticatedUser = AuthUtil.requireAuth(authHeader, authService);

            Long ratingId = Long.parseLong(request.getPathParam("ratingId"));

            ratingService.deleteRating(ratingId, authenticatedUser.getId());
            return new Response(Status.OK.getCode(),
                    new JSONObject().put("message", "Rating deleted successfully").toString());
        } catch (NumberFormatException e) {
            return new Response(Status.BAD_REQUEST.getCode(),
                    new JSONObject().put("error", "Invalid rating ID format").toString());
        } catch (Exception e) {
            return new Response(Status.BAD_REQUEST.getCode(),
                    new JSONObject().put("error", e.getMessage()).toString());
        }
    }


     // POST /api/ratings/{ratingId}/like
    public Response likeRating(Request request) {
        try {
            String authHeader = request.getHeader("authorization");
            AuthUtil.requireAuth(authHeader, authService);

            Long ratingId = Long.parseLong(request.getPathParam("ratingId"));
            Rating rating = ratingService.likeRating(ratingId);
            JSONObject response = buildRatingJson(rating);
            return new Response(Status.OK.getCode(), response.toString());
        } catch (NumberFormatException e) {
            return new Response(Status.BAD_REQUEST.getCode(),
                    new JSONObject().put("error", "Invalid rating ID format").toString());
        } catch (Exception e) {
            return new Response(Status.NOT_FOUND.getCode(),
                    new JSONObject().put("error", e.getMessage()).toString());
        }
    }

      // POST /api/ratings/{ratingId}/confirm
    public Response confirmRating(Request request) {
        try {
            String authHeader = request.getHeader("authorization");
            User authenticatedUser = AuthUtil.requireAuth(authHeader, authService);

            Long ratingId = Long.parseLong(request.getPathParam("ratingId"));
            Rating rating = ratingService.confirmRating(ratingId, authenticatedUser.getId());
            JSONObject response = buildRatingJson(rating);
            return new Response(Status.OK.getCode(), response.toString());

        } catch (NumberFormatException e) {
            return new Response(Status.BAD_REQUEST.getCode(),
                    new JSONObject().put("error", "Invalid rating ID format").toString());
        } catch (Exception e) {
            return new Response(Status.FORBIDDEN.getCode(),
                    new JSONObject().put("error", e.getMessage()).toString());
        }
    }

     // GET /api/media/{mediaId}/ratings
    public Response getMediaRatings(Request request) {
        try {
            String authHeader = request.getHeader("authorization");
            AuthUtil.requireAuth(authHeader, authService);

            Long mediaId = Long.parseLong(request.getPathParam("mediaId"));
            List<Rating> ratings = ratingService.getRatingsByMediaId(mediaId);

            JSONArray jsonArray = new JSONArray();
            for (Rating rating : ratings) {
                if (rating.isConfirmed()) {
                    jsonArray.put(buildRatingJson(rating));
                }
            }

            return new Response(Status.OK.getCode(), jsonArray.toString());
        } catch (NumberFormatException e) {
            return new Response(Status.BAD_REQUEST.getCode(),
                    new JSONObject().put("error", "Invalid media ID format").toString());
        } catch (Exception e) {
            return new Response(Status.UNAUTHORIZED.getCode(),
                    new JSONObject().put("error", e.getMessage()).toString());
        }
    }


     // GET /api/users/{userId}/ratings
    public Response getUserRatings(Request request) {
        try {
            String authHeader = request.getHeader("authorization");
            AuthUtil.requireAuth(authHeader, authService);

            Long userId = Long.parseLong(request.getPathParam("userId"));
            List<Rating> ratings = ratingService.getRatingsByUserId(userId);

            JSONArray jsonArray = new JSONArray();
            for (Rating rating : ratings) {
                jsonArray.put(buildRatingJson(rating));
            }

            return new Response(Status.OK.getCode(), jsonArray.toString());
        } catch (NumberFormatException e) {
            return new Response(Status.BAD_REQUEST.getCode(),
                    new JSONObject().put("error", "Invalid user ID format").toString());
        } catch (Exception e) {
            return new Response(Status.UNAUTHORIZED.getCode(),
                    new JSONObject().put("error", e.getMessage()).toString());
        }
    }


      // Helper method to build JSON response for a rating
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