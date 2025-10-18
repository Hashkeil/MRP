package at.technikum.service;

import at.technikum.model.Favorite;
import at.technikum.model.MediaEntry;
import at.technikum.repository.FavoriteRepository;
import at.technikum.repository.MediaRepository;

import java.sql.SQLException;
import java.util.List;

public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final MediaRepository mediaRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, MediaRepository mediaRepository) {
        this.favoriteRepository = favoriteRepository;
        this.mediaRepository = mediaRepository;
    }

    public Favorite addFavorite(Long userId, Long mediaId) throws Exception {
        MediaEntry media = mediaRepository.findById(mediaId)
                .orElseThrow(() -> new Exception("Media not found"));

        if (favoriteRepository.existsByUserIdAndMediaId(userId, mediaId)) {
            throw new Exception("Media already in favorites");
        }

        Favorite favorite = new Favorite(userId, mediaId);
        favorite = favoriteRepository.save(favorite);

        media.incrementFavorites();
        mediaRepository.update(media);

        return favorite;
    }


    public boolean removeFavorite(Long userId, Long mediaId) throws Exception {
        if (!favoriteRepository.existsByUserIdAndMediaId(userId, mediaId)) {
            throw new Exception("Favorite not found");
        }

        MediaEntry media = mediaRepository.findById(mediaId)
                .orElseThrow(() -> new Exception("Media not found"));

        boolean deleted = favoriteRepository.deleteByUserIdAndMediaId(userId, mediaId);
        if (deleted) {
            media.decrementFavorites();
            mediaRepository.update(media);
        }
        return deleted;
    }


    public List<Favorite> getUserFavorites(Long userId) throws SQLException {
        return favoriteRepository.findByUserId(userId);
    }

    public List<Favorite> getMediaFavorites(Long mediaId) throws SQLException {
        return favoriteRepository.findByMediaId(mediaId);
    }
}