package es.anescdev.velox.core.view.utils;

import java.time.Duration;
import java.util.function.Function;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 * Utilidad de la capa de vista del dominio None (conversión de datos para la
 * UI,
 * apertura de pantallas relacionadas, eventos de edición en tablas, etc.).
 */
public class ObservableDurationValueFactory<S>
        implements Callback<CellDataFeatures<S, String>, ObservableValue<String>> {

    private final Function<S, SimpleObjectProperty<Duration>> parseToDuration;
    private final StringConverter<Duration> stringConverter = new DurationStringConverter();

    public ObservableDurationValueFactory(Function<S, SimpleObjectProperty<Duration>> parseToDuration) {
        this.parseToDuration = parseToDuration;
    }

    @Override
    public ObservableValue<String> call(CellDataFeatures<S, String> arg0) {
        var dur = this.parseToDuration.apply(arg0.getValue());
        return dur.map(this.stringConverter::toString);
    }
}
