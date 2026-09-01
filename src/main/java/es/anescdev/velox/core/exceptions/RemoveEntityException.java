package es.anescdev.velox.core.exceptions;

/** Error al borrar una entidad en el repositorio, o la entidad a borrar no existe. */
public class RemoveEntityException extends RuntimeException {

    /**
     * @param message
     */
    public RemoveEntityException(String message) {
        super(message);
    }
    
}