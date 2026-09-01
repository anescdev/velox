package es.anescdev.velox.core.dto;

import java.util.Optional;

/**
 * @author AnesCDev
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
