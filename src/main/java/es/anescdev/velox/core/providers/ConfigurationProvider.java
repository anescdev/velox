package es.anescdev.velox.core.providers;

import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;

import es.anescdev.velox.core.bus.BusKey;

/**
 * Almacén de configuración de la aplicación en memoria (directorio de datos,
 * nombre de la
 * base de datos...), tipado mediante {@link ConfigurationKey} para evitar
 * errores de casteo.
 * Se rellena una vez en el arranque a partir de los argumentos de lanzamiento
 * (ver {@link es.anescdev.velox.core.CoreModule#includeInitialConfiguration}).
 */
public class ConfigurationProvider {
    protected final Map<ConfigurationKey<?>, Object> configurations;

    public ConfigurationProvider(
            Map<ConfigurationKey<?>, Object> initialValues) {
        this.configurations = new HashMap<>(initialValues);
    }

    public ConfigurationProvider() {
        this(new HashMap<>());
    }

    /**
     * Enum que contiene las claves existentes para la configuración. Importante
     * usar las que están disponibles y si
     * en un futuro es necesario otras nuevas, incluirlas en esta.
     * 
     */

    public <T> T get(ConfigurationKey<T> key)
            throws MissingResourceException {
        if (!this.configurations.containsKey(key)) {
            throw new MissingResourceException("The key isn't set", ConfigurationProvider.class.getName(),
                    key.getName());
        }
        return key.getType().cast(this.configurations.get(key));
    }

    public boolean has(ConfigurationKey<?> key) {
        return this.configurations.containsKey(key);
    }

    public <T> void set(ConfigurationKey<T> key, T value) {
        if (key == null || value == null)
            throw new IllegalArgumentException("The key or value cannot be null");
        this.configurations.put(key, value);
    }

    public void remove(ConfigurationKey<?> key) {
        this.configurations.remove(key);
    }

    public static class ConfigurationKey<T> extends BusKey<T> {
        public ConfigurationKey(String name, Class<T> type) {
            super(name, type);
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((this.key() == null) ? 0 : this.key().hashCode());
            result = prime * result + ((this.keyClass() == null) ? 0 : this.keyClass().hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            ConfigurationKey<?> other = (ConfigurationKey<?>) obj;
            if (this.key() == null) {
                if (other.key() != null)
                    return false;
            } else if (!this.key().equals(other.key()))
                return false;
            if (this.keyClass() == null) {
                if (other.keyClass() != null)
                    return false;
            } else if (!this.keyClass().equals(other.keyClass()))
                return false;
            return true;
        }

        /**
         * @return the name
         */
        public String getName() {
            return this.key();
        }

        /**
         * @return the type
         */
        public Class<T> getType() {
            return this.keyClass();
        }
    }
}