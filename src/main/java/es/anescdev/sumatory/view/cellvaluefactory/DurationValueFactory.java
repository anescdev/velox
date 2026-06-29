package es.anescdev.sumatory.view.cellvaluefactory;

import java.time.Duration;
import java.util.function.Function;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

public class DurationValueFactory<S> implements Callback<CellDataFeatures<S, String>, ObservableValue<String>>{

    private final Function<S, Duration> parseToDuration;
    
    

    public DurationValueFactory(Function<S, Duration> parseToDuration) {
        this.parseToDuration = parseToDuration;
    }



    @Override
    public ObservableValue<String> call(CellDataFeatures<S, String> arg0) {
        Duration dur = this.parseToDuration.apply(arg0.getValue());
        var hours = dur.toHours();
        var secs = dur.getSeconds();
        return new ReadOnlyStringWrapper(
            (hours < 10 ? "0" : "") + hours + ":" + (secs < 10 ? "0" : "") + dur.getSeconds());
    }
    
}
