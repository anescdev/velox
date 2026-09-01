package es.anescdev.velox.core.dto;

import java.util.List;
import java.util.Optional;

/**
 * @author AnesCDev
 */
public record DeleteMultipleEntitiesResult<T>(boolean success, int removedCount, Optional<List<T>> notRemoved) {
    public static <T> DeleteMultipleEntitiesResult<T> success(int removedCount) {
        return new DeleteMultipleEntitiesResult<>(true, removedCount, Optional.empty());
    }
    public static <T> DeleteMultipleEntitiesResult<T> failed(int removedCount, List<T> notRemoved) {
        return new DeleteMultipleEntitiesResult<>(false, removedCount, Optional.of(notRemoved));
    }
}
