package es.anescdev.velox.context.sumatory.view.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import es.anescdev.velox.context.cod.model.entities.Cod;
import es.anescdev.velox.context.cod.model.services.CodService;
import es.anescdev.velox.context.employee.model.entity.Employee;
import es.anescdev.velox.context.sumatory.model.entity.SumatoryEntry;
import es.anescdev.velox.core.view.utils.TableCellCommitEvent;
import es.anescdev.velox.core.view.utils.TableChangeType;
import javafx.application.Platform;
import javafx.scene.control.TableColumn.CellEditEvent;

/**
 * @author AnesCDev
 */
public class CodEditCommitEvent extends TableCellCommitEvent<SumatoryEntry, String> {

    private final Map<String, CompletableFuture<Optional<Cod>>> cacheCod = new HashMap<>();
    private final CodService codService;
    private final Employee employee;

    public CodEditCommitEvent(CodService codService,
            Map<SumatoryEntry, TableChangeType> changes, Map<SumatoryEntry, SumatoryEntry> originalEntities,
            Employee employee) {
        super(null, changes, originalEntities);
        this.setCommitEventCallback(this::callback);
        this.codService = codService;
        this.employee = employee;
    }

    @Override
    public void handle(CellEditEvent<SumatoryEntry, String> event) {
        if (!event.getNewValue().matches("^([a-zA-Z0-9])+-([a-zA-Z0-9])+$"))
            return;
        String[] codPart = event.getNewValue().split("-");
        var futureCachedCod = this.cacheCod.get(event.getNewValue());
        if (futureCachedCod != null) {
            futureCachedCod.thenAccept(cod -> {
                if (cod.isPresent())
                    super.handle(event);
                if (this.cacheCod.containsKey(event.getNewValue()))
                    this.cacheCod.remove(event.getNewValue());
                event.consume();
                event.getTableView().refresh();
            });
            return;
        }
        var futureCod = new CompletableFuture<Optional<Cod>>();
        this.cacheCod.put(event.getNewValue(), futureCod);
        futureCod.thenAccept(cod -> {
            if (cod.isPresent()) {
                super.handle(event);
                Platform.runLater(() -> {
                    event.getTableView().getItems().set(event.getTablePosition().getRow(), event.getRowValue());
                });
            }
            if (this.cacheCod.containsKey(event.getNewValue()))
                this.cacheCod.remove(event.getNewValue());
            event.consume();
            event.getTableView().refresh();
        });
        Thread.startVirtualThread(() -> {
            futureCod.complete(this.codService.searchCodsByAbbreviation(codPart[0], codPart[1], employee));
        });
    }

    private void callback(SumatoryEntry entry, String completeCod) {
        try {
            var cod = this.cacheCod.get(completeCod).get();
            entry.setCod(cod.get());
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
