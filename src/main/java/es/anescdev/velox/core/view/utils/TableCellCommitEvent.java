package es.anescdev.velox.core.view.utils;

import java.util.Map;
import java.util.function.BiConsumer;

import es.anescdev.velox.core.model.DataEqualable;
import es.anescdev.velox.core.utils.LombokableBuilder;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.control.TableColumn.CellEditEvent;

/**
 * Utilidad de la capa de vista del dominio None (conversión de datos para la UI,
 * apertura de pantallas relacionadas, eventos de edición en tablas, etc.).
 */
public class TableCellCommitEvent<S extends LombokableBuilder<S> & DataEqualable<S>, T>
        implements EventHandler<CellEditEvent<S, T>> {

    private BiConsumer<S, T> commitEventCallback;
    private final Map<S, TableChangeType> changes;
    private final Map<S, S> originalEntities;

    /**
     * @param commitEventCallback
     */
    public TableCellCommitEvent(BiConsumer<S, T> commitEventCallback, Map<S, TableChangeType> changes,
            Map<S, S> originalEntities) {
        this.commitEventCallback = commitEventCallback;
        this.changes = changes;
        this.originalEntities = originalEntities;
    }

    @Override
    public void handle(CellEditEvent<S, T> event) {
        S row = event.getRowValue();
        S toCompareRow = row.toBuilder().build();
        this.commitEventCallback.accept(toCompareRow, event.getNewValue());
        S originalRow = originalEntities.get(row);
        if (originalRow != null && originalRow.isDataEqual(toCompareRow)) {
            originalEntities.remove(row);
            changes.remove(row);
            this.commitEventCallback.accept(row, event.getNewValue());
            return;
        }
        if (!originalEntities.containsKey(row))
            originalEntities.put(row, row.toBuilder().build());
        this.commitEventCallback.accept(row, event.getNewValue());
        var actualChange = changes.get(row);
        if (actualChange != TableChangeType.ADD_CHANGE)
            Platform.runLater(() -> {
                this.changes.put(row, TableChangeType.UPDATE_CHANGE);
            });
    }

    protected void setCommitEventCallback(BiConsumer<S, T> commitEventCallback) {
        this.commitEventCallback = commitEventCallback;
    }
}
