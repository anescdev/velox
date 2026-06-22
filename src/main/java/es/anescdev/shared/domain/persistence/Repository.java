package es.anescdev.shared.domain.persistence;

import java.util.Optional;

import es.anescdev.shared.domain.exceptions.CountEntityException;
import es.anescdev.shared.domain.exceptions.CreateEntityException;
import es.anescdev.shared.domain.exceptions.RemoveEntityException;
import es.anescdev.shared.domain.exceptions.SearchEntityException;
import es.anescdev.shared.domain.exceptions.SetupRepositoryException;
import es.anescdev.shared.domain.exceptions.UpdateEntityException;

/**
 * Interfaz que representa al repositorio de acceso a los datos.
 * Contiene los métodos genéricos necesarios para acceder a los datos de las
 * entidades elegidas.
 * 
 * @author AnesCDev
 * 
 * @param T entidad que manejará el repositorio
 * @param I tipo del identificador de la entidad
 */
public interface Repository<T, I> {
    public T createEntity(T entity) throws CreateEntityException;

    public void updateEntity(T entity) throws UpdateEntityException;

    public void removeEntityById(I entityId) throws RemoveEntityException;

    public Page<T> searchAll(I lastSeenId, int limit) throws SearchEntityException;

    public Optional<T> searchById(I entityId) throws SearchEntityException;

    public long count() throws CountEntityException;

    public void setup() throws SetupRepositoryException;
}
