package es.anescdev.sumatory.dto;

import java.util.Optional;

import es.anescdev.sumatory.utils.SumatoryList;

/**
 * @author AnesCDev
 */
public record DeleteMultipleSumatoryResult(boolean success, int removedCount, Optional<SumatoryList> notRemoved) {

}
