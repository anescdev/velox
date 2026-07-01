package es.anescdev.core.dto;

import java.util.List;


/**
 * @author AnesCDev
 */
public record FetchedEntitiesResult<T>(List<T> sumatories, long totalEntities) {
    
}
