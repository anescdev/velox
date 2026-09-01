package es.anescdev.velox.core.dto;

import java.util.Optional;

/**
 * Resultado tipado de borrar una única entidad en {@code AbstractService}
 * (éxito/fracaso + mensaje), para que la capa de vista no maneje excepciones directamente.
 * 
 * @param isSuccess indica si se ha borrado con éxito o no
 */
public record DeleteEntityResult(boolean isSuccess, Optional<String> message) {
    public static DeleteEntityResult success(String message) {
        return new DeleteEntityResult(true, Optional.of(message));
    }
    public static DeleteEntityResult success() {
        return new DeleteEntityResult(true, Optional.empty());
    }
    public static DeleteEntityResult failed(String message) {
        return new DeleteEntityResult(false, Optional.of(message));
    }
}
