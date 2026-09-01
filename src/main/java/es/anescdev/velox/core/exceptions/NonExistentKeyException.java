package es.anescdev.velox.core.exceptions;

/**
 * Se lanza cuando se pide el valor de una columna que no forma parte de una
 * {@link es.anescdev.velox.core.data.entities.CompositeKey} concreta
 * (ver {@code CompositeKey#getColumnValue(String)}).
 */
public class NonExistentKeyException extends RuntimeException {
    public NonExistentKeyException(String key) {
        super("The key " + key + "don't exist");
    }
}
