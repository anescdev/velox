package es.anescdev.shared.infrastructure.providers;

import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;

import es.anescdev.shared.domain.providers.ConfigurationProvider;

/**
 * Clase de utilidad que se encarga de unificar la configuración del sistema en
 * un único lugar
 * 
 * @author AnesCDev
 */
public class InMemoryConfigurationProvider implements ConfigurationProvider {
    private final Map<ConfigurationProvider.ConfigurationKey<?>, Object> configurations;

    public InMemoryConfigurationProvider(
            Map<ConfigurationProvider.ConfigurationKey<?>, Object> initialValues) {
        this.configurations = new HashMap<>(initialValues);
    }

    public InMemoryConfigurationProvider() {
        this(new HashMap<>());
    }

    /**
     * Enum que contiene las claves existentes para la configuración. Importante
     * usar las que están disponibles y si
     * en un futuro es necesario otras nuevas, incluirlas en esta.
     * 
     * @author AnesCDev
     */

    @Override
    public <T> T get(ConfigurationProvider.ConfigurationKey<T> appDirectory)
            throws MissingResourceException {
        if (!this.configurations.containsKey(appDirectory))
            throw new MissingResourceException("The key isn't set", InMemoryConfigurationProvider.class.getName(),
                    appDirectory.getName());
        return appDirectory.getType().cast(this.configurations.get(appDirectory));
    }

    @Override
    public boolean has(ConfigurationProvider.ConfigurationKey<?> key) {
        return this.configurations.containsKey(key);
    }

    @Override
    public <T> void set(ConfigurationProvider.ConfigurationKey<T> key, T value) {
        if (key == null || value == null)
            throw new IllegalArgumentException("The key or value cannot be null");
        this.configurations.put(key, value);
    }

    @Override
    public void remove(ConfigurationProvider.ConfigurationKey<?> key) {
        this.configurations.remove(key);
    }
}
