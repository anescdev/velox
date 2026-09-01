package es.anescdev.velox.core.dto;

import java.util.Optional;

/**
 * @author AnesCDev
 */
public record UpdateEntityResult(boolean isSuccess, Optional<String> message) {
    public static UpdateEntityResult success() {
        return new UpdateEntityResult(true, Optional.empty());
    }
    public static UpdateEntityResult failed(String message) {
        return new UpdateEntityResult(false, Optional.of(message));
    }
}
