package es.anescdev.velox.app.loader;

import es.anescdev.velox.app.loader.events.LoadEvent;
import javafx.event.EventType;

/**
 * Contrato para cada paso del proceso de arranque de la aplicación (ver {@code LoadController}).
 * Cada implementación realiza una tarea de inicialización (logging, base de datos, PDF...)
 * y expone un mensaje de progreso y un {@link javafx.event.EventType} propio para notificar
 * cuándo ha terminado.
 */
public interface Loader {
    void load() throws LoadException;

    String getLoadingMessage();
    EventType<LoadEvent> eventType();
}
