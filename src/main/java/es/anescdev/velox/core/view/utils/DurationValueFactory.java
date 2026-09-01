package es.anescdev.velox.core.view.utils;

import java.time.Duration;
import java.util.function.Function;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 * Utilidad de la capa de vista del dominio None (conversión de datos para la UI,
 * apertura de pantallas relacionadas, eventos de edición en tablas, etc.).
 */
public class DurationValueFactory<S> implements Callback<CellDataFeatures<S, String>, ObservableValue<String>> {

    private final Function<S, Duration> parseToDuration;
    private final StringConverter<Duration> stringConverter = new DurationStringConverter();

    public DurationValueFactory(Function<S, Duration> parseToDuration) {
        this.parseToDuration = parseToDuration;
    }

    @Override
    public ObservableValue<String> call(CellDataFeatures<S, String> arg0) {
        Duration dur = this.parseToDuration.apply(arg0.getValue());
        return new ReadOnlyStringWrapper(
                this.stringConverter.toString(dur));
    }

}
