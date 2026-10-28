package es.anescdev.velox.context.sumatory.view.viewmodel;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import javax.inject.Inject;

import es.anescdev.velox.app.App;
import es.anescdev.velox.context.cod.model.entities.Cod;
import es.anescdev.velox.context.cod.model.services.CodService;
import es.anescdev.velox.context.customer.model.entities.Customer;
import es.anescdev.velox.context.sumatory.data.key.SumatoryEntryKey;
import es.anescdev.velox.context.sumatory.model.dto.CreateSumatoryEntry;
import es.anescdev.velox.context.sumatory.model.entity.Sumatory;
import es.anescdev.velox.context.sumatory.model.entity.SumatoryEntry;
import es.anescdev.velox.context.sumatory.model.events.SumatoryChangeEvent;
import es.anescdev.velox.context.sumatory.model.service.SumatoryEntryService;
import es.anescdev.velox.context.sumatory.model.service.SumatoryService;
import es.anescdev.velox.core.events.DataChangedEvent;
import es.anescdev.velox.core.model.EntityState;
import es.anescdev.velox.core.view.utils.NotificationsUtils;
import es.anescdev.velox.core.view.utils.TableChangeType;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

/**
 * ViewModel del dominio sumatory, usado por los controladores para bindear
 * listas
 * observables a la UI (tablas, combos...) sin acceder directamente al
 * {@code Service}.
 * Hereda de {@code AbstractViewModel} la búsqueda paginada, creación y borrado.
 */
public class SumatoryDetailsViewModel {
    public final SimpleStringProperty titleProperty = new SimpleStringProperty("");
    public final SimpleObjectProperty<Sumatory> sumatory = new SimpleObjectProperty<>(null);
    public final SimpleObjectProperty<Duration> total = new SimpleObjectProperty<Duration>(Duration.ZERO);
    public final ObservableList<SumatoryEntry> entities = FXCollections.observableArrayList();
    public final SimpleBooleanProperty canExit = new SimpleBooleanProperty(true);
    public final SimpleBooleanProperty isSaving = new SimpleBooleanProperty(false);
    public final SimpleMapProperty<SumatoryEntry, TableChangeType> changes = new SimpleMapProperty<>(
            FXCollections.observableHashMap());
    public final List<SumatoryEntry> originalEntities = new ArrayList<>();
    public static final Cod EMPTY_COD = Cod.draftCod(-1L, "NEW", new Customer("new", "NEW"), null,
            App.instance().getMessage("sumatoryentry.table.column.emptydescription"), Duration.ZERO);

    public final SumatoryEntryService service;
    private final SumatoryService sumatoryService;
    public final CodService codService;
    private boolean isFirstSearch = true;
    private Consumer<List<SumatoryEntry>> onSearchedEntries;

    /**
     * @param onSearchedEntries the onSearchedEntries to set
     */
    public void setOnSearchedEntries(Consumer<List<SumatoryEntry>> onSearchedEntries) {
        this.onSearchedEntries = onSearchedEntries;
    }

    @Inject
    public SumatoryDetailsViewModel(SumatoryEntryService service, CodService codService,
            SumatoryService sumatoryService) {
        this.service = service;
        this.codService = codService;
        this.sumatoryService = sumatoryService;
        this.canExit.bind(
                this.isSaving.or((this.changes.sizeProperty().lessThan(1))));
        this.entities.addListener((ListChangeListener<SumatoryEntry>) change -> {
            if (isFirstSearch)
                return;
            Duration sum = Duration.ZERO;
            for (SumatoryEntry entry : change.getList()) {
                Duration worked = entry.getCod().getTimeWorked();
                if (worked != null) {
                    sum = sum.plus(worked);
                }
            }
            total.set(sum);

        });

    }

