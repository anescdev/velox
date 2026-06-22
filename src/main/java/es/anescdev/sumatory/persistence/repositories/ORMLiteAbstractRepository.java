package es.anescdev.sumatory.persistence.repositories;

import java.sql.SQLException;
import java.util.Optional;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.table.TableUtils;

import es.anescdev.shared.domain.exceptions.CountEntityException;
import es.anescdev.shared.domain.exceptions.CreateEntityException;
import es.anescdev.shared.domain.exceptions.RemoveEntityException;
import es.anescdev.shared.domain.exceptions.SearchEntityException;
import es.anescdev.shared.domain.exceptions.SetupRepositoryException;
import es.anescdev.shared.domain.exceptions.UpdateEntityException;
import es.anescdev.shared.domain.persistence.Page;
import es.anescdev.shared.domain.persistence.Repository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class ORMLiteAbstractRepository<T, ID, DAO extends Dao<T, ID>> implements Repository<T, ID> {

    protected final DAO dao;

    protected abstract Class<T> getEntityClass();
    protected abstract String getIdColumn();

    @Override
    public T createEntity(T entity) throws CreateEntityException {
        try {
            if (this.dao.create(entity) < 1)
                throw new CreateEntityException("This entity not exist. ID: " + entity.toString());
            return entity;
        } catch (SQLException e) {
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
    public Page<T> searchAll(ID lastSeenId, int limit) throws SearchEntityException {
        QueryBuilder<T, ID> searchAllQuery = this.dao.queryBuilder();
        try {
            searchAllQuery
                    .limit((long) limit).setWhere(searchAllQuery
                            .where()
                            .gt(this.getIdColumn(), lastSeenId));
            return new Page<>(searchAllQuery.query(), -1, limit, -1);
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
            TableUtils.createTableIfNotExists(this.dao.getConnectionSource(),this.getEntityClass());
        } catch (SQLException e) {
            throw new SetupRepositoryException("Error initializing the table. Error: " + e.getMessage());
        }
    }
}
