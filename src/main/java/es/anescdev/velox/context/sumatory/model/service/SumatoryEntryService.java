package es.anescdev.velox.context.sumatory.model.service;

import java.util.function.BiConsumer;

import javax.inject.Inject;
import javax.inject.Singleton;

import es.anescdev.velox.context.sumatory.data.key.SumatoryEntryKey;
import es.anescdev.velox.context.sumatory.data.repositories.SumatoryEntryRepository;
import es.anescdev.velox.context.sumatory.model.dto.CreateSumatoryEntry;
import es.anescdev.velox.context.sumatory.model.entity.SumatoryEntry;
import es.anescdev.velox.core.dto.UpdateEntityResult;
import es.anescdev.velox.core.exceptions.UpdateEntityException;
import es.anescdev.velox.core.services.AbstractService;
import es.anescdev.velox.core.services.AbstractServiceMessage;

/**
 * @author AnesCDev
 */
@Singleton
public class SumatoryEntryService extends AbstractService<SumatoryEntry, SumatoryEntryKey, CreateSumatoryEntry> {

    @Inject
    public SumatoryEntryService(SumatoryEntryRepository repo) {
         super(repo);
    }

    @Override
    protected void initializeMessages(BiConsumer<AbstractServiceMessage, String> includeMessage) {
        includeMessage.accept(AbstractServiceMessage.CREATE_ENTITY, "notifications.sumatoryentry.create");
        includeMessage.accept(AbstractServiceMessage.CREATE_ENTITY_ERROR, "notifications.sumatoryentry.create.error");
        includeMessage.accept(AbstractServiceMessage.ENTITY_EXISTING_ERROR, "notifications.sumatoryentry.existing.error");
    }

    public UpdateEntityResult updateEntity(SumatoryEntry entity) {
        try {
            this.repo.updateEntity(entity);
            return UpdateEntityResult.success();
        } catch (UpdateEntityException e) {
            return UpdateEntityResult.failed(e.getMessage());
        }
    }

}
