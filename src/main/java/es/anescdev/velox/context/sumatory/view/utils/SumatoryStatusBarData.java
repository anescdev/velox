package es.anescdev.velox.context.sumatory.view.utils;

import java.time.Duration;

import es.anescdev.velox.core.view.components.statusbar.StateStatusBar;
import javafx.beans.value.ObservableValue;
import javafx.util.StringConverter;

/**
 * Utilidad de la capa de vista del dominio sumatory {@literal (conversión de datos para la UI,
 * apertura de pantallas relacionadas, eventos de edición en tablas, etc.)}.
 */
public record SumatoryStatusBarData(
        ObservableValue<String> title,
        ObservableValue<StateStatusBar> sumatoryState,
        ObservableValue<Duration> sumatoryTotalProperty,
        StringConverter<Duration> totalWorkDurationConverter) {

}
