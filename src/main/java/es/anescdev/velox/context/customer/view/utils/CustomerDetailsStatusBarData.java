package es.anescdev.velox.context.customer.view.utils;

import es.anescdev.velox.core.view.components.statusbar.StateStatusBar;
import javafx.beans.value.ObservableValue;

/**
 * @author AnesCDev
 */
public record CustomerDetailsStatusBarData(
    ObservableValue<StateStatusBar> state
) {
    
}
