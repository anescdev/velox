package es.anescdev.velox.core.utils;

/**
 * Contrato para componentes (típicamente ViewModels, que son singletons) que deben limpiar
 * su estado en memoria al cerrar la sesión del empleado actual. {@code canReset()} permite
 * bloquear el reseteo (y pedir confirmación al usuario) si hay cambios sin guardar.
 * Ver {@link es.anescdev.velox.core.view.commands.BackToEmployeeSelectorCommand}.
 */
public interface Reseteable {
    void reset();

    boolean canReset();
}
