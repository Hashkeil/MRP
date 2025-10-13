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
    private final UserController userController;
    private final MediaController mediaController;
    private final RatingController ratingController;
    private final FavoriteController favoriteController;
    private final RecommendationController recommendationController;
    private final LeaderboardController leaderboardController;

    public SimpleApplication() {
        // Repositories
        UserRepository userRepo = new UserRepository();
        MediaRepository mediaRepo = new MediaRepository();
        RatingRepository ratingRepo = new RatingRepository();
        FavoriteRepository favoriteRepo = new FavoriteRepository();

        // Services
        UserService userService = new UserService(userRepo);
        MediaService mediaService = new MediaService(mediaRepo);
        RatingService ratingService = new RatingService(ratingRepo, mediaRepo);
        FavoriteService favoriteService = new FavoriteService(favoriteRepo, mediaRepo);
        RecommendationService recommendationService = new RecommendationService(mediaRepo, ratingRepo, userRepo);
        LeaderboardService leaderboardService = new LeaderboardService(userRepo, ratingRepo);

        // Controllers
        this.userController = new UserController(userService);
        this.mediaController = new MediaController(mediaService);
        this.ratingController = new RatingController(ratingService);
        this.favoriteController = new FavoriteController(favoriteService);
        this.recommendationController = new RecommendationController(recommendationService);
        this.leaderboardController = new LeaderboardController(leaderboardService);

        this.router = new Router();
        setupRoutes();
    }

    private void setupRoutes() {
        // User routes (Auth + Profile)
        router.addRoute("POST", "/api/users/register", req -> userController.register(req));
        router.addRoute("POST", "/api/users/login", req -> userController.login(req));
        router.addRoute("GET", "/api/users/{userId}/profile", req -> userController.getProfile(req));
        router.addRoute("PUT", "/api/users/{userId}/profile", req -> userController.updateProfile(req));
        router.addRoute("GET", "/api/users/{userId}/ratings", req -> userController.getRatings(req));
        router.addRoute("GET", "/api/users/{userId}/favorites", req -> userController.getFavorites(req));

        // Media routes
        router.addRoute("GET", "/api/media", req -> mediaController.getAllMedia(req));
        router.addRoute("POST", "/api/media", req -> mediaController.createMedia(req));
        router.addRoute("GET", "/api/media/{mediaId}", req -> mediaController.getMedia(req));
        router.addRoute("PUT", "/api/media/{mediaId}", req -> mediaController.updateMedia(req));
        router.addRoute("DELETE", "/api/media/{mediaId}", req -> mediaController.deleteMedia(req));

        // Rating routes
        router.addRoute("POST", "/api/media/{mediaId}/rate", req -> ratingController.rateMedia(req));
        router.addRoute("PUT", "/api/ratings/{ratingId}", req -> ratingController.updateRating(req));
        router.addRoute("DELETE", "/api/ratings/{ratingId}", req -> ratingController.deleteRating(req));
        router.addRoute("POST", "/api/ratings/{ratingId}/like", req -> ratingController.likeRating(req));
        router.addRoute("POST", "/api/ratings/{ratingId}/confirm", req -> ratingController.confirmRating(req));

        // Favorite routes
        router.addRoute("POST", "/api/media/{mediaId}/favorite", req -> favoriteController.addFavorite(req));
        router.addRoute("DELETE", "/api/media/{mediaId}/favorite", req -> favoriteController.removeFavorite(req));

        // Recommendation routes
        router.addRoute("GET", "/api/users/{userId}/recommendations", req -> recommendationController.getRecommendations(req));

        // Leaderboard routes
        router.addRoute("GET", "/api/leaderboard", req -> leaderboardController.getLeaderboard(req));
    }

    public Response handle(Request request) {
        Function<Request, Response> endpoint = router.findEndpoint(request);

        if (endpoint == null) {
            return new Response(Status.NOT_FOUND.getCode(), "{\"message\":\"Not found\"}");
        }

        return endpoint.apply(request);
    }
}