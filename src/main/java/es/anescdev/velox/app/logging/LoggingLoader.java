package es.anescdev.velox.app.logging;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;

import es.anescdev.velox.app.App;
import es.anescdev.velox.app.loader.LoadException;
import es.anescdev.velox.app.loader.Loader;
import es.anescdev.velox.app.loader.events.LoadEvent;
import javafx.event.EventType;

/**
 * Loader de arranque encargado de configurar el sistema de logging de la aplicación
 * (nivel, formato de salida, destino) antes de que el resto de loaders empiecen a trabajar.
 */
public class LoggingLoader implements Loader {

    public static final EventType<LoadEvent> LOGGING_LOAD_EVENT_TYPE = new EventType<>("LOGGING");

    @Override
    public void load() throws LoadException {

        try (InputStream loggingConfiguration = this.getClass().getClassLoader()
                .getResourceAsStream("conf/logging.properties")) {
            LogManager.getLogManager().readConfiguration(loggingConfiguration);
            App.instance().initLogger();
        } catch (IOException e) {
            throw new LoadException("load.logging.error.configuration", e.getMessage());
        }
    }

    @Override
    public String getLoadingMessage() {
        return "load.logging.message";
    }

    @Override
    public EventType<LoadEvent> eventType() {
        return LOGGING_LOAD_EVENT_TYPE;
    }

}