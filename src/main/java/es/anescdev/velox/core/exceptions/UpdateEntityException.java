package es.anescdev.velox.core.exceptions;

/** Error al actualizar una entidad en el repositorio, o la entidad a actualizar no existe. */
public class UpdateEntityException extends RuntimeException{

    /**
     * @param message
     */
    public UpdateEntityException(String message) {
        super(message);
    }
    
}
