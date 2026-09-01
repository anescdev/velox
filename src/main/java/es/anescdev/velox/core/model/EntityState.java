package es.anescdev.velox.core.model;

/**
 * Estado de una entidad "editable en la UI" antes de persistirse:
 * {@code DRAFT} = creada/modificada en memoria pero no guardada aún,
 * {@code SAVED} = ya reflejada en base de datos. Usado por {@code Cod} y {@code SumatoryEntry},
 * que permiten al usuario preparar varias filas y confirmarlas juntas.
 */
public enum EntityState {
    DRAFT, SAVED;
}
