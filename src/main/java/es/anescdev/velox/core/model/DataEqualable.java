package es.anescdev.velox.core.model;

/**
 * @author AnesCDev
 */
public interface DataEqualable<T> {
    boolean isDataEqual(T other);
}
