package at.technikum.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Rating {
    private Long id;
    private Long userId;
    private Long mediaId;
    private Integer stars;
    private String comment;
    private LocalDateTime timestamp;
    private Boolean confirmed;
    private Integer likesCount;

    public Rating() {
        this.timestamp = LocalDateTime.now();
        this.confirmed = false;
        this.likesCount = 0;
    }

    public Rating(Long userId, Long mediaId, Integer stars, String comment) {
        this();
        this.userId = userId;
        this.mediaId = mediaId;
        this.stars = stars;
        this.comment = comment;
    }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getMediaId() { return mediaId; }
    public void setMediaId(Long mediaId) { this.mediaId = mediaId; }

    public Integer getStars() { return stars; }
    public void setStars(Integer stars) {
        if (stars < 1 || stars > 5) {
            throw new IllegalArgumentException("Stars must be between 1 and 5");
        }
        this.stars = stars;
    }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public Boolean getConfirmed() { return confirmed; }
    public void setConfirmed(Boolean confirmed) { this.confirmed = confirmed; }

    public Integer getLikesCount() { return likesCount; }
    public void setLikesCount(Integer likesCount) { this.likesCount = likesCount; }



    // Business methods
    public boolean isConfirmed() {
        return this.confirmed != null && this.confirmed;
    }

    public void confirmRating() {
        this.confirmed = true;
    }

    public void incrementLikes() {
        this.likesCount++;
    }




    public boolean isOwnedBy(Long userId) {
        return Objects.equals(this.userId, userId);
    }

    public void updateRating(Integer stars, String comment) {
        setStars(stars);
        this.comment = comment;
        this.timestamp = LocalDateTime.now();
        this.confirmed = false;
    }






    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rating rating = (Rating) o;
        return Objects.equals(id, rating.id) &&
                Objects.equals(userId, rating.userId) &&
                Objects.equals(mediaId, rating.mediaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, mediaId);
    }

    @Override
    public String toString() {
        return "Rating{" +
                "id=" + id +
                ", userId=" + userId +
                ", mediaId=" + mediaId +
                ", stars=" + stars +
                ", confirmed=" + confirmed +
                ", likesCount=" + likesCount +
                '}';
    }
}