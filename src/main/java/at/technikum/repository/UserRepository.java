package at.technikum.repository;

import at.technikum.database.DatabaseConnection;
import at.technikum.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository {

    public User save(User user) throws SQLException {
        String sql = "INSERT INTO users (username, password, token, favorite_genre) VALUES (?, ?, ?, ?) RETURNING id";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getToken());
            stmt.setString(4, user.getFavoriteGenre());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) user.setId(rs.getLong("id"));
        }
        return user;
    }

    public void updateUser(User user) throws SQLException {
        String sql = "UPDATE users SET password = ?, token = ?, favorite_genre = ?, total_ratings = ?, average_given_rating = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getPassword());
            stmt.setString(2, user.getToken());
            stmt.setString(3, user.getFavoriteGenre());
            stmt.setInt(4, user.getTotalRatings());
            stmt.setDouble(5, user.getAverageGivenRating());
            stmt.setLong(6, user.getId());
            stmt.executeUpdate();
        }
    }

    public Optional<User> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return Optional.of(mapRow(rs));
        }
        return Optional.empty();
    }

    public Optional<User> findByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return Optional.of(mapRow(rs));
        }
        return Optional.empty();
    }

    public Optional<User> findByToken(String token) throws SQLException {
        String sql = "SELECT * FROM users WHERE token = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, token);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return Optional.of(mapRow(rs));
        }
        return Optional.empty();
    }

    public List<User> findAll() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) users.add(mapRow(rs));
        }
        return users;
    }

    public boolean existsByUsername(String username) throws SQLException {
        return findByUsername(username).isPresent();
    }

    private User mapRow(ResultSet rs) throws SQLException {
        User user = new User(rs.getString("username"), rs.getString("password"));
        user.setId(rs.getLong("id"));
        user.setToken(rs.getString("token"));
        user.setFavoriteGenre(rs.getString("favorite_genre"));
        user.setTotalRatings(rs.getInt("total_ratings"));
        user.setAverageGivenRating(rs.getDouble("average_given_rating"));
        return user;
    }
}


