package at.technikum.repository;

import at.technikum.database.DatabaseConnection;
import at.technikum.model.MediaEntry;
import at.technikum.model.enums.AgeRestriction;
import at.technikum.model.enums.MediaType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MediaRepository {

    public MediaEntry save(MediaEntry media) throws SQLException {
        String sql = "INSERT INTO media_entries " +
                "(title, description, type, release_year, age_restriction, creator_id, average_rating, total_ratings, favorites_count) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, media.getTitle());
            stmt.setString(2, media.getDescription());
            stmt.setString(3, media.getType().name());
            stmt.setInt(4, media.getReleaseYear());
            stmt.setString(5, media.getAgeRestriction().name());
            stmt.setLong(6, media.getCreatorId());
            stmt.setDouble(7, media.getAverageRating());
            stmt.setInt(8, media.getTotalRatings());
            stmt.setInt(9, media.getFavoritesCount());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) media.setId(rs.getLong("id"));
        }
        return media;
    }

    public Optional<MediaEntry> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM media_entries WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return Optional.of(mapRow(rs));
        }
        return Optional.empty();
    }

    public List<MediaEntry> findAll() throws SQLException {
        List<MediaEntry> mediaList = new ArrayList<>();
        String sql = "SELECT * FROM media_entries";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) mediaList.add(mapRow(rs));
        }
        return mediaList;
    }

    public List<MediaEntry> findByCreatorId(Long creatorId) throws SQLException {
        List<MediaEntry> mediaList = new ArrayList<>();
        String sql = "SELECT * FROM media_entries WHERE creator_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, creatorId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) mediaList.add(mapRow(rs));
        }
        return mediaList;
    }

    public void update(MediaEntry media) throws SQLException {
        String sql = "UPDATE media_entries SET title = ?, description = ?, type = ?, release_year = ?, age_restriction = ?, " +
                "average_rating = ?, total_ratings = ?, favorites_count = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, media.getTitle());
            stmt.setString(2, media.getDescription());
            stmt.setString(3, media.getType().name());
            stmt.setInt(4, media.getReleaseYear());
            stmt.setString(5, media.getAgeRestriction().name());
            stmt.setDouble(6, media.getAverageRating());
            stmt.setInt(7, media.getTotalRatings());
            stmt.setInt(8, media.getFavoritesCount());
            stmt.setLong(9, media.getId());

            stmt.executeUpdate();
        }
    }

    public boolean delete(Long id) throws SQLException {
        String sql = "DELETE FROM media_entries WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    private MediaEntry mapRow(ResultSet rs) throws SQLException {
        MediaEntry media = new MediaEntry(
                rs.getString("title"),
                rs.getString("description"),
                MediaType.valueOf(rs.getString("type")),
                rs.getInt("release_year"),
                AgeRestriction.valueOf(rs.getString("age_restriction")),
                rs.getLong("creator_id")
        );
        media.setId(rs.getLong("id"));
        media.setAverageRating(rs.getDouble("average_rating"));
        media.setTotalRatings(rs.getInt("total_ratings"));
        media.setFavoritesCount(rs.getInt("favorites_count"));
        return media;
    }
}
