package es.anescdev.sumatory.dto;

import java.util.List;

import es.anescdev.sumatory.model.Sumatory;

/**
 * @author AnesCDev
 */
public record FetchedSumatoriesResult(List<Sumatory> sumatories, long totalEntities) {
    
}
