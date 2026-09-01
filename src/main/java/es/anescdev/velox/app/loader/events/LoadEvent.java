package es.anescdev.velox.app.loader.events;

import javafx.event.Event;
import javafx.event.EventType;

/**
 * Evento de JavaFX usado para comunicar el progreso del arranque (un evento por cada
 * {@link es.anescdev.velox.app.loader.Loader} completado, más {@link #COMPLETED_EVENT}
 * cuando termina todo el proceso).
 */
public class LoadEvent extends Event {

    public static final EventType<? extends LoadEvent> COMPLETED_EVENT = new EventType<>("completed");

    public LoadEvent(EventType<? extends LoadEvent> eventType) {
        super(eventType);
    }

}
