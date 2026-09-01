package es.anescdev.velox.core.mapper;

import es.anescdev.velox.core.data.entities.Identificable;

/**
 * Contrato que deben cumplir los DTO de creación (records) para poder convertirse en la
 * entidad persistible correspondiente. Lo usa {@link es.anescdev.velox.core.services.AbstractService#createEntity}
 * para no tener que conocer el mapeo concreto de cada dominio.
 *
 * @param <T>  tipo de la entidad destino
 * @param <ID> tipo del identificador de esa entidad
 */
public interface ToEntityMapper<T extends Identificable<ID>, ID> {
    T mapToEntity();
}
