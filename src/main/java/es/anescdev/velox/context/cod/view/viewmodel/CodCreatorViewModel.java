package es.anescdev.velox.context.cod.view.viewmodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import es.anescdev.velox.app.App;
import es.anescdev.velox.context.cod.model.dto.CreateCod;
import es.anescdev.velox.context.cod.model.entities.Cod;
import es.anescdev.velox.context.cod.model.entities.CodKey;
import es.anescdev.velox.context.cod.model.services.CodService;
import es.anescdev.velox.context.customer.model.entities.Customer;
import es.anescdev.velox.context.employee.model.entity.Employee;
import es.anescdev.velox.core.model.EntityState;
import es.anescdev.velox.core.view.components.ConfirmationDialog;
import es.anescdev.velox.core.view.utils.NotificationsUtils;
import es.anescdev.velox.core.view.utils.TableChangeType;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ButtonType;

/**
 * ViewModel del dominio cod, usado por los controladores para bindear listas
 * observables a la UI (tablas, combos...) sin acceder directamente al {@code Service}.
 * Hereda de {@code AbstractViewModel} la búsqueda paginada, creación y borrado.
 */
public class CodCreatorViewModel {
    public final ObservableList<Cod> entities = FXCollections.observableArrayList();
    public final SimpleBooleanProperty canExit = new SimpleBooleanProperty(true);
    public final SimpleBooleanProperty isSaving = new SimpleBooleanProperty(false);
    public final SimpleObjectProperty<Customer> customer = new SimpleObjectProperty<>(null);
    public final SimpleObjectProperty<Employee> employee = new SimpleObjectProperty<>(null);
    public final SimpleMapProperty<Cod, TableChangeType> changes = new SimpleMapProperty<>(
            FXCollections.observableHashMap());
    public final Map<Cod, Cod> originalEntities = new HashMap<>();

    private final CodService service;

    @Inject
    public CodCreatorViewModel(CodService codService) {
        this.service = codService;
        this.canExit.bind(
                this.isSaving.or((this.changes.sizeProperty().lessThan(1))));
    }

    public void saveCodsChanges() {
        var toDeleteCount = this.changes.values().stream().filter(this::isDeleteChange).count();
        if (toDeleteCount > 0) {
            var alert = new ConfirmationDialog(App.instance().getMessage("cod.dialog.remove.title"),
                    toDeleteCount > 1 ? App.instance().getMessage("cod.dialog.remove.text.multiple")
                            : App.instance().getMessage("cod.dialog.remove.text"));
            var userConfirmation = alert.showAndWait();
            userConfirmation.filter(buttonType -> buttonType.equals(ButtonType.OK));
            if (userConfirmation.isEmpty())
                return;
        }
        var codsStream = this.changes.keySet().stream()
                .filter(cod -> this.changes.get(cod) != TableChangeType.DELETE_CHANGE);
        if (codsStream.anyMatch(this::isInvalidCod)) {
            NotificationsUtils.showError(
                    App.instance().getMessage("notifications.cod.error.saving"),
                    App.instance().getMessage("notifications.cod.error.invaliddata"));
            return;
        }

        this.isSaving.set(true);
        Thread.startVirtualThread(() -> {
            for (var codEntry : new ArrayList<>(this.changes.entrySet())) {
                var cod = codEntry.getKey();
                switch (codEntry.getValue()) {
                    case ADD_CHANGE:
                        var createResult = this.service
                                .createEntity(new CreateCod(cod.getId(), cod.getDescription(), cod.getTimeWorked()));
                        if (createResult.success()) {
                            this.changes.remove(cod);
                            cod.setInternalId(createResult.entity().getInternalId());
                            cod.setState(EntityState.SAVED);
                        } else
                            NotificationsUtils.showError(
                                    App.instance().getMessage("notifications.cod.error.saving"),
                                    App.instance().getMessage(createResult.message().get()));
                        break;
                    case DELETE_CHANGE:
                        var deleteResult = this.service.deleteEntity(cod);
                        if (deleteResult.isSuccess())
                            this.changes.remove(cod);
                        else
                            NotificationsUtils.showError(
                                    App.instance().getMessage("notifications.cod.error.saving"),
                                    App.instance().getMessage(deleteResult.message().get()));
                        break;
                    case UPDATE_CHANGE:
                        var updateResult = this.service.updateEntity(cod);
                        if (updateResult.isSuccess())
                            this.changes.remove(cod);
                        else
                            NotificationsUtils.showError(
                                    App.instance().getMessage("notifications.cod.error.saving"),
                                    App.instance().getMessage(updateResult.message().get()));
                        break;
                    default:
                        break;

                }
            }
            if (this.changes.size() == 0) {
                NotificationsUtils.showInformation(
                        App.instance().getMessage("notifications.entity.confirmation.saved"),
                        App.instance().getMessage("notifications.cod.saved.message"));
            }
            Platform.runLater(() -> this.isSaving.set(false));
        });
    }

    public void addCod(Cod cod) {
        this.entities.add(cod);
        this.changes.put(cod, TableChangeType.ADD_CHANGE);
    }

    public void delete(List<Cod> toDelete) {
        for (Cod selectedCod : toDelete) {
            this.entities.remove(selectedCod);
            if (selectedCod.isDraft())
                this.changes.remove(selectedCod);
            else
                this.changes.put(selectedCod, TableChangeType.DELETE_CHANGE);

        }
    }

    private boolean isInvalidCod(Cod cod) {
        return cod.getCod() == null || cod.getCod().isBlank()
                || cod.getDescription() == null || cod.getDescription().isBlank()
                || cod.getTimeWorked() == null;
    }

    private boolean isDeleteChange(TableChangeType changeType) {
        return changeType == TableChangeType.DELETE_CHANGE;
    }

    public void searchAllCods() {
        Thread.startVirtualThread(() -> {
            var searchResult = this.service
                    .searchEntity(new CodKey(-1L, "", this.employee.get(), this.customer.get()));
            this.entities.addAll(searchResult.entities());
        });
    }
}
