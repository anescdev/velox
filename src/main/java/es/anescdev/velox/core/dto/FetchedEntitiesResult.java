package es.anescdev.velox.core.dto;

import java.util.List;


/**
 * Resultado tipado de una búsqueda paginada en {@code AbstractService}: lista de entidades
 * de la página actual junto con el total de entidades existentes (usado por la UI para
 * mostrar el contador y decidir si hay más páginas que cargar).
 * 
 * @param <T>  tipo de las entidades encontradas
 */
public record FetchedEntitiesResult<T>(List<T> entities, long totalEntities, boolean success, String message) {
    public static <T> FetchedEntitiesResult<T> success(List<T> entities, long totalEntities) {
        return new FetchedEntitiesResult<>(entities, totalEntities, true, null);
    }
    public static <T> FetchedEntitiesResult<T> failed(String message) {
        return new FetchedEntitiesResult<>(null, 0, false, message);
    }
}
