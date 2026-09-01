package es.anescdev.velox.core.command;

import es.anescdev.velox.app.App;

/**
 * Variante de {@link Command} pensada para comandos que se instancian con {@code new}
 * directamente desde un manejador {@code @FXML} (y por tanto Feather no los construye a
 * través de su propio constructor con {@code @Inject}). En su constructor pide a Feather
 * que inyecte los campos anotados con {@code @Inject} de la instancia ya creada
 * ({@code App.instance().getFeather().injectFields(this)}), simulando así la inyección
 * habitual pese a no pasar por el contenedor.
 */
public abstract class FeatherCommand<R> implements Command<R> {
    /**
     * Debes llamar a este constructor si quieres que el comando inyecte las dependencias marcadas con Inject
     */

    public FeatherCommand() {
        App.instance().getFeather().injectFields(this);
    }
    
}
