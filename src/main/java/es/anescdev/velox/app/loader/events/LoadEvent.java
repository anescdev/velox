package es.anescdev.velox.app.loader.events;

import javafx.event.Event;
import javafx.event.EventType;

/**
 * @author AnesCDev
 */
public class LoadEvent extends Event {

    public static final EventType<? extends LoadEvent> COMPLETED_EVENT = new EventType<>("completed");

    public LoadEvent(EventType<? extends LoadEvent> eventType) {
        super(eventType);
    }

}
