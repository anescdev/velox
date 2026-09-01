package es.anescdev.velox.core.view.viewmodel;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import es.anescdev.velox.app.App;
import es.anescdev.velox.core.data.entities.Identificable;
import es.anescdev.velox.core.exceptions.CreateEntityException;
import es.anescdev.velox.core.mapper.ToEntityMapper;
import es.anescdev.velox.core.services.AbstractService;
import es.anescdev.velox.core.utils.Reseteable;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @author AnesCDev
 */
public abstract class AbstractViewModel<T extends Identificable<ID>, ID, SERVICE extends AbstractService<T, ID, C>, C extends ToEntityMapper<T, ID>>
        implements Reseteable {
    public final ObservableList<T> entities = FXCollections.observableArrayList();
    protected final SimpleObjectProperty<ID> lastId;
    protected final SimpleLongProperty count = new SimpleLongProperty(0);
    protected final SimpleIntegerProperty limit = new SimpleIntegerProperty(20);

    protected final SERVICE service;

    public AbstractViewModel(SERVICE service) {
        this.service = service;
        this.lastId = new SimpleObjectProperty<>(this.initialLastId());
    }

    public void searchEntities() {
        Thread.startVirtualThread(() -> {
            var result = this.service.searchEntity(lastId.get(), limit.get());
            Platform.runLater(() -> {
                if (!this.entities.isEmpty())
                    this.entities.clear();
                this.entities.addAll(result.entities());
                this.count.set(result.totalEntities());
            });
        });
    }

    public CompletableFuture<T> createEntity(C createEntityDTO) {
        CompletableFuture<T> entity = new CompletableFuture<>();
        Thread.startVirtualThread(() -> {
            var result = this.service.createEntity(createEntityDTO);
            if (result.success()) {
                entity.complete(result.entity());
            } else {
                entity.completeExceptionally(new CreateEntityException(result.message().get()));
            }
        });
        return entity;
    }

    public void deleteEntities(List<T> entities) {
        if (entities.isEmpty())
            return;
        Thread.startVirtualThread(() -> {
            if (entities.size() == 1) {
                var first = entities.getFirst();
                if (this.service.deleteEntity(first).isSuccess()) {
                    Platform.runLater(() -> this.entities.remove(first));
                }
            } else {
                if (this.service.deleteEntities(entities).success()) {
                    Platform.runLater(() -> entities.forEach(sumatory -> this.entities.remove(sumatory)));
                }
            }
        });
    }

    @Override
    public void reset() {
        this.entities.clear();
        App.instance().getLogger().info("Reseted " + this.getClass().getSimpleName());
    }

    @Override
    public boolean canReset() {
        return true;
    }

    public abstract ID initialLastId();

}
