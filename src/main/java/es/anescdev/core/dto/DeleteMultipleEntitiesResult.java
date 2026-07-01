package es.anescdev.core.dto;

import java.util.List;
import java.util.Optional;

/**
 * @author AnesCDev
 */
public record DeleteMultipleEntitiesResult<T>(boolean success, int removedCount, Optional<List<T>> notRemoved) {

}
