package es.anescdev.velox.context.sumatory.view.utils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import es.anescdev.velox.context.cod.model.entities.Cod;
import es.anescdev.velox.context.sumatory.model.entity.SumatoryEntry;

import lombok.Getter;
import lombok.Setter;

/**
 * @author AnesCDev
 */

@Getter
@Setter
public class GroupedSumatoryEntry {
    private Cod cod;
    private Duration totalTimeWorked = Duration.ZERO;
    private final Map<Byte, SumatoryEntry> daysEntry = new TreeMap<>();
    private final List<Byte> days = new ArrayList<>();
}
