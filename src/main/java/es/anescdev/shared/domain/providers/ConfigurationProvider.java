package es.anescdev.shared.domain.providers;

import java.util.MissingResourceException;

/**
 * Intefaz que representa a un proveedor de configuraciones
 * 
 * @author AnesCDev
 */
public interface ConfigurationProvider {
    public <T> T get(ConfigurationKey<T> key) throws MissingResourceException;

    public boolean has(ConfigurationKey<?> key);

    public <T> void set(ConfigurationKey<T> key, T value);

    public void remove(ConfigurationKey<?> key);

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

        /**
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * @return the type
         */
        public Class<T> getType() {
            return type;
        }
    }
}
