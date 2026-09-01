package es.anescdev.velox.core.model;

/**
 * Comparación de "igualdad de datos" independiente de {@code equals()}/{@code hashCode()}
 * (pensada para usarse en listas observables de JavaFX, donde a veces interesa comparar
 * por contenido aunque el identificador aún no exista, p. ej. entidades en estado
 * {@link es.anescdev.velox.core.model.EntityState#DRAFT}).
 */
public interface DataEqualable<T> {
    boolean isDataEqual(T other);
}
