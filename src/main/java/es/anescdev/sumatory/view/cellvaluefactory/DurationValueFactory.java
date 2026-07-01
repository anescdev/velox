package es.anescdev.sumatory.view.cellvaluefactory;

import java.time.Duration;
import java.util.function.Function;

import es.anescdev.sumatory.view.utils.SumatoryUtils;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

public class DurationValueFactory<S> implements Callback<CellDataFeatures<S, String>, ObservableValue<String>> {

    private final Function<S, Duration> parseToDuration;

    public DurationValueFactory(Function<S, Duration> parseToDuration) {
        this.parseToDuration = parseToDuration;
    }

    @Override
    public ObservableValue<String> call(CellDataFeatures<S, String> arg0) {
        Duration dur = this.parseToDuration.apply(arg0.getValue());
        return new ReadOnlyStringWrapper(
                SumatoryUtils.parseDuration(dur));
    }

}
