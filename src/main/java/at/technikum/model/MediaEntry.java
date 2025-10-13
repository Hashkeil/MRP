package at.technikum.model;

import at.technikum.model.enums.AgeRestriction;
import at.technikum.model.enums.Genre;
import at.technikum.model.enums.MediaType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MediaEntry {

    private Long id;
    private String title;
    private String description;
    private MediaType type;
    private Integer releaseYear;
    private AgeRestriction ageRestriction;
    private Long creatorId;
    private LocalDateTime createdDate;
    private LocalDateTime lastModified;


    // Associated data
    private List<String> genres;
    private Double averageRating;
    private Integer totalRatings;
    private Integer favoritesCount;

    public MediaEntry() {
        this.createdDate = LocalDateTime.now();
        this.lastModified = LocalDateTime.now();
        this.genres = new ArrayList<>();
        this.averageRating = 0.0;
        this.totalRatings = 0;
        this.favoritesCount = 0;
    }

    public MediaEntry(String title, String description, MediaType type, Integer releaseYear,
                      AgeRestriction ageRestriction, Long creatorId) {
        this();
        this.title = title;
        this.description = description;
        this.type = type;
        this.releaseYear = releaseYear;
        this.ageRestriction = ageRestriction;
        this.creatorId = creatorId;
    }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) {
        this.title = title;
        updateLastModified();
    }

    public String getDescription() { return description; }
    public void setDescription(String description) {
        this.description = description;
        updateLastModified();
    }

    public MediaType getType() { return type; }
    public void setType(MediaType type) {
        this.type = type;
        updateLastModified();
    }

    public Integer getReleaseYear() { return releaseYear; }
    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
        updateLastModified();
    }

    public AgeRestriction getAgeRestriction() { return ageRestriction; }
    public void setAgeRestriction(AgeRestriction ageRestriction) {
        this.ageRestriction = ageRestriction;
        updateLastModified();
    }

    public Long getCreatorId() { return creatorId; }
    public void setCreatorId(Long creatorId) { this.creatorId = creatorId; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }

    public LocalDateTime getLastModified() { return lastModified; }
    public void setLastModified(LocalDateTime lastModified) { this.lastModified = lastModified; }

    public List<String> getGenres() { return new ArrayList<>(genres); }
    public void setGenres(List<String> genres) {
        this.genres = new ArrayList<>(genres);
        updateLastModified();
    }

    public Double getAverageRating() { return averageRating; }
    public void setAverageRating(Double averageRating) { this.averageRating = averageRating; }

    public Integer getTotalRatings() { return totalRatings; }
    public void setTotalRatings(Integer totalRatings) { this.totalRatings = totalRatings; }

    public Integer getFavoritesCount() { return favoritesCount; }
    public void setFavoritesCount(Integer favoritesCount) { this.favoritesCount = favoritesCount; }



    // Business methods
    public void addGenre(String genre) {
        if (!this.genres.contains(genre)) {
            this.genres.add(genre);
            updateLastModified();
        }
    }

    public void removeGenre(String genre) {
        if (this.genres.remove(genre)) {
            updateLastModified(); }
    }

    public boolean hasGenre(String genre) {
        return this.genres.contains(genre);
    }

    public void updateAverageRating(double newAverage, int totalCount) {
        this.averageRating = newAverage;
        this.totalRatings = totalCount;
    }



    public void incrementFavorites() {
        this.favoritesCount++;
    }
    public void decrementFavorites() {
        if (this.favoritesCount > 0) {
            this.favoritesCount--;
        }
    }




    public boolean isCreatedBy(Long userId) {
        return Objects.equals(this.creatorId, userId);
    }
    private void updateLastModified() {
        this.lastModified = LocalDateTime.now();
    }




    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MediaEntry media = (MediaEntry) o;
        return Objects.equals(id, media.id) && Objects.equals(title, media.title);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }
    @Override
    public String toString() {
        return "Media{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", type=" + type +
                ", releaseYear=" + releaseYear +
                ", averageRating=" + averageRating +
                '}';
    }





}