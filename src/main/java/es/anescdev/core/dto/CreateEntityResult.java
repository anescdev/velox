package es.anescdev.core.dto;

/**
 * @author AnesCDev
 */
/**
 * @author AnesCDev
 */
public record CreateEntityResult<T>(boolean success, String message, T entity) {
    
}
