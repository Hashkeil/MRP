package at.technikum.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class User {


    private Long id;
    private String username;
    private String password;
    private String token;

    private int totalRatings;
    private double averageGivenRating;
    private String favoriteGenre;


    public User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }




    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public int getTotalRatings() { return totalRatings; }
    public void setTotalRatings(int totalRatings) { this.totalRatings = totalRatings; }

    public double getAverageGivenRating() { return averageGivenRating; }
    public void setAverageGivenRating(double averageGivenRating) { this.averageGivenRating = averageGivenRating; }

    public String getFavoriteGenre() { return favoriteGenre; }
    public void setFavoriteGenre(String favoriteGenre) { this.favoriteGenre = favoriteGenre; }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                '}';
    }
}