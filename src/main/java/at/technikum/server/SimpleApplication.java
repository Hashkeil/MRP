package at.technikum.server;

import at.technikum.controller.*;
import at.technikum.router.Router;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import at.technikum.server.http.Status;
import at.technikum.repository.*;
import at.technikum.service.*;

import java.util.function.Function;

public class SimpleApplication {

    private final Router router;
    private final AuthController authController;
    private final UserController userController;
    private final MediaController mediaController;
    private final RatingController ratingController;
    private final FavoriteController favoriteController;
    private final RecommendationController recommendationController;
    private final LeaderboardController leaderboardController;

    public SimpleApplication() {

        UserRepository userRepo = new UserRepository();
        MediaRepository mediaRepo = new MediaRepository();
        RatingRepository ratingRepo = new RatingRepository();
        FavoriteRepository favoriteRepo = new FavoriteRepository();

        AuthService authService = new AuthService(userRepo);
        UserService userService = new UserService(userRepo, ratingRepo);
        MediaService mediaService = new MediaService(mediaRepo);
        RatingService ratingService = new RatingService(ratingRepo, mediaRepo, userService);
        FavoriteService favoriteService = new FavoriteService(favoriteRepo, mediaRepo);
        RecommendationService recommendationService = new RecommendationService(mediaRepo, ratingRepo, userRepo);
        LeaderboardService leaderboardService = new LeaderboardService(userRepo, ratingRepo);

        this.authController = new AuthController(authService);
        this.userController = new UserController(userService, authService);
        this.mediaController = new MediaController(mediaService, authService);
        this.ratingController = new RatingController(ratingService, authService);
        this.favoriteController = new FavoriteController(favoriteService, authService);
        this.recommendationController = new RecommendationController(recommendationService, authService); // ADDED authService
        this.leaderboardController = new LeaderboardController(leaderboardService, authService); // ADDED authService

        this.router = new Router();
        setupRoutes();
    }

    private void setupRoutes() {
        //  AUTHENTICATION ROUTES
        router.addRoute("POST", "/api/users/register", req -> authController.register(req));
        router.addRoute("POST", "/api/users/login", req -> authController.login(req));

        //  AUTHENTICATION ROUTES
        router.addRoute("POST", "/api/users/logout", req -> authController.logout(req));

        //  USER ROUTES
        router.addRoute("GET", "/api/users/{userId}/profile", req -> userController.getProfile(req));
        router.addRoute("PUT", "/api/users/{userId}/profile", req -> userController.updateProfile(req));
        router.addRoute("GET", "/api/users/{userId}/stats", req -> userController.getRatings(req));
        router.addRoute("GET", "/api/users/{userId}/favorites", req -> favoriteController.getUserFavorites(req));

        //  MEDIA ROUTES
        // Public endpoints
        router.addRoute("GET", "/api/media", req -> mediaController.getAllMedia(req));
        router.addRoute("GET", "/api/media/{mediaId}", req -> mediaController.getMedia(req));

        // Protected endpoints
        router.addRoute("POST", "/api/media", req -> mediaController.createMedia(req));
        router.addRoute("PUT", "/api/media/{mediaId}", req -> mediaController.updateMedia(req));
        router.addRoute("DELETE", "/api/media/{mediaId}", req -> mediaController.deleteMedia(req));

        //  RATING ROUTES
        router.addRoute("POST", "/api/media/{mediaId}/ratings", req -> ratingController.rateMedia(req));
        router.addRoute("GET", "/api/media/{mediaId}/ratings", req -> ratingController.getMediaRatings(req));
        router.addRoute("GET", "/api/users/{userId}/ratings", req -> ratingController.getUserRatings(req));
        router.addRoute("PUT", "/api/ratings/{ratingId}", req -> ratingController.updateRating(req));
        router.addRoute("DELETE", "/api/ratings/{ratingId}", req -> ratingController.deleteRating(req));
        router.addRoute("POST", "/api/ratings/{ratingId}/like", req -> ratingController.likeRating(req));
        router.addRoute("POST", "/api/ratings/{ratingId}/confirm", req -> ratingController.confirmRating(req));

        //  FAVORITE ROUTES
        router.addRoute("POST", "/api/media/{mediaId}/favorites", req -> favoriteController.addFavorite(req));
        router.addRoute("DELETE", "/api/media/{mediaId}/favorites", req -> favoriteController.removeFavorite(req));

        // RECOMMENDATION ROUTES
        router.addRoute("GET", "/api/users/{userId}/recommendations", req -> recommendationController.getRecommendations(req));

        //  LEADERBOARD ROUTES
        router.addRoute("GET", "/api/leaderboard", req -> leaderboardController.getLeaderboard(req));
    }

    public Response handle(Request request) {
        try {
            Function<Request, Response> endpoint = router.findEndpoint(request);

            if (endpoint == null) {
                return new Response(Status.NOT_FOUND.getCode(),
                        "{\"error\":\"Endpoint not found: " + request.getMethod() + " " + request.getPath() + "\"}");
            }

            return endpoint.apply(request);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(Status.INTERNAL_SERVER_ERROR.getCode(),
                    "{\"error\":\"Internal server error: " + e.getMessage() + "\"}");
        }
    }
}