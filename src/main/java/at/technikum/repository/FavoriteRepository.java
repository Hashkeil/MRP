package at.technikum.repository;

import at.technikum.model.Favorite;

import java.util.*;

public class FavoriteRepository {

    private final Map<Long, Favorite> favorites = new HashMap<>();
    private final Map<Long, List<Favorite>> favoritesByUser = new HashMap<>();
    private final Map<Long, List<Favorite>> favoritesByMedia = new HashMap<>();
    private Long nextId = 1L;

    public Favorite save(Favorite favorite) {
        if (favorite.getId() == null) {
            favorite.setId(nextId++);
        }
        favorites.put(favorite.getId(), favorite);

        // Index by user
        favoritesByUser.computeIfAbsent(favorite.getUserId(), k -> new ArrayList<>()).add(favorite);

        // Index by media
        favoritesByMedia.computeIfAbsent(favorite.getMediaId(), k -> new ArrayList<>()).add(favorite);

        return favorite;
    }

    public Optional<Favorite> findById(Long id) {
        return Optional.ofNullable(favorites.get(id));
    }

    public List<Favorite> findAll() {
        return new ArrayList<>(favorites.values());
    }

    public List<Favorite> findByUserId(Long userId) {
        return new ArrayList<>(favoritesByUser.getOrDefault(userId, new ArrayList<>()));
    }

    public List<Favorite> findByMediaId(Long mediaId) {
        return new ArrayList<>(favoritesByMedia.getOrDefault(mediaId, new ArrayList<>()));
    }

    public Optional<Favorite> findByUserIdAndMediaId(Long userId, Long mediaId) {
        return favoritesByUser.getOrDefault(userId, new ArrayList<>()).stream()
                .filter(f -> f.getMediaId().equals(mediaId))
                .findFirst();
    }

    public boolean existsByUserIdAndMediaId(Long userId, Long mediaId) {
        return findByUserIdAndMediaId(userId, mediaId).isPresent();
    }

    public boolean delete(Long id) {
        Favorite favorite = favorites.remove(id);
        if (favorite != null) {
            // Remove from user index
            List<Favorite> userFavorites = favoritesByUser.get(favorite.getUserId());
            if (userFavorites != null) {
                userFavorites.remove(favorite);
            }

            // Remove from media index
            List<Favorite> mediaFavorites = favoritesByMedia.get(favorite.getMediaId());
            if (mediaFavorites != null) {
                mediaFavorites.remove(favorite);
            }
            return true;
        }
        return false;
    }

    public boolean deleteByUserIdAndMediaId(Long userId, Long mediaId) {
        Optional<Favorite> favorite = findByUserIdAndMediaId(userId, mediaId);
        return favorite.map(f -> delete(f.getId())).orElse(false);
    }

    public int countByMediaId(Long mediaId) {
        return favoritesByMedia.getOrDefault(mediaId, new ArrayList<>()).size();
    }

    public int countByUserId(Long userId) {
        return favoritesByUser.getOrDefault(userId, new ArrayList<>()).size();
    }
}