package es.anescdev.velox.core.data;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
/**
 * Marca una clase DAO de ORMLite para que {@code DataBaseLoader} la detecte por reflexión
 * en el arranque y cree automáticamente su tabla en SQLite si todavía no existe.
 */
public @interface DaoInitializable {
    
}
