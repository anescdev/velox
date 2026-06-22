package es.anescdev.shared.infrastructure.exceptions;

/**
 * @author AnesCDev
 */
public class ColumnNotDefinedException extends RuntimeException {
    public ColumnNotDefinedException(String columnName, Class<?> c) {
        super("The column " + columnName + "isn't defined in class" + c.getName());
    }
}
