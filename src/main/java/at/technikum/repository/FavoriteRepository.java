package at.technikum.repository;

import at.technikum.database.DatabaseConnection;
import at.technikum.model.Favorite;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FavoriteRepository {

    public Favorite save(Favorite favorite) throws SQLException {
        String sql = "INSERT INTO favorites (user_id, media_id, date_added) VALUES (?, ?, ?) RETURNING id";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, favorite.getUserId());
            stmt.setLong(2, favorite.getMediaId());
            stmt.setTimestamp(3, Timestamp.valueOf(favorite.getDateAdded()));

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) favorite.setId(rs.getLong("id"));
        }
        return favorite;
    }

    public List<Favorite> findByUserId(Long userId) throws SQLException {
        List<Favorite> favorites = new ArrayList<>();
        String sql = "SELECT * FROM favorites WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) favorites.add(mapRow(rs));
        }
        return favorites;
    }

    public List<Favorite> findByMediaId(Long mediaId) throws SQLException {
        List<Favorite> favorites = new ArrayList<>();
        String sql = "SELECT * FROM favorites WHERE media_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, mediaId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) favorites.add(mapRow(rs));
        }
        return favorites;
    }

    public Optional<Favorite> findByUserIdAndMediaId(Long userId, Long mediaId) throws SQLException {
        String sql = "SELECT * FROM favorites WHERE user_id = ? AND media_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, userId);
            stmt.setLong(2, mediaId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return Optional.of(mapRow(rs));
        }
        return Optional.empty();
    }

    public boolean existsByUserIdAndMediaId(Long userId, Long mediaId) throws SQLException {
        return findByUserIdAndMediaId(userId, mediaId).isPresent();
    }

    public boolean delete(Long id) throws SQLException {
        String sql = "DELETE FROM favorites WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deleteByUserIdAndMediaId(Long userId, Long mediaId) throws SQLException {
        Optional<Favorite> favorite = findByUserIdAndMediaId(userId, mediaId);
        return favorite.map(f -> {
            try {
                return delete(f.getId());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }).orElse(false);
    }

    private Favorite mapRow(ResultSet rs) throws SQLException {
        Favorite favorite = new Favorite(rs.getLong("user_id"), rs.getLong("media_id"));
        favorite.setId(rs.getLong("id"));
        favorite.setDateAdded(rs.getTimestamp("date_added").toLocalDateTime());
        return favorite;
    }
}
