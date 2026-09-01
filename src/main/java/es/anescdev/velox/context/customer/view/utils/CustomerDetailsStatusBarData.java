package es.anescdev.velox.context.customer.view.utils;

import es.anescdev.velox.core.view.components.statusbar.StateStatusBar;
import javafx.beans.value.ObservableValue;

/**
 * Utilidad de la capa de vista del dominio customer (conversión de datos para la UI,
 * apertura de pantallas relacionadas, eventos de edición en tablas, etc.).
 */
public record CustomerDetailsStatusBarData(
    ObservableValue<StateStatusBar> state
) {
    
}
