package es.anescdev.sumatory.model.dto;

import java.util.List;

import es.anescdev.sumatory.model.entities.Sumatory;

/**
 * @author AnesCDev
 */
public record FetchedSumatoriesResult(List<Sumatory> sumatories, long totalEntities) {
    
}
