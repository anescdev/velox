package es.anescdev.velox.core.events;

import javafx.event.Event;
import javafx.event.EventType;

public class DataChangedEvent extends Event {
    public static EventType<DataChangedEvent> CHANGE_EVENT_TYPE = new EventType<>("CHANGE");
    public static EventType<DataChangedEvent> DELETE_EVENT_TYPE = new EventType<>("DELETE");
    
    public DataChangedEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }
}