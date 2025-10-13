// Favorite.java
package at.technikum.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Favorite {
    private Long id;
    private Long userId;
    private Long mediaId;
    private LocalDateTime dateAdded;

    public Favorite() {
        this.dateAdded = LocalDateTime.now();
    }

    public Favorite(Long userId, Long mediaId) {
        this();
        this.userId = userId;
        this.mediaId = mediaId;
    }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getMediaId() { return mediaId; }
    public void setMediaId(Long mediaId) { this.mediaId = mediaId; }

    public LocalDateTime getDateAdded() { return dateAdded; }
    public void setDateAdded(LocalDateTime dateAdded) { this.dateAdded = dateAdded; }


    public boolean belongsTo(Long userId) {
        return Objects.equals(this.userId, userId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Favorite favorite = (Favorite) o;
        return Objects.equals(userId, favorite.userId) &&
                Objects.equals(mediaId, favorite.mediaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, mediaId);
    }

    @Override
    public String toString() {
        return "Favorite{" +
                "id=" + id +
                ", userId=" + userId +
                ", mediaId=" + mediaId +
                ", dateAdded=" + dateAdded +
                '}';
    }
}