package es.anescdev.velox.core.exceptions;

/**
 * Se lanza cuando se intenta crear una entidad que ya existe (violación de una restricción
 * {@code UNIQUE} de SQLite), para poder distinguirlo de un error genérico de creación y
 * mostrar al usuario un mensaje específico ("ya existe") en vez de un error técnico.
 */
public class ExistingEntityException extends RuntimeException {

    /**
     * 
     */
    public ExistingEntityException() {
        super();
    }
    
}
