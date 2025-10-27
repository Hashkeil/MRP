
package at.technikum.controller;

import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import at.technikum.server.http.Status;
import at.technikum.service.AuthService;
import at.technikum.service.MediaService;
import at.technikum.model.MediaEntry;
import at.technikum.model.User;
import at.technikum.model.enums.AgeRestriction;
import at.technikum.model.enums.MediaType;
import at.technikum.util.AuthUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MediaController {
    private final MediaService mediaService;
    private final AuthService authService;

    public MediaController(MediaService mediaService, AuthService authService) {
        this.mediaService = mediaService;
        this.authService = authService;
    }

    // POST /api/media
    public Response createMedia(Request request) {
        try {
            User user = AuthUtil.requireAuth(request.getHeader("authorization"), authService);
            JSONObject json = new JSONObject(request.getBody());


            String title = json.getString("title");
            String description = json.getString("description");
            MediaType type = MediaType.valueOf(json.getString("type"));
            int releaseYear = json.getInt("releaseYear");
            AgeRestriction ageRestriction = AgeRestriction.fromString(json.getString("ageRestriction"));

            List<String> genres = json.has("genres")
                    ? json.getJSONArray("genres").toList().stream().map(Object::toString).toList()
                    : new ArrayList<>();

            MediaEntry media = mediaService.createMedia(
                    title, description, type, releaseYear, ageRestriction, user.getId(), genres
            );

            return new Response(Status.CREATED.getCode(), buildMediaJson(media).toString());

        } catch (Exception e) {
            int code = e.getMessage().toLowerCase().contains("auth")
                    ? Status.UNAUTHORIZED.getCode()
                    : Status.BAD_REQUEST.getCode();
            return new Response(code, new JSONObject().put("error", e.getMessage()).toString());
        }
    }

    // GET /api/media/{mediaId}
    public Response getMedia(Request request) {
        try {
            Long id = Long.parseLong(request.getPathParam("mediaId"));
            MediaEntry media = mediaService.getMediaById(id);

            JSONObject response = buildMediaJson(media);
            return new Response(Status.OK.getCode(), response.toString());
        } catch (NumberFormatException e) {
            return new Response(Status.BAD_REQUEST.getCode(),
                    new JSONObject().put("error", "Invalid media ID format").toString());
        } catch (Exception e) {
            return new Response(Status.NOT_FOUND.getCode(),
                    new JSONObject().put("error", "Media not found").toString());
        }
    }


      //GET /api/media
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


    // PUT /api/media/{mediaId}
    public Response updateMedia(Request request) {
        try {
            User user = AuthUtil.requireAuth(request.getHeader("authorization"), authService);
            Long mediaId = Long.parseLong(request.getPathParam("mediaId"));

            MediaEntry media = mediaService.getMediaById(mediaId);
            if (!media.isCreatedBy(user.getId())) {
                throw new Exception("Not authorized to update this media");
            }

            JSONObject json = new JSONObject(request.getBody());

            String title = json.optString("title", null);
            String description = json.optString("description", null);
            Integer releaseYear = json.has("releaseYear") ? json.getInt("releaseYear") : null;

            List<String> genres = json.has("genres")
                    ? json.getJSONArray("genres").toList().stream().map(Object::toString).toList()
                    : null;

            MediaEntry updated = mediaService.updateMedia(mediaId, title, description, releaseYear, genres);
            return new Response(Status.OK.getCode(), buildMediaJson(updated).toString());

        } catch (NumberFormatException e) {
            return new Response(Status.BAD_REQUEST.getCode(),
                    new JSONObject().put("error", "Invalid media ID format").toString());
        } catch (Exception e) {
            int code = e.getMessage().toLowerCase().contains("auth")
                    ? (e.getMessage().toLowerCase().contains("not authorized") ? Status.FORBIDDEN.getCode() : Status.UNAUTHORIZED.getCode())
                    : Status.BAD_REQUEST.getCode();
            return new Response(code, new JSONObject().put("error", e.getMessage()).toString());
        }
    }



    // GET /api/media/search?query=...
    public Response searchMedia(Request request) {
        try {
            String query = request.getPathParam("query");
            if (query == null || query.isEmpty()) {
                return new Response(Status.BAD_REQUEST.getCode(),
                        new JSONObject().put("error", "Search query is required").toString());
            }

            List<MediaEntry> mediaList = mediaService.searchByTitle(query);
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

    // GET /api/media/filter/genre/{genre}
    public Response filterByGenre(Request request) {
        try {
            String genre = request.getPathParam("genre");
            List<MediaEntry> mediaList = mediaService.filterByGenre(genre);

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

    // GET /api/media/filter/type/{type}
    public Response filterByType(Request request) {
        try {
            String typeStr = request.getPathParam("type");
            MediaType type = MediaType.valueOf(typeStr.toUpperCase());
            List<MediaEntry> mediaList = mediaService.filterByType(type);

            JSONArray jsonArray = new JSONArray();
            for (MediaEntry media : mediaList) {
                jsonArray.put(buildMediaJson(media));
            }

            return new Response(Status.OK.getCode(), jsonArray.toString());
        } catch (IllegalArgumentException e) {
            return new Response(Status.BAD_REQUEST.getCode(),
                    new JSONObject().put("error", "Invalid media type").toString());
        } catch (Exception e) {
            return new Response(Status.INTERNAL_SERVER_ERROR.getCode(),
                    new JSONObject().put("error", e.getMessage()).toString());
        }
    }

    // GET /api/media/filter/year/{year}
    public Response filterByYear(Request request) {
        try {
            Integer year = Integer.parseInt(request.getPathParam("year"));
            List<MediaEntry> mediaList = mediaService.filterByYear(year);

            JSONArray jsonArray = new JSONArray();
            for (MediaEntry media : mediaList) {
                jsonArray.put(buildMediaJson(media));
            }

            return new Response(Status.OK.getCode(), jsonArray.toString());
        } catch (NumberFormatException e) {
            return new Response(Status.BAD_REQUEST.getCode(),
                    new JSONObject().put("error", "Invalid year format").toString());
        } catch (Exception e) {
            return new Response(Status.INTERNAL_SERVER_ERROR.getCode(),
                    new JSONObject().put("error", e.getMessage()).toString());
        }
    }

    // GET /api/media/filter/age/{age}
    public Response filterByAgeRestriction(Request request) {
        try {
            String ageStr = request.getPathParam("age");
            AgeRestriction restriction = AgeRestriction.valueOf(ageStr.toUpperCase());
            List<MediaEntry> mediaList = mediaService.filterByAgeRestriction(restriction);

            JSONArray jsonArray = new JSONArray();
            for (MediaEntry media : mediaList) {
                jsonArray.put(buildMediaJson(media));
            }

            return new Response(Status.OK.getCode(), jsonArray.toString());
        } catch (IllegalArgumentException e) {
            return new Response(Status.BAD_REQUEST.getCode(),
                    new JSONObject().put("error", "Invalid age restriction.").toString());
        } catch (Exception e) {
            return new Response(Status.INTERNAL_SERVER_ERROR.getCode(),
                    new JSONObject().put("error", e.getMessage()).toString());
        }
    }

    // DELETE /api/media/{mediaId}
    public Response deleteMedia(Request request) {
        try {
            User user = AuthUtil.requireAuth(request.getHeader("authorization"), authService);
            Long mediaId = Long.parseLong(request.getPathParam("mediaId"));

            mediaService.deleteMedia(mediaId, user.getId());

            return new Response(Status.OK.getCode(),
                    new JSONObject().put("message", "Media deleted successfully").toString());

        } catch (NumberFormatException e) {
            return new Response(Status.BAD_REQUEST.getCode(),
                    new JSONObject().put("error", "Invalid media ID format").toString());
        } catch (Exception e) {
            int code = e.getMessage().toLowerCase().contains("auth")
                    ? (e.getMessage().toLowerCase().contains("not authorized") ? Status.FORBIDDEN.getCode() : Status.UNAUTHORIZED.getCode())
                    : Status.BAD_REQUEST.getCode();
            return new Response(code,
                    new JSONObject().put("error", e.getMessage()).toString());
        }
    }



    // Helper method to build JSON response for a media entry
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

