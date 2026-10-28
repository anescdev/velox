package es.anescdev.velox.app.loader.loaders;

import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;

import javax.inject.Inject;

import es.anescdev.velox.app.loader.LoadException;
import es.anescdev.velox.app.loader.Loader;
import es.anescdev.velox.app.loader.events.LoadEvent;
import es.anescdev.velox.core.bus.BusKey;
import es.anescdev.velox.core.providers.SettingsProvider;
import es.anescdev.velox.core.settings.SettingsKey;
import es.anescdev.velox.core.settings.SettingsStore;
import javafx.event.EventType;

public class SettingsLoader implements Loader {

    public static EventType<LoadEvent> SETTING_LOAD_EVENT = new EventType<>("SETTINGS-LOAD-EVENT");
    public static String LOAD_STRING = "load.settings.message";

    private final SettingsStore settingsStore;
    private final SettingsProvider settingsProvider;

    @Inject
    public SettingsLoader(SettingsStore settingsStore, SettingsProvider settingsProvider) {
        this.settingsStore = settingsStore;
        this.settingsProvider = settingsProvider;
    }

    @Override
    public void load() throws LoadException {
        if (!this.settingsStore.existSettingsFile()) {
            this.settingsStore.createDefaultSettings();
            return;
        }
        try {
            Properties properties = this.settingsStore.loadSettingsProperties();
            if (!this.settingsStore.isSettingsFileValid(properties))
                throw new LoadException("load.settings.error.invalidconfiguration", "");
            Iterator<SettingsKey<?>> settings = SettingsKey.getCreatedKeys();
            while (settings.hasNext()) {
                SettingsKey<Object> settingKey = (SettingsKey<Object>) settings.next();
                this.settingsProvider.set((BusKey<Object>) settingKey,
                        settingKey.getValueParser().apply(properties.getProperty(settingKey.toString())));
            }
        } catch (IOException e) {
            throw new LoadException("load.settings.error.cannotload", "");
        }
    }

    @Override
    public String getLoadingMessage() {
        return LOAD_STRING;
    }

    @Override
    public EventType<LoadEvent> eventType() {
        return SETTING_LOAD_EVENT;
    }

}