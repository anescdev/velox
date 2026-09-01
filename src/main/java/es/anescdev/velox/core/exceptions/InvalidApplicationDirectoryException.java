package es.anescdev.velox.core.exceptions;

/**
 * El directorio de datos de la aplicación indicado (por defecto {@code ~/.velox}, o el
 * pasado por el argumento {@code app-dir}) no existe o no tiene permisos de lectura/escritura.
 */
public class InvalidApplicationDirectoryException extends RuntimeException {
    public InvalidApplicationDirectoryException(String message) {
        super(message);
    }

}