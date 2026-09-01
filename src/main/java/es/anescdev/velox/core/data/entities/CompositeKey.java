package es.anescdev.velox.core.data.entities;

/**
 * Representa una clave primaria lógica formada por varias columnas (p. ej. {@code CodKey}
 * = empleado + cliente + código). {@code getColumnValue} permite a
 * {@link es.anescdev.velox.core.data.repositories.ORMLiteCompositeKeyAbstractRepository}
 * construir cláusulas {@code WHERE} genéricas sin conocer los campos concretos de cada entidad,
 * y {@code getInternalId} da acceso al id físico autogenerado usado para la paginación.
 *
 * @param <IID> tipo del identificador interno autogenerado de la tabla
 */
public interface CompositeKey<IID> {
    Object getColumnValue(String columnName);
    IID getInternalId();
}
