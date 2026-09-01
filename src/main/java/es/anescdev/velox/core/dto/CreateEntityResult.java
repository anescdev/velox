package es.anescdev.velox.core.dto;

import java.util.Optional;

/**
 * @author AnesCDev
 */
/**
 * @author AnesCDev
 */
public record CreateEntityResult<T>(boolean success, Optional<String> message, T entity) {

    public static <T> CreateEntityResult<T> success(T entity, String message) {
        return new CreateEntityResult<T>(true, Optional.of(message), entity);
    }

    public static <T> CreateEntityResult<T> success(T entity) {
        return new CreateEntityResult<T>(true, Optional.empty(), entity);
    }

    public static <T> CreateEntityResult<T> failed(String message) {
        return new CreateEntityResult<T>(false, Optional.of(message), null);
    }
}
