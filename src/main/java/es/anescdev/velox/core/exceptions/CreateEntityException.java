package es.anescdev.velox.core.exceptions;

/** Error al crear una entidad en el repositorio (fallo de base de datos, no de validación). */
public class CreateEntityException extends RuntimeException {

    /**
     * @param message
     */
    public CreateEntityException(String message) {
        super(message);
    }

}