package es.anescdev.velox.core.dto;

import java.util.List;


/**
 * @author AnesCDev
 */
public record FetchedEntitiesResult<T>(List<T> entities, long totalEntities, boolean success, String message) {
    public static <T> FetchedEntitiesResult<T> success(List<T> entities, long totalEntities) {
        return new FetchedEntitiesResult<>(entities, totalEntities, true, null);
    }
    public static <T> FetchedEntitiesResult<T> failed(String message) {
        return new FetchedEntitiesResult<>(null, 0, false, message);
    }
}
