package es.anescdev.velox.app.loader;

/**
 * Excepción específica del proceso de arranque (ver {@link es.anescdev.velox.app.loader.Loader}).
 * Si un {@code Loader} falla durante el arranque, la aplicación muestra un diálogo de error
 * con la clave de mensaje indicada y se cierra.
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