    public void saveCodsChanges() {
        var sumatoryEntriesStream = this.changes.keySet().stream()
                .filter(sumatoryEntry -> this.changes.get(sumatoryEntry) != TableChangeType.DELETE_CHANGE);
        if (sumatoryEntriesStream.anyMatch(this::isInvalidSumatoryEntry)) {
            NotificationsUtils.showError(
                    App.instance().getMessage("notifications.sumatory.error.saving"),
                    App.instance().getMessage("notifications.sumatory.error.invaliddata"));
            return;
        }

        this.isSaving.set(true);
        Thread.startVirtualThread(() -> {
            for (var sumatoryEntryItem : new ArrayList<>(this.changes.entrySet())) {
                var sumatoryEntry = sumatoryEntryItem.getKey();
                switch (sumatoryEntryItem.getValue()) {
                    case ADD_CHANGE:
                        var createResult = this.service
                                .createEntity(new CreateSumatoryEntry(sumatoryEntry.getCod(), this.sumatory.get(),
                                        sumatoryEntry.getDay()));
                        if (createResult.success()) {
                            this.changes.remove(sumatoryEntry);
                            sumatoryEntry.setInternalId(createResult.entity().getInternalId());
                            sumatoryEntry.setState(EntityState.SAVED);
                        } else
                            NotificationsUtils.showError(
                                    App.instance().getMessage("notifications.sumatory.error.saving"),
                                    App.instance().getMessage(createResult.message().get()));
                        break;
                    case DELETE_CHANGE:
                        var deleteResult = this.service.deleteEntity(sumatoryEntry);
                        if (deleteResult.isSuccess())
                            this.changes.remove(sumatoryEntry);
                        else
                            NotificationsUtils.showError(
                                    App.instance().getMessage("notifications.sumatory.error.saving"),
                                    App.instance().getMessage(deleteResult.message().get()));
                        break;
                    case UPDATE_CHANGE:
                        var updateResult = this.service.updateEntity(sumatoryEntry);
                        if (updateResult.isSuccess())
                            this.changes.remove(sumatoryEntry);
                        else
                            NotificationsUtils.showError(
                                    App.instance().getMessage("notifications.sumatory.error.saving"),
                                    App.instance().getMessage(updateResult.message().get()));
                        break;
                    default:
                        break;

                }
            }
            if (this.changes.size() == 0) {
                var actualSumatory = this.sumatory.get();
                actualSumatory.setTotal(this.total.get());
                var finalUpdateResult = this.sumatoryService.update(actualSumatory);
                if (!finalUpdateResult.isSuccess()) {
                    NotificationsUtils.showError(
                            App.instance().getMessage("notifications.sumatory.error.saving"),
                            App.instance().getMessage(finalUpdateResult.message().get()));
                } else {
                    NotificationsUtils.showInformation(
                            App.instance().getMessage("notifications.sumatory.confirmation.title"),
                            App.instance()
                                    .getMessage("notifications.sumatory.confirmation.message"));
                    this.sumatory.get().setTotal(this.total.get());
                    Platform.runLater(
                            () -> App.instance().getMainStage().fireEvent(new SumatoryChangeEvent(DataChangedEvent.CHANGE_EVENT_TYPE, this.sumatory.get())));
                }

            }
            this.isSaving.set(false);
        });
    }

    public SumatoryEntry addSumatoryEntry(SumatoryEntry entry) {
        if (changes.keySet().removeIf(changeSumatoryEntry -> changeSumatoryEntry.isDataEqual(entry))) {
            var originalEntryKey = this.originalEntities.stream()
                    .filter(keyOriginal -> keyOriginal.isDataEqual(entry))
                    .findFirst();
            if (originalEntryKey.isPresent()) {
                this.originalEntities.remove(originalEntryKey.get());
                this.entities.add(originalEntryKey.get());
                return originalEntryKey.get();
            }
            this.entities.add(entry);
            return entry;
        }
        this.entities.add(entry);
        this.changes.put(entry, TableChangeType.ADD_CHANGE);
        return entry;
    }

    public void delete(Collection<SumatoryEntry> toDelete) {
        for (SumatoryEntry selectedEntry : toDelete) {
            this.delete(selectedEntry);
        }
    }

    public void delete(SumatoryEntry toDelete) {
        this.entities.remove(toDelete);
        if (toDelete.isDraft())
            this.changes.remove(toDelete);
        else {
            this.changes.put(toDelete, TableChangeType.DELETE_CHANGE);
            this.originalEntities.add(toDelete);
        }
    }

    private boolean isInvalidSumatoryEntry(SumatoryEntry sumatoryEntry) {
        return sumatoryEntry.getCod() == null || sumatoryEntry.getCod().equals(EMPTY_COD)
                || sumatoryEntry.getSumatory() == null || sumatoryEntry.getDay() < 1
                || sumatoryEntry.getDay() > 31;
    }

    public void searchAllCods() {
        Thread.startVirtualThread(() -> {
            var searchResult = this.service
                    .searchEntity(new SumatoryEntryKey(-1L, new Cod(), this.sumatory.get(), (byte) 1));
            this.entities.addAll(searchResult.entities());
            if (this.onSearchedEntries != null)
                this.onSearchedEntries.accept(searchResult.entities());
            this.isFirstSearch = false;
        });
    }
}
