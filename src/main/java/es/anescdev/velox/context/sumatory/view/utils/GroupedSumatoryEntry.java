package es.anescdev.velox.context.sumatory.view.utils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import es.anescdev.velox.context.cod.model.entities.Cod;
import es.anescdev.velox.context.sumatory.model.entity.SumatoryEntry;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.collections.WeakMapChangeListener;
import lombok.Getter;
import lombok.Setter;

/**
 * @author AnesCDev
 */

@Getter
@Setter
public class GroupedSumatoryEntry {
    private Cod cod;
    private SimpleObjectProperty<Duration> totalTimeWorked = new SimpleObjectProperty<Duration>(Duration.ZERO);
    private final Map<Byte, SumatoryEntry> daysEntry;
    private final List<Byte> days = new ArrayList<>();

    private final MapChangeListener<Byte, SumatoryEntry> onDayModifiedListener;

    public GroupedSumatoryEntry() {
        ObservableMap<Byte, SumatoryEntry> observableMap = FXCollections.observableMap(new TreeMap<>());
        this.daysEntry = observableMap;
        this.onDayModifiedListener = (MapChangeListener<Byte, SumatoryEntry>) change -> this.recalculateTotal();
        observableMap.addListener(new WeakMapChangeListener<>(this.onDayModifiedListener));
    }

    private void recalculateTotal() {
        this.totalTimeWorked
                    .set(this.cod.getTimeWorked().multipliedBy(this.daysEntry.size()));
    }

    public void setCod(Cod cod) {
        this.cod = cod;
        if(cod != null) {
            this.recalculateTotal();
        }
    }

}
