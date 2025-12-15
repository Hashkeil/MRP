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
        this.recommendationController = new RecommendationController(recommendationService, authService);
        this.leaderboardController = new LeaderboardController(leaderboardService, authService);

        this.router = new Router();
        setupRoutes();
    }

    private void setupRoutes() {
        //  AUTHENTICATION ROUTES
        router.addRoute("POST", "/api/users/register", authController::register);
        router.addRoute("POST", "/api/users/login", authController::login);

        //  AUTHENTICATION ROUTES
        router.addRoute("POST", "/api/users/logout", authController::logout);

        //  USER ROUTES
        router.addRoute("GET", "/api/users/{userId}/profile", userController::getProfile);
        router.addRoute("PUT", "/api/users/{userId}/profile", userController::updateProfile);
        router.addRoute("GET", "/api/users/{userId}/stats", userController::getRatings);
        router.addRoute("GET", "/api/users/{userId}/favorites", favoriteController::getUserFavorites);

        //  MEDIA ROUTES
        //  Public endpoints
        router.addRoute("GET", "/api/media", mediaController::getAllMedia);
        router.addRoute("GET", "/api/media/{mediaId}", mediaController::getMedia);

        // Search and Filter endpoints (REQUIRED BY SPEC)
        router.addRoute("GET", "/api/media/search/{query}", mediaController::searchMedia);
        router.addRoute("GET", "/api/media/filter/genre/{genre}", mediaController::filterByGenre);
        router.addRoute("GET", "/api/media/filter/type/{type}", mediaController::filterByType);
        router.addRoute("GET", "/api/media/filter/year/{year}", mediaController::filterByYear);
        router.addRoute("GET", "/api/media/filter/age/{age}", mediaController::filterByAgeRestriction);

        // Protected endpoints
        router.addRoute("POST", "/api/media", mediaController::createMedia);
        router.addRoute("PUT", "/api/media/{mediaId}", mediaController::updateMedia);
        router.addRoute("DELETE", "/api/media/{mediaId}", mediaController::deleteMedia);

        //  RATING ROUTES
        router.addRoute("POST", "/api/media/{mediaId}/rate", ratingController::rateMedia);
        router.addRoute("GET", "/api/media/{mediaId}/rate", ratingController::getMediaRatings);
        router.addRoute("GET", "/api/users/{userId}/ratings", ratingController::getUserRatings);
        router.addRoute("PUT", "/api/ratings/{ratingId}", ratingController::updateRating);
        router.addRoute("DELETE", "/api/ratings/{ratingId}", ratingController::deleteRating);
        router.addRoute("POST", "/api/ratings/{ratingId}/like", ratingController::likeRating);
        router.addRoute("POST", "/api/ratings/{ratingId}/confirm", ratingController::confirmRating);

        //  FAVORITE ROUTES
        router.addRoute("POST", "/api/media/{mediaId}/favorite", favoriteController::addFavorite);
        router.addRoute("DELETE", "/api/media/{mediaId}/favorite", favoriteController::removeFavorite);

        // RECOMMENDATION ROUTES
        router.addRoute("GET", "/api/users/{userId}/recommendations", recommendationController::getRecommendations);

        //  LEADERBOARD ROUTES
        router.addRoute("GET", "/api/leaderboard", leaderboardController::getLeaderboard);
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