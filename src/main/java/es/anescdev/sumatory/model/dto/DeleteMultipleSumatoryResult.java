package es.anescdev.sumatory.model.dto;

import java.util.List;
import java.util.Optional;

import es.anescdev.sumatory.model.entities.Sumatory;

/**
 * @author AnesCDev
 */
public record DeleteMultipleSumatoryResult(boolean success, int removedCount, Optional<List<Sumatory>> notRemoved) {

}
