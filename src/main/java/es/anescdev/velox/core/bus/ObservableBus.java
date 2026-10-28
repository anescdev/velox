package es.anescdev.velox.core.bus;

import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;

public class ObservableBus extends Bus {

    public ObservableBus() {
        super(new SimpleMapProperty<>(FXCollections.observableHashMap()));
    }

    private SimpleMapProperty<BusKey<?>, Object> getObservableMap() {
        return (SimpleMapProperty<BusKey<?>, Object>) this.busData;
    }

    public ReadOnlyIntegerProperty sizeProperty() {
        return this.getObservableMap().sizeProperty();
    }

}