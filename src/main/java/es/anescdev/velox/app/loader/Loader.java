package es.anescdev.velox.app.loader;

import es.anescdev.velox.app.loader.events.LoadEvent;
import javafx.event.EventType;

/**
 * @author AnesCDev
 */
public interface Loader {
    void load() throws LoadException;

    String getLoadingMessage();
    EventType<LoadEvent> eventType();
}
