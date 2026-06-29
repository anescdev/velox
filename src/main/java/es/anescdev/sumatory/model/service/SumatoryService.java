package es.anescdev.sumatory.model.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

import javax.inject.Inject;

import es.anescdev.core.exceptions.CreateEntityException;
import es.anescdev.core.exceptions.RemoveEntityException;
import es.anescdev.core.exceptions.SearchEntityException;
import es.anescdev.sumatory.data.repositories.SumatoryRepository;
import es.anescdev.sumatory.model.dto.CreateSumatory;
import es.anescdev.sumatory.model.dto.CreateSumatoryResult;
import es.anescdev.sumatory.model.dto.DeleteMultipleSumatoryResult;
import es.anescdev.sumatory.model.dto.DeleteSumatoryResult;
import es.anescdev.sumatory.model.dto.FetchedSumatoriesResult;
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

    public FetchedSumatoriesResult searchSumatories(long lastId, int limit) {
        try {
            return new FetchedSumatoriesResult(this.repository.searchAll(lastId, limit), this.repository.count());
        } catch (SearchEntityException e) {
            System.err.println(e.getMessage());
            return new FetchedSumatoriesResult(List.of(), 0);
        }
    }

    public CreateSumatoryResult createSumatory(CreateSumatory sumatoryDTO) {
        try {
            var entityCreated = this.repository
                    .createEntity(new Sumatory(sumatoryDTO.month(), sumatoryDTO.year(), sumatoryDTO.employee()));
            return new CreateSumatoryResult(true, "notifications.sumatory.create ", entityCreated);
        } catch (CreateEntityException e) {
            return new CreateSumatoryResult(false, "notifications.sumatory.create.error", null);
        }
    }

    public DeleteSumatoryResult deleteSumatory(Sumatory sumatory) {
        try {
            this.repository.removeEntityById(sumatory.getId());// TODO: Hacer que devuelva la cantidad borrada
                                                               // directamente del resultado
            return new DeleteSumatoryResult(true);
        } catch (RemoveEntityException e) {
            return new DeleteSumatoryResult(false);
        }
    }

    public DeleteMultipleSumatoryResult deleteSumatories(List<Sumatory> sumatories) {
        Queue<Sumatory> toRemove = new LinkedList<>(sumatories);
        try {
            for (Sumatory sumatory : sumatories) {
                this.repository.removeEntityById(sumatory.getId());
                toRemove.poll();
            }
            return new DeleteMultipleSumatoryResult(true, sumatories.size(), Optional.empty());
        } catch (RemoveEntityException e) {
            return new DeleteMultipleSumatoryResult(false, sumatories.size() - toRemove.size(),
                    Optional.of(new ArrayList<>(toRemove)));
        }
    }
}
