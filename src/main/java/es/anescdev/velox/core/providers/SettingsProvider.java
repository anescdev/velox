package es.anescdev.velox.core.providers;

import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Properties;

import javax.inject.Singleton;

import es.anescdev.velox.core.bus.BusKey;
import es.anescdev.velox.core.settings.ObservableSettingsKey;
import es.anescdev.velox.core.settings.SettingsKey;

import javafx.beans.property.SimpleObjectProperty;

/**
 * @author AnesCDev
 */
@Singleton
public class SettingsProvider extends ConfigurationProvider {
    public final Map<ConfigurationKey<?>, String> groups = new HashMap<>();
    public final Map<ObservableSettingsKey<?>, SimpleObjectProperty<?>> observableSettings;

    /**
     * @param observableSettings
     */
    public SettingsProvider() {
        this.observableSettings = new HashMap<>();
    }

    @Override
    public <T> T get(ConfigurationKey<T> key) throws MissingResourceException {
        try {
            return super.get(key);
        } catch (MissingResourceException e) {
            if (key instanceof SettingsKey<T> settingKey) {
                var defaultValue = settingKey.getDefaultValue();
                if (defaultValue != null)
                    return defaultValue;
            }
            throw e;

        }
    }

    public <T> SimpleObjectProperty<T> get(ObservableSettingsKey<T> key) {
        var observableValue = (SimpleObjectProperty<T>) this.observableSettings.get(key);
        if (observableValue == null) {
            SimpleObjectProperty<T> newObservableValue = new SimpleObjectProperty<T>(key.getDefaultValue());
            this.observableSettings.put(key, newObservableValue);
            return newObservableValue;
        }
        return observableValue;
    }

    public <T> void set(BusKey<T> key, T value) {
        if (key instanceof ObservableSettingsKey<T> observableSettingsKey) {
            this.set(observableSettingsKey, value);
        } else if (key instanceof SettingsKey<T> settingsKey) {
            this.set(settingsKey, value);
        } else if (key instanceof ConfigurationKey<T> configurationKey) {
            this.set(configurationKey, value);
        }
    }

    @Override
    public <T> void set(ConfigurationKey<T> key, T value) {
        super.set(key, value);
        this.groups.put(key, SettingsKey.GENERAL_GROUP);
    }

    public <T> void set(SettingsKey<T> key, T value) {
        super.set(key, value);
        this.groups.put(key, key.getSettingsGroup());
    }

    public <T> void set(ObservableSettingsKey<T> key, T value) {
        SimpleObjectProperty<T> objectProperty = (SimpleObjectProperty<T>) this.observableSettings.get(key);
        if (objectProperty == null) {
            this.observableSettings.put(key, new SimpleObjectProperty<T>(value));
            this.groups.put(key, key.getSettingsGroup());
        } else
            objectProperty.set(value);
    }

    @Override
    public void remove(ConfigurationKey<?> key) {
        super.remove(key);
        this.groups.remove(key);
    }

    public Properties toProperties() {
        Properties settingsProperties = new Properties();
        this.configurations.forEach((key, val) -> {
            if (key instanceof SettingsKey settingKey)
                settingsProperties.setProperty(settingKey.toString(), val.toString());
            else if ((key instanceof ObservableSettingsKey observableSettingsKey)
                    && (val instanceof SimpleObjectProperty observableVal))
                settingsProperties.setProperty(observableSettingsKey.toString(), observableVal.getValue().toString());
        });
        return settingsProperties;
    }

}
