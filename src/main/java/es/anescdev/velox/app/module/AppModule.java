package es.anescdev.velox.app.module;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
/**
 * Marca una clase de configuración de Feather (con métodos {@code @Provides}) como un
 * módulo de la aplicación que debe cargarse automáticamente al arrancar.
 * <p>
 * {@link es.anescdev.velox.app.App#searchAppModules()} escanea por reflexión el paquete
 * {@code es.anescdev.velox.context} buscando, en cada subcarpeta, una clase llamada
 * {@code <Carpeta>Module} anotada con {@code @AppModule}; si la encuentra, la instancia
 * y la añade al contenedor de Feather junto con {@code CoreModule}.
 * Esto permite añadir un módulo de dominio nuevo sin tocar {@code App.java}.
 */
public @interface AppModule {
    
}
