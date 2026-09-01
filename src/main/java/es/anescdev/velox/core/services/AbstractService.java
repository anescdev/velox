package es.anescdev.velox.core.services;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.BiConsumer;

import es.anescdev.velox.app.App;
import es.anescdev.velox.core.data.entities.Identificable;
import es.anescdev.velox.core.data.repositories.Repository;
import es.anescdev.velox.core.dto.CreateEntityResult;
import es.anescdev.velox.core.dto.DeleteEntityResult;
import es.anescdev.velox.core.dto.DeleteMultipleEntitiesResult;
import es.anescdev.velox.core.dto.FetchedEntitiesResult;
import es.anescdev.velox.core.exceptions.CountEntityException;
import es.anescdev.velox.core.exceptions.CreateEntityException;
import es.anescdev.velox.core.exceptions.ExistingEntityException;
import es.anescdev.velox.core.exceptions.RemoveEntityException;
import es.anescdev.velox.core.exceptions.SearchEntityException;
import es.anescdev.velox.core.mapper.ToEntityMapper;

/**
 * @author AnesCDev
 */
public abstract class AbstractService<T extends Identificable<ID>, ID, C extends ToEntityMapper<T, ID>> {

    protected final Repository<T, ID> repo;
    private final EnumMap<AbstractServiceMessage, String> messages;

    protected AbstractService(Repository<T, ID> repo) {
        this.repo = repo;
        this.messages = new EnumMap<>(AbstractServiceMessage.class);
        this.initializeMessages((key, val) -> this.messages.put(key, val));
    }

    protected abstract void initializeMessages(BiConsumer<AbstractServiceMessage, String> includeMessage);

    private String getServiceMessages(AbstractServiceMessage message) {
        return messages.get(message);
    }

    public FetchedEntitiesResult<T> searchEntity(ID lastId, int limit) {
        try {
            return FetchedEntitiesResult.success(repo.searchAll(lastId, limit), repo.count());
        } catch (SearchEntityException | CountEntityException e) {
            App.instance().getLogger().severe(e.getMessage());
            return FetchedEntitiesResult.failed(e.getMessage());
        }
    }

    public FetchedEntitiesResult<T> searchEntity(ID lastId) {
        return this.searchEntity(lastId, Repository.NO_LIMIT);
    }

    /**
     * Messages: createEntityError
     * 
     * @param entityDTO
     * @return
     */
    public CreateEntityResult<T> createEntity(C entityDTO) {
        try {
            return CreateEntityResult.success(
                    this.repo.createEntity(entityDTO.mapToEntity()),
                    this.getServiceMessages(AbstractServiceMessage.CREATE_ENTITY));
        } catch (CreateEntityException e) {
            App.instance().getLogger().severe(e.getMessage());
            return CreateEntityResult.failed(this.getServiceMessages(AbstractServiceMessage.CREATE_ENTITY_ERROR));
        } catch (ExistingEntityException e) {
            App.instance().getLogger().severe("The entity already exist on database");
            return CreateEntityResult.failed(this.getServiceMessages(AbstractServiceMessage.ENTITY_EXISTING_ERROR));
        }

    }

    public DeleteEntityResult deleteEntity(T entity) {
        try {
            this.repo.removeEntityById(entity.getId());
            return DeleteEntityResult.success();
        } catch (RemoveEntityException e) {
            App.instance().getLogger().severe(e.getMessage());
            return DeleteEntityResult.failed(e.getMessage());
        }
    }

    public DeleteMultipleEntitiesResult<T> deleteEntities(List<T> entities) {
        Queue<T> toRemove = new LinkedList<>(entities);
        try {
            for (T sumatory : entities) {
                this.repo.removeEntityById(sumatory.getId());
                toRemove.poll();
            }
            return DeleteMultipleEntitiesResult.success(entities.size());
        } catch (RemoveEntityException e) {
            App.instance().getLogger().severe(e.getMessage());
            return DeleteMultipleEntitiesResult.failed(entities.size() - toRemove.size(),
                    new ArrayList<>(toRemove));
        }
    }
}
