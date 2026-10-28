package es.anescdev.velox.context.sumatory.model.events;

import es.anescdev.velox.context.sumatory.model.entity.Sumatory;
import es.anescdev.velox.core.events.DataChangedEvent;
import javafx.event.EventType;

public class SumatoryChangeEvent extends DataChangedEvent {
    
    private final Sumatory affectedSumatory;

    /**
     * @param eventType
     * @param affectedSumatory
     */
    public SumatoryChangeEvent(EventType<? extends DataChangedEvent> eventType, Sumatory affectedSumatory) {
        super(eventType);
        this.affectedSumatory = affectedSumatory;
    }

    public Sumatory getAffectedSumatory() {
        return this.affectedSumatory;
    }
    
}