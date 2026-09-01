package es.anescdev.velox.core.data.repositories;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.StatementBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.table.TableUtils;

import es.anescdev.velox.core.data.entities.CompositeKey;
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
/**
 * Igual que {@link ORMLiteAbstractRepository} pero para entidades con
 * <b>clave compuesta</b> ({@code Cod}, {@code SumatoryEntry}...), donde el identificador
 * lógico ({@code CID}) combina varias columnas. Construye manualmente las cláusulas
 * {@code WHERE} de igualdad a partir de {@link es.anescdev.velox.core.data.entities.CompositeKey#getColumnValue(String)}
 * porque ORMLite, a nivel de DAO, sigue trabajando con el id interno autogenerado
 * ({@code IID}) como clave física de la tabla.
 *
 * @param <T>   tipo de la entidad
 * @param <IID> tipo del identificador interno autogenerado (columna física)
 * @param <CID> tipo de la clave compuesta lógica que expone la entidad
 * @param <DAO> DAO de ORMLite concreto usado para acceder a la tabla
 */
public abstract class ORMLiteCompositeKeyAbstractRepository<T extends Identificable<CID>, IID, CID extends CompositeKey<IID>, DAO extends Dao<T, IID>>
        implements Repository<T, CID> {

    protected final DAO dao;

    protected abstract Class<T> getEntityClass();

    protected abstract String[] getCompositeIdColumns();

    protected abstract List<String> excludedFromSearch();

    protected abstract String getInternalIdColumn();

    @Override
    public T createEntity(T entity) throws CreateEntityException {
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
    public void removeEntityById(CID entityId) throws RemoveEntityException {
        try {
            DeleteBuilder<T, IID> query = this.dao.deleteBuilder();
            query.setWhere(this.getIdEqualsWhere(entityId, query));
            if (query.delete() < 1)
                throw new RemoveEntityException("This entity not exist. ID: " + entityId.toString());
        } catch (SQLException e) {
            throw new RemoveEntityException("Error deleting the entity. Error: " + e.getMessage());
        }
    }

    @Override
    public List<T> searchAll(CID lastSeenId, int limit) throws SearchEntityException {
        QueryBuilder<T, IID> searchAllQuery = this.dao.queryBuilder();
        try {
            if (limit > NO_LIMIT) {
                searchAllQuery.limit((long) limit);
            }
            searchAllQuery
                    .setWhere(this.getIdEqualsUpdateWhere(lastSeenId, searchAllQuery));
            return searchAllQuery.query();
        } catch (SQLException e) {
            throw new SearchEntityException("Error searching the entities. Error: " + e.getMessage());
        }
    }

    @Override
    public Optional<T> searchById(CID entityId) throws SearchEntityException {
        try {
            QueryBuilder<T, IID> querySearch = this.dao.queryBuilder();
            querySearch.setWhere(this.getIdEqualsWhere(entityId, querySearch));
            return Optional.ofNullable(querySearch.queryForFirst());
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

    private Where<T, IID> getIdEqualsWhere(CID entityId, StatementBuilder<T, IID> statement) throws SQLException {
        Where<T, IID> where = statement.where();
        String[] columns = this.getCompositeIdColumns();
        if (columns.length > 0) {
            for (String column : this.getCompositeIdColumns()) {
                where.eq(column, entityId.getColumnValue(column));
            }
            where.and(columns.length);
        }
        return where;
    }

    private Where<T, IID> getIdEqualsUpdateWhere(CID entityId, StatementBuilder<T, IID> statement) throws SQLException {
        var where = statement
                .where()
                .gt(this.getInternalIdColumn(), entityId.getInternalId());
        for (String column : this.getCompositeIdColumns()) {
            if (this.excludedFromSearch().contains(column))
                continue;
            where.and().eq(column, entityId.getColumnValue(column));
        }
        return where;
    }
}
