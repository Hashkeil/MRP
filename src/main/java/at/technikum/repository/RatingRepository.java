package at.technikum.repository;

import at.technikum.database.DatabaseConnection;
import at.technikum.model.Rating;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RatingRepository {

    public Rating save(Rating rating) throws SQLException {
        String sql = "INSERT INTO ratings (user_id, media_id, stars, comment, confirmed, likes_count) " +
                "VALUES (?, ?, ?, ?, ?, ?) RETURNING id";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, rating.getUserId());
            stmt.setLong(2, rating.getMediaId());
            stmt.setInt(3, rating.getStars());
            stmt.setString(4, rating.getComment());
            stmt.setBoolean(5, rating.isConfirmed());
            stmt.setInt(6, rating.getLikesCount());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) rating.setId(rs.getLong("id"));
        }
        return rating;
    }

    public Optional<Rating> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM ratings WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return Optional.of(mapRow(rs));
        }
        return Optional.empty();
    }

    public List<Rating> findByUserId(Long userId) throws SQLException {
        List<Rating> ratings = new ArrayList<>();
        String sql = "SELECT * FROM ratings WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) ratings.add(mapRow(rs));
        }
        return ratings;
    }

    public List<Rating> findByMediaId(Long mediaId) throws SQLException {
        List<Rating> ratings = new ArrayList<>();
        String sql = "SELECT * FROM ratings WHERE media_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, mediaId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) ratings.add(mapRow(rs));
        }
        return ratings;
    }

    public Optional<Rating> findByUserIdAndMediaId(Long userId, Long mediaId) throws SQLException {
        String sql = "SELECT * FROM ratings WHERE user_id = ? AND media_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, userId);
            stmt.setLong(2, mediaId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return Optional.of(mapRow(rs));
        }
        return Optional.empty();
    }

    public void update(Rating rating) throws SQLException {
        String sql = "UPDATE ratings SET stars = ?, comment = ?, confirmed = ?, likes_count = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, rating.getStars());
            stmt.setString(2, rating.getComment());
            stmt.setBoolean(3, rating.isConfirmed());
            stmt.setInt(4, rating.getLikesCount());
            stmt.setLong(5, rating.getId());

            stmt.executeUpdate();
        }
    }

    public boolean delete(Long id) throws SQLException {
        String sql = "DELETE FROM ratings WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    private Rating mapRow(ResultSet rs) throws SQLException {
        Rating rating = new Rating(
                rs.getLong("user_id"),
                rs.getLong("media_id"),
                rs.getInt("stars"),
                rs.getString("comment")
        );
        rating.setId(rs.getLong("id"));
        rating.setConfirmed(rs.getBoolean("confirmed"));
        rating.setLikesCount(rs.getInt("likes_count"));
        return rating;
    }

    public int countByUserId(Long userId) {
        String sql = "SELECT COUNT(*) FROM ratings WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

}
