package es.anescdev.velox.core.dto;

import java.util.Optional;

/**
 * Resultado tipado de una operación de creación en {@code AbstractService}: si tuvo éxito,
 * lleva la entidad creada; si no, un mensaje de error listo para mostrar al usuario.
 * Evita que la capa de vista tenga que capturar excepciones de negocio directamente.
 * 
 * @param <T> tipo de la entidad creada
 */
public record CreateEntityResult<T>(boolean success, Optional<String> message, T entity) {

    public static <T> CreateEntityResult<T> success(T entity, String message) {
        return new CreateEntityResult<T>(true, Optional.of(message), entity);
    }

    public static <T> CreateEntityResult<T> success(T entity) {
        return new CreateEntityResult<T>(true, Optional.empty(), entity);
    }

    public static <T> CreateEntityResult<T> failed(String message) {
        return new CreateEntityResult<T>(false, Optional.of(message), null);
    }
}
