package es.anescdev.sumatory.model.dto;

import es.anescdev.sumatory.model.entities.Sumatory;

/**
 * @author AnesCDev
 */
public record CreateSumatoryResult(boolean success, String message, Sumatory entity) {
    
}
