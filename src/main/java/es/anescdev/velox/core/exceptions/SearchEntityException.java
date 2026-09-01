package es.anescdev.velox.core.exceptions;

/** Error al ejecutar una consulta de búsqueda contra el repositorio. */
public class SearchEntityException extends RuntimeException {

    /**
     * @param message
     */
    public SearchEntityException(String message) {
        super(message);
    }

}
