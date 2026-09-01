package es.anescdev.velox.core.data.repositories;

import java.util.List;
import java.util.Optional;

import es.anescdev.velox.core.exceptions.CountEntityException;
import es.anescdev.velox.core.exceptions.CreateEntityException;
import es.anescdev.velox.core.exceptions.ExistingEntityException;
import es.anescdev.velox.core.exceptions.RemoveEntityException;
import es.anescdev.velox.core.exceptions.SearchEntityException;
import es.anescdev.velox.core.exceptions.SetupRepositoryException;
import es.anescdev.velox.core.exceptions.UpdateEntityException;

/**
 * Contrato genérico de acceso a datos que implementan todos los repositorios de la
 * aplicación (uno por entidad/dominio). Mantiene la capa de servicio ({@code AbstractService})
 * desacoplada de si por debajo hay ORMLite, clave simple o compuesta, etc.
 *
 * @param <T>  tipo de la entidad
 * @param <ID> tipo de su identificador (simple o compuesto)
 */
public interface Repository<T, ID> {
    public static final int NO_LIMIT = -1;
    public T createEntity(T entity) throws CreateEntityException, ExistingEntityException;

    public void updateEntity(T entity) throws UpdateEntityException;

    public void removeEntityById(ID entityId) throws RemoveEntityException;

    public List<T> searchAll(ID lastSeenId, int limit) throws SearchEntityException;

    public Optional<T> searchById(ID entityId) throws SearchEntityException;

    public long count() throws CountEntityException;

    public void setup() throws SetupRepositoryException;
}
