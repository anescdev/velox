package es.anescdev.velox.core.utils;

import javafx.util.Builder;

/**
 * Puente entre el {@code Builder} generado por Lombok ({@code @Builder}) y
 * {@link javafx.util.Builder} de JavaFX, para poder reconstruir una copia editable
 * de una entidad (p. ej. pasar de {@code SAVED} a una versión {@code DRAFT} modificada)
 * a través de {@code toBuilder()}.
 */
public interface LombokableBuilder<T> {
    Builder<T> toBuilder();
}
