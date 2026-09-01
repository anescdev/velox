package es.anescdev.velox.context.sumatory.model.service;

import java.util.function.BiConsumer;

import javax.inject.Inject;

import es.anescdev.velox.context.employee.model.entity.Employee;
import es.anescdev.velox.context.sumatory.data.repositories.SumatoryRepository;
import es.anescdev.velox.context.sumatory.model.dto.CreateSumatory;
import es.anescdev.velox.context.sumatory.model.entity.Sumatory;
import es.anescdev.velox.core.dto.FetchedEntitiesResult;
import es.anescdev.velox.core.dto.UpdateEntityResult;
import es.anescdev.velox.core.exceptions.SearchEntityException;
import es.anescdev.velox.core.exceptions.UpdateEntityException;
import es.anescdev.velox.core.services.AbstractService;
import es.anescdev.velox.core.services.AbstractServiceMessage;


/**
 * @author AnesCDev
 */
public class SumatoryService extends AbstractService<Sumatory, Long, CreateSumatory>{

    @Inject
    public SumatoryService(SumatoryRepository repository) {
        super(repository);
    }

    @Override
    protected void initializeMessages(BiConsumer<AbstractServiceMessage, String> includeMessage) {
        includeMessage.accept(AbstractServiceMessage.CREATE_ENTITY, "notifications.sumatory.create");
        includeMessage.accept(AbstractServiceMessage.CREATE_ENTITY_ERROR, "notifications.sumatory.create.error");
        includeMessage.accept(AbstractServiceMessage.ENTITY_EXISTING_ERROR, "notifications.sumatory.existing.error");
    }

    public UpdateEntityResult update(Sumatory sumatory) {
        try {
            this.repo.updateEntity(sumatory);
            return UpdateEntityResult.success();
        } catch (UpdateEntityException e) {
            return UpdateEntityResult.failed(e.getMessage());
        }
    }
    public FetchedEntitiesResult<Sumatory> searchByEmployee(Employee employee) {
        try {
            var founded = ((SumatoryRepository)this.repo).searchByEmployee(employee);
            return FetchedEntitiesResult.success(founded, founded.size());
        } catch (SearchEntityException e) {
            return FetchedEntitiesResult.failed(e.getMessage());
        }
    }
}
