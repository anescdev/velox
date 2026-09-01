package es.anescdev.velox.context.sumatory.view.utils;

import java.time.Duration;

import es.anescdev.velox.core.view.components.statusbar.StateStatusBar;
import javafx.beans.value.ObservableValue;
import javafx.util.StringConverter;

/**
 * @author AnesCDev
 */
public record SumatoryStatusBarData(
        ObservableValue<String> title,
        ObservableValue<StateStatusBar> sumatoryState,
        ObservableValue<Duration> sumatoryTotalProperty,
        StringConverter<Duration> totalWorkDurationConverter) {

}
