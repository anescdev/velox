package es.anescdev.velox.core.data.entities;

/**
 * @author AnesCDev
 */
public interface CompositeKey<IID> {
    Object getColumnValue(String columnName);
    IID getInternalId();
}
