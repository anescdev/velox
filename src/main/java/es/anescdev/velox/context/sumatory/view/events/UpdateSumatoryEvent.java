package es.anescdev.velox.context.sumatory.view.events;

import es.anescdev.velox.context.sumatory.model.entity.Sumatory;
import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

public class UpdateSumatoryEvent extends Event {

    public static EventType<UpdateSumatoryEvent> EVENT_TYPE = new EventType<>("update-sumatory");

    private final Sumatory updatedSumatory;

    public UpdateSumatoryEvent(Sumatory updatedSumatory) {
        super(EVENT_TYPE);
        this.updatedSumatory = updatedSumatory;
    }

    public UpdateSumatoryEvent(Object source, EventTarget target, Sumatory updatedSumatory) {
        super(source, target, EVENT_TYPE);
        this.updatedSumatory = updatedSumatory;
    }

    public Sumatory getSumatory() {
        return this.updatedSumatory;
    }

}