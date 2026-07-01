package es.anescdev.sumatory.model.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

import javax.inject.Inject;

import es.anescdev.core.dto.CreateEntityResult;
import es.anescdev.core.dto.DeleteEntityResult;
import es.anescdev.core.dto.DeleteMultipleEntitiesResult;
import es.anescdev.core.dto.FetchedEntitiesResult;
import es.anescdev.core.exceptions.CreateEntityException;
import es.anescdev.core.exceptions.RemoveEntityException;
import es.anescdev.core.exceptions.SearchEntityException;
import es.anescdev.sumatory.data.repositories.SumatoryRepository;
import es.anescdev.sumatory.model.dto.CreateSumatory;
import es.anescdev.sumatory.model.entities.Sumatory;

/**
 * @author AnesCDev
 */
public class SumatoryService {

    private final SumatoryRepository repository;

    @Inject
    public SumatoryService(SumatoryRepository repository) {
        this.repository = repository;
    }

    public FetchedEntitiesResult<Sumatory> searchSumatories(long lastId, int limit) {
        try {
            return new FetchedEntitiesResult<>(this.repository.searchAll(lastId, limit), this.repository.count());
        } catch (SearchEntityException e) {
            System.err.println(e.getMessage());
            return new FetchedEntitiesResult<>(List.of(), 0);
        }
    }

    public CreateEntityResult<Sumatory> createSumatory(CreateSumatory sumatoryDTO) {
        try {
            var entityCreated = this.repository
                    .createEntity(new Sumatory(sumatoryDTO.month(), sumatoryDTO.year(), sumatoryDTO.employee()));
            return new CreateEntityResult<>(true, "notifications.sumatory.create ", entityCreated);
        } catch (CreateEntityException e) {
            return new CreateEntityResult<>(false, "notifications.sumatory.create.error", null);
        }
    }

    public DeleteEntityResult deleteSumatory(Sumatory sumatory) {
        try {
            this.repository.removeEntityById(sumatory.getId());// TODO: Hacer que devuelva la cantidad borrada
                                                               // directamente del resultado
            return new DeleteEntityResult(true);
        } catch (RemoveEntityException e) {
            return new DeleteEntityResult(false);
        }
    }

    public DeleteMultipleEntitiesResult<Sumatory> deleteSumatories(List<Sumatory> sumatories) {
        Queue<Sumatory> toRemove = new LinkedList<>(sumatories);
        try {
            for (Sumatory sumatory : sumatories) {
                this.repository.removeEntityById(sumatory.getId());
                toRemove.poll();
            }
            return new DeleteMultipleEntitiesResult<>(true, sumatories.size(), Optional.empty());
        } catch (RemoveEntityException e) {
            return new DeleteMultipleEntitiesResult<>(false, sumatories.size() - toRemove.size(),
                    Optional.of(new ArrayList<>(toRemove)));
        }
    }
}
