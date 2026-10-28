package es.anescdev.velox.core.settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.inject.Inject;

import es.anescdev.velox.core.CoreModule;
import es.anescdev.velox.core.providers.ConfigurationProvider;

public class SettingsStore {

    private final ConfigurationProvider configuration;

    @Inject
    public SettingsStore(ConfigurationProvider configuration) {
        this.configuration = configuration;
    }

    public void save(Properties changes) {
        try (FileOutputStream settingOutputStream = new FileOutputStream(this.getSettingsFile())) {
            changes.store(settingOutputStream, "");
        } catch (IOException e) {
            throw new CannotSaveSettingFile();
        }
    }

    public void createDefaultSettings() {
        Properties newProperties = new Properties();
        var settingsIterator = SettingsKey.getCreatedKeys();
        while (settingsIterator.hasNext()) {
            var settingKey = settingsIterator.next();
            newProperties.setProperty(settingKey.toString(), settingKey.getDefaultValue().toString());
        }
        try (FileOutputStream configFile = new FileOutputStream(this.getSettingsFile())) {
            newProperties.store(configFile, "Archivo de configuración");
        } catch (IOException e) {
            throw new CannotSaveSettingFile();
        }
    }

    public boolean existSettingsFile() {
        File settingsFile = this.getSettingsFile();
        return settingsFile.exists();
    }

    public boolean isSettingsFileValid(Properties settings) {
        var settingsIterator = SettingsKey.getCreatedKeys();
        while (settingsIterator.hasNext()) {
            var settingKey = settingsIterator.next();
            var settingRawValue = settings.getProperty(settingKey.toString());
            if (!(settingRawValue != null
                    && settingKey.getValueParser().apply(settingRawValue) != null))
                return false;
        }
        return true;
    }

    public Properties loadSettingsProperties() throws IOException {
        File settingsFile = this.getSettingsFile();
        Properties settings = new Properties();
        FileInputStream settingsInputStream = new FileInputStream(settingsFile);
        settings.load(settingsInputStream);
        settingsInputStream.close();
        return settings;
    }

    private File getSettingsFile() {
        return new File(this.configuration.get(CoreModule.APP_DIRECTORY), "settings.properties");
    }
}