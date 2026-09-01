package es.anescdev.velox.core.dto;

import java.util.Optional;

/**
 * Resultado tipado de una operación de actualización en {@code AbstractService}
 * (éxito/fracaso + mensaje), análogo a {@link CreateEntityResult}.
 */
public record UpdateEntityResult(boolean isSuccess, Optional<String> message) {
    public static UpdateEntityResult success() {
        return new UpdateEntityResult(true, Optional.empty());
    }
    public static UpdateEntityResult failed(String message) {
        return new UpdateEntityResult(false, Optional.of(message));
    }
}
