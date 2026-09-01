package es.anescdev.velox.core.view.utils;

import java.util.Map;
import java.util.function.BiConsumer;

import es.anescdev.velox.core.model.DataEqualable;
import es.anescdev.velox.core.utils.LombokableBuilder;

import javafx.scene.control.TableColumn.CellEditEvent;

/**
 * Utilidad de la capa de vista del dominio None (conversión de datos para la UI,
 * apertura de pantallas relacionadas, eventos de edición en tablas, etc.).
 */
public class NumberTableCellCommitEvent<S extends LombokableBuilder<S> & DataEqualable<S>, T extends Number>
        extends TableCellCommitEvent<S, T> {
    private final T minValue;
    private final T maxValue;

    /**
     * @param commitEventCallback
     */
    public NumberTableCellCommitEvent(BiConsumer<S, T> commitEventCallback, Map<S, TableChangeType> changes,
            Map<S, S> originalEntities, T minValue, T maxValue) {
        super(commitEventCallback, changes, originalEntities);
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    @Override
    public void handle(CellEditEvent<S, T> event) {
        if(this.isValid(event.getNewValue())) super.handle(event);
    }

    private boolean isValid(T newValue) {
        return (this.minValue == null || newValue.doubleValue() >= this.minValue.doubleValue())
                && (this.maxValue == null || newValue.doubleValue() <= this.maxValue.doubleValue());
    }
}
