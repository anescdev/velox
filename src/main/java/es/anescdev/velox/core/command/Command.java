package es.anescdev.velox.core.command;

/**
 * Contrato mínimo del patrón Command usado en toda la capa de vista: encapsula una
 * acción de usuario (crear un cliente, generar un PDF, etc.) en un objeto ejecutable,
 * en vez de resolverla directamente en el manejador {@code @FXML} del botón.
 *
 * @param <R> tipo de resultado que produce la acción (puede ser {@code Void})
 */
public interface Command<R> {
    public R executeCommand();
}
