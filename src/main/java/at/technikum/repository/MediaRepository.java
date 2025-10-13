package at.technikum.repository;

import at.technikum.model.MediaEntry;
import java.util.*;
import java.util.stream.Collectors;

public class MediaRepository {

    private final Map<Long, MediaEntry> mediaEntries = new HashMap<>();
    private Long nextId = 1L;

    public MediaEntry save(MediaEntry media) {
        if (media.getId() == null) {
            media.setId(nextId++);
        }
        mediaEntries.put(media.getId(), media);
        return media;
    }

    public Optional<MediaEntry> findById(Long id) {
        return Optional.ofNullable(mediaEntries.get(id));
    }

    public List<MediaEntry> findAll() {
        return new ArrayList<>(mediaEntries.values());
    }

    public List<MediaEntry> findByCreatorId(Long creatorId) {
        return mediaEntries.values().stream()
                .filter(m -> m.getCreatorId().equals(creatorId))
                .collect(Collectors.toList());
    }

    public boolean delete(Long id) {
        return mediaEntries.remove(id) != null;
    }

    public void update(MediaEntry media) {
        if (media.getId() != null && mediaEntries.containsKey(media.getId())) {
            mediaEntries.put(media.getId(), media);
        }
    }
}