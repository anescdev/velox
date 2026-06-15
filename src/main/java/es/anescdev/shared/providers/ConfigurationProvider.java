package es.anescdev.shared.providers;

import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;

/**
 * Clase de utilidad que se encarga de unificar la configuración del sistema en
 * un único lugar
 * 
 * @author AnesCDev
 */
public class ConfigurationProvider {
    private final Map<ConfigurationKey<?>, Object> configurations;

    public ConfigurationProvider(Map<ConfigurationKey<?>, Object> initialValues) {
        this.configurations = new HashMap<>(initialValues);
    }

    public ConfigurationProvider() {
        this(new HashMap<>());
    }

    public <T> T get(ConfigurationKey<T> key) throws MissingResourceException {
        if (!this.configurations.containsKey(key))
            throw new MissingResourceException("The key isn't set", ConfigurationProvider.class.getName(), key.name);
        return key.type.cast(this.configurations.get(key));
    }

    public boolean has(ConfigurationKey<?> key) {
        return this.configurations.containsKey(key);
    }

    public <T> void set(ConfigurationKey<T> key, T value) {
        if (key == null || value == null) throw new IllegalArgumentException("The key or value cannot be null");
        this.configurations.put(key, value);
    }

    public void remove(ConfigurationKey<?> key) {
        this.configurations.remove(key);
    }

    /**
     * Enum que contiene las claves existentes para la configuración. Importante
     * usar las que están disponibles y si
     * en un futuro es necesario otras nuevas, incluirlas en esta.
     * 
     * @author AnesCDev
     */
    public static class ConfigurationKey<T> {
        /**
         * Directorio de la aplicación. Tipo String
         */
        public static final ConfigurationKey<String> APP_DIRECTORY = new ConfigurationKey<>("APP_DIR", String.class);
        public static final ConfigurationKey<String> DATABASE_NAME = new ConfigurationKey<>("DB_NAME", String.class);

        private final String name;
        private final Class<T> type;

        public ConfigurationKey(String name, Class<T> type) {
            this.name = name;
            this.type = type;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((name == null) ? 0 : name.hashCode());
            result = prime * result + ((type == null) ? 0 : type.hashCode());
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
            if (name == null) {
                if (other.name != null)
                    return false;
            } else if (!name.equals(other.name))
                return false;
            if (type == null) {
                if (other.type != null)
                    return false;
            } else if (!type.equals(other.type))
                return false;
            return true;
        }

        
    }
}
