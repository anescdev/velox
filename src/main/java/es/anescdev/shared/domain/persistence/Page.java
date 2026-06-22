package es.anescdev.shared.domain.persistence;

import java.util.List;

/**
 * Pagina de resultados
 * @author AnesCDev
 * 
 * @param entities entidades encontradas
 * @param totalEntities total de entidades de la tabla en esta consulta
 * @param entitiesPerPage entidades máximas de la página
 * @param currentPage pagina actual
 */
public record Page<T>(List<T> entities, int totalEntities, int entitiesPerPage, int currentPage) {
}
