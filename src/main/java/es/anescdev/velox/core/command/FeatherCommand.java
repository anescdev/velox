package es.anescdev.velox.core.command;

import es.anescdev.velox.app.App;

/**
 * Clase especial que permite usar el Feather IOC para inyectar dependencias con @Inject en los campos
 * @author AnesCDev
 */
public abstract class FeatherCommand<R> implements Command<R> {
    /**
     * Debes llamar a este constructor si quieres que el comando inyecte las dependencias marcadas con Inject
     */

    public FeatherCommand() {
        App.instance().getFeather().injectFields(this);
    }
    
}
