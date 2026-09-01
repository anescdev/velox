package es.anescdev.velox.core.data.repositories;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.table.TableUtils;

import es.anescdev.velox.core.data.entities.Identificable;
import es.anescdev.velox.core.exceptions.CountEntityException;
import es.anescdev.velox.core.exceptions.CreateEntityException;
import es.anescdev.velox.core.exceptions.ExistingEntityException;
import es.anescdev.velox.core.exceptions.RemoveEntityException;
import es.anescdev.velox.core.exceptions.SearchEntityException;
import es.anescdev.velox.core.exceptions.SetupRepositoryException;
import es.anescdev.velox.core.exceptions.UpdateEntityException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class ORMLiteAbstractRepository<T extends Identificable<ID>, ID, DAO extends Dao<T, ID>>
        implements Repository<T, ID> {

    protected final DAO dao;

    protected abstract Class<T> getEntityClass();

    protected abstract String getIdColumn();

    @Override
    public T createEntity(T entity) throws CreateEntityException, ExistingEntityException {
        try {
            if (this.dao.create(entity) < 1)
                throw new CreateEntityException("This entity not exist. ID: " + entity.toString());
            return entity;
        } catch (SQLException e) {
            if (e.getCause().getMessage().startsWith("[SQLITE_CONSTRAINT_UNIQUE]"))
                throw new ExistingEntityException();
            throw new CreateEntityException("Error creating the entity. Error: " + e.getMessage());
        }
    }

    @Override
    public void updateEntity(T entity) throws UpdateEntityException {
        try {
            if (this.dao.update(entity) < 1)
                throw new UpdateEntityException("This entity not exist. ID: " + entity.toString());
        } catch (SQLException e) {
            throw new UpdateEntityException("Error updating the entity. Error: " + e.getMessage());
        }
    }

    @Override
    public void removeEntityById(ID entityId) throws RemoveEntityException {
        try {
            if (this.dao.deleteById(entityId) < 1)
                throw new RemoveEntityException("This entity not exist. ID: " + entityId.toString());
        } catch (SQLException e) {
            throw new RemoveEntityException("Error deleting the entity. Error: " + e.getMessage());
        }
    }

    @Override
    public List<T> searchAll(ID lastSeenId, int limit) throws SearchEntityException {
        QueryBuilder<T, ID> searchAllQuery = this.dao.queryBuilder();
        try {
            if (limit > NO_LIMIT) {
                searchAllQuery.limit((long) limit);
            }
            searchAllQuery
                    .setWhere(searchAllQuery
                            .where()
                            .gt(this.getIdColumn(), lastSeenId));
            return searchAllQuery.query();
        } catch (SQLException e) {
            throw new SearchEntityException("Error searching the entities. Error: " + e.getMessage());
        }
    }

    @Override
    public Optional<T> searchById(ID entityId) throws SearchEntityException {
        try {
            return Optional.ofNullable(this.dao.queryForId(entityId));
        } catch (SQLException e) {
            throw new SearchEntityException("Error searching the entities. Error: " + e.getMessage());
        }
    }

    @Override
    public long count() throws CountEntityException {
        try {
            return this.dao.countOf();
        } catch (SQLException e) {
            throw new CountEntityException("Error counting the entities. Error: " + e.getMessage());
        }
    }

    @Override
    public void setup() throws SetupRepositoryException {
        try {
            TableUtils.createTableIfNotExists(this.dao.getConnectionSource(), this.getEntityClass());
        } catch (SQLException e) {
            throw new SetupRepositoryException("Error initializing the table. Error: " + e.getMessage());
        }
    }
}
