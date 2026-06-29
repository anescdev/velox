package es.anescdev.sumatory.viewmodel;

import java.util.List;

import javax.inject.Inject;

import es.anescdev.sumatory.model.dto.CreateSumatory;
import es.anescdev.sumatory.model.entities.Sumatory;
import es.anescdev.sumatory.model.service.SumatoryService;

import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import lombok.AccessLevel;
import lombok.Getter;

@Getter
public class SumatoryListViewModel {
    public final ObservableList<Sumatory> sumatories = FXCollections.observableArrayList();
    private final SimpleLongProperty lastId = new SimpleLongProperty(-1);
    private final SimpleLongProperty count = new SimpleLongProperty(0);
    private final SimpleIntegerProperty limit = new SimpleIntegerProperty(20);

    @Getter(AccessLevel.NONE)
    private final SumatoryService service;

    /**
     * @param service
     */
    @Inject
    public SumatoryListViewModel(SumatoryService service) {
        this.service = service;
    }

    public void searchSumatories() {
        Thread.startVirtualThread(() -> {
            var result = service.searchSumatories(lastId.get(), limit.get());
            Platform.runLater(() -> {
                sumatories.addAll(result.sumatories());
                count.set(result.totalEntities());
            });
        });
    }

    public void createSumatory(CreateSumatory sumatoryDTO) {
        Thread.startVirtualThread(() -> {
            var result = this.service.createSumatory(sumatoryDTO);
            if (result.success())
                Platform.runLater(() -> this.sumatories.add(result.entity()));
        });
    }

    public void deleteSumatories(List<Sumatory> sumatories) {
        if (sumatories.isEmpty())
            return;
        Thread.startVirtualThread(() -> {
            if (sumatories.size() == 1) {
                var first = sumatories.getFirst();
                if (this.service.deleteSumatory(first).success()) {
                    Platform.runLater(() -> this.sumatories.remove(first));
                }
            } else {
                if (this.service.deleteSumatories(sumatories).success()) {
                    Platform.runLater(() -> this.sumatories.removeAll(sumatories));
                }
            }
        });
    }
}