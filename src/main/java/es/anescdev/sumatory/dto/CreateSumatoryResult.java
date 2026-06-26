package es.anescdev.sumatory.dto;

import es.anescdev.sumatory.model.Sumatory;

/**
 * @author AnesCDev
 */
public record CreateSumatoryResult(boolean success, String message, Sumatory entity) {
    
}
