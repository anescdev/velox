package es.anescdev.velox.core.mapper;

import es.anescdev.velox.core.data.entities.Identificable;

/**
 * @author AnesCDev
 */
public interface ToEntityMapper<T extends Identificable<ID>, ID> {
    T mapToEntity();
}
