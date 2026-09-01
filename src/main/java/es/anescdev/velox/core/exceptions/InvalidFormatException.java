package es.anescdev.velox.core.exceptions;

/** El valor introducido por el usuario no tiene el formato esperado (p. ej. al parsear una duración). */
public class InvalidFormatException extends RuntimeException {

    /**
     * @param message
     */
    public InvalidFormatException(String message) {
        super(message);
    }

}
