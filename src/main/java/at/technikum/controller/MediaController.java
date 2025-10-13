package at.technikum.controller;

import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import at.technikum.server.http.Status;
import at.technikum.service.MediaService;
import at.technikum.model.MediaEntry;
import at.technikum.model.enums.AgeRestriction;
import at.technikum.model.enums.MediaType;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MediaController {
    private final MediaService mediaService;

    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    public Response createMedia(Request request) {
        try {
            JSONObject json = new JSONObject(request.getBody());

            String title = json.getString("title");
            String description = json.getString("description");
            MediaType type = MediaType.valueOf(json.getString("type"));
            int releaseYear = json.getInt("releaseYear");
            AgeRestriction ageRestriction = AgeRestriction.valueOf(json.getString("ageRestriction"));
            long creatorId = json.getLong("creatorId");

            List<String> genres = new ArrayList<>();
            if (json.has("genres")) {
                JSONArray arr = json.getJSONArray("genres");
                for (int i = 0; i < arr.length(); i++) genres.add(arr.getString(i));
            }

            MediaEntry media = mediaService.createMedia(
                    title, description, type, releaseYear, ageRestriction, creatorId, genres
            );

            JSONObject response = new JSONObject()
                    .put("id", media.getId())
                    .put("title", media.getTitle())
                    .put("description", media.getDescription())
                    .put("type", media.getType().name())
                    .put("releaseYear", media.getReleaseYear())
                    .put("ageRestriction", media.getAgeRestriction().name())
                    .put("creatorId", media.getCreatorId())
                    .put("genres", new JSONArray(media.getGenres()));

            return new Response(Status.CREATED.getCode(), response.toString());

        } catch (Exception e) {
            return new Response(Status.BAD_REQUEST.getCode(),
                    new JSONObject().put("error", e.getMessage()).toString());
        }
    }

    public Response getMedia(Request request) {
        try {
            Long id = Long.parseLong(request.getPathParam("mediaId"));
            MediaEntry media = mediaService.getMediaById(id);

            JSONObject response = buildMediaJson(media);
            return new Response(Status.OK.getCode(), response.toString());
        } catch (Exception e) {
            return new Response(Status.NOT_FOUND.getCode(),
                    new JSONObject().put("error", "Media not found").toString());
        }
    }

    public Response getAllMedia(Request request) {
        try {
            List<MediaEntry> mediaList = mediaService.getAllMedia();
            JSONArray jsonArray = new JSONArray();

            for (MediaEntry media : mediaList) {
                jsonArray.put(buildMediaJson(media));
            }

            return new Response(Status.OK.getCode(), jsonArray.toString());
        } catch (Exception e) {
            return new Response(Status.INTERNAL_SERVER_ERROR.getCode(),
                    new JSONObject().put("error", e.getMessage()).toString());
        }
    }

    public Response updateMedia(Request request) {
        try {
            Long id = Long.parseLong(request.getPathParam("mediaId"));
            JSONObject json = new JSONObject(request.getBody());

            String title = json.has("title") ? json.getString("title") : null;
            String description = json.has("description") ? json.getString("description") : null;
            Integer releaseYear = json.has("releaseYear") ? json.getInt("releaseYear") : null;

            List<String> genres = null;
            if (json.has("genres")) {
                genres = new ArrayList<>();
                JSONArray genreArray = json.getJSONArray("genres");
                for (int i = 0; i < genreArray.length(); i++) {
                    genres.add(genreArray.getString(i));
                }
            }

            MediaEntry media = mediaService.updateMedia(id, title, description, releaseYear, genres);
            JSONObject response = buildMediaJson(media);

            return new Response(Status.OK.getCode(), response.toString());
        } catch (Exception e) {
            return new Response(Status.BAD_REQUEST.getCode(),
                    new JSONObject().put("error", e.getMessage()).toString());
        }
    }

    public Response deleteMedia(Request request) {
        try {
            Long id = Long.parseLong(request.getPathParam("mediaId"));
            Long userId = Long.parseLong(request.getPathParam("userId"));

            mediaService.deleteMedia(id, userId);
            return new Response(Status.OK.getCode(),
                    new JSONObject().put("message", "Media deleted").toString());
        } catch (Exception e) {
            return new Response(Status.BAD_REQUEST.getCode(),
                    new JSONObject().put("error", e.getMessage()).toString());
        }
    }

    //  Helper method to build consistent JSON for MediaEntry
    private JSONObject buildMediaJson(MediaEntry media) {
        JSONObject json = new JSONObject();
        json.put("id", media.getId());
        json.put("title", media.getTitle());
        json.put("description", media.getDescription());
        json.put("type", media.getType().name());
        json.put("releaseYear", media.getReleaseYear());
        json.put("ageRestriction", media.getAgeRestriction().name());
        json.put("creatorId", media.getCreatorId());
        json.put("averageRating", media.getAverageRating());
        json.put("totalRatings", media.getTotalRatings());
        json.put("favoritesCount", media.getFavoritesCount());
        json.put("createdDate", media.getCreatedDate().toString());
        json.put("lastModified", media.getLastModified().toString());

        JSONArray genreArray = new JSONArray(media.getGenres());
        json.put("genres", genreArray);

        return json;
    }
}
