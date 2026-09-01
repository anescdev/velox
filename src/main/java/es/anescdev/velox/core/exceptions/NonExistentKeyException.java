package es.anescdev.velox.core.exceptions;

/**
 * @author AnesCDev
 */
public class NonExistentKeyException extends RuntimeException {
    public NonExistentKeyException(String key) {
        super("The key " + key + "don't exist");
    }
}
