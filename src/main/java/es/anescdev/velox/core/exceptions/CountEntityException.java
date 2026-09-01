package es.anescdev.velox.core.exceptions;

/** Error al contar el número de entidades en el repositorio. */
public class CountEntityException extends RuntimeException {

    /**
     * @param message
     */
    public CountEntityException(String message) {
        super(message);
    }

}
