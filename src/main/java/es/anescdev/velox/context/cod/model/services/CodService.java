package es.anescdev.velox.context.cod.model.services;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

import javax.inject.Inject;
import javax.inject.Singleton;

import es.anescdev.velox.app.App;
import es.anescdev.velox.context.cod.data.repositories.CodRepository;
import es.anescdev.velox.context.cod.model.dto.CreateCod;
import es.anescdev.velox.context.cod.model.entities.Cod;
import es.anescdev.velox.context.cod.model.entities.CodKey;
import es.anescdev.velox.context.employee.model.entity.Employee;
import es.anescdev.velox.core.dto.CreateEntityResult;
import es.anescdev.velox.core.dto.UpdateEntityResult;
import es.anescdev.velox.core.exceptions.SearchEntityException;
import es.anescdev.velox.core.exceptions.UpdateEntityException;
import es.anescdev.velox.core.model.EntityState;
import es.anescdev.velox.core.services.AbstractService;
import es.anescdev.velox.core.services.AbstractServiceMessage;

@Singleton
/**
 * Servicio de negocio del dominio cod. Hereda el CRUD genérico de
 * {@code AbstractService} y solo define los mensajes de usuario (éxito/error)
 * propios
 * de este dominio, salvo que añada alguna regla de negocio adicional.
 */
public class CodService extends AbstractService<Cod, CodKey, CreateCod> {

    @Inject
    public CodService(CodRepository repo) {
        super(repo);
    }

    @Override
    protected void initializeMessages(BiConsumer<AbstractServiceMessage, String> includeMessage) {
        includeMessage.accept(AbstractServiceMessage.CREATE_ENTITY, "notifications.cod.create");
        includeMessage.accept(AbstractServiceMessage.CREATE_ENTITY_ERROR, "notifications.cod.create.error");
        includeMessage.accept(AbstractServiceMessage.ENTITY_EXISTING_ERROR, "notifications.cod.existing.error");
    }

    @Override
    public CreateEntityResult<Cod> createEntity(CreateCod entityDTO) {
        var cod = super.createEntity(entityDTO);
        if (cod.success())
            cod.entity().setState(EntityState.SAVED);
        return cod;
    }

    public Optional<Cod> searchCodByAbbreviation(String abbreviation, String cod, Employee employee) {
        try {
            return ((CodRepository) this.repo).searchEntityByAbbreviationAndCod(abbreviation, cod, employee);
        } catch (SearchEntityException e) {
            App.instance().getLogger().severe(e.getMessage());
            return Optional.empty();
        }
    }

    public List<Cod> searchCodsByDescription(String description, Employee employee) {
        try {
            return ((CodRepository) this.repo).searchEntitiesByDescription(description.trim(), employee);
        } catch (SearchEntityException e) {
            App.instance().getLogger().severe(e.getMessage());
            return List.of();
        }
    }

    public List<Cod> searchCods(String abbreviation, String cod, Employee employee) {
        try {
            return ((CodRepository) this.repo).searchEntitiesByCod(abbreviation, cod, employee);
        } catch (SearchEntityException e) {
            App.instance().getLogger().severe(e.getMessage());
            return List.of();
        }
    }

    public UpdateEntityResult updateEntity(Cod cod) {
        try {
            this.repo.updateEntity(cod);
            return UpdateEntityResult.success();
        } catch (UpdateEntityException e) {
            return UpdateEntityResult.failed(e.getMessage());
        }
    }

}
