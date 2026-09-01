package es.anescdev.velox.core.exceptions;

/** Error al crear la tabla de una entidad en el arranque de la aplicación. */
public class SetupRepositoryException extends RuntimeException{

    /**
     * @param message
     */
    public SetupRepositoryException(String message) {
        super(message);
    }
    
}
