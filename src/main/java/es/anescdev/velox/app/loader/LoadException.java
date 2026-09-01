package es.anescdev.velox.app.loader;

/**
 * @author AnesCDev
 */
public class LoadException extends RuntimeException {

    private final String errorKey;

    /**
     * @param details
     */
    public LoadException(String errorKey, String details) {
        super(details);
        this.errorKey = errorKey;
    }

    public String getErrorKey() {
        return this.errorKey;
    }

}
