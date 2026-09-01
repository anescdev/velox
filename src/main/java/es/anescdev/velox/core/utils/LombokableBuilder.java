package es.anescdev.velox.core.utils;

import javafx.util.Builder;

/**
 * @author AnesCDev
 */
public interface LombokableBuilder<T> {
    Builder<T> toBuilder();
}
