package es.anescdev.velox.core.settings;

import java.util.function.Function;

public class ObservableSettingsKey<T> extends SettingsKey<T> {

    public ObservableSettingsKey(String name, String settingsGroup, Class<T> type, T defaultValue,
            Function<String, T> valueParser) {
        super(name, settingsGroup, type, defaultValue, valueParser);
    }

    public ObservableSettingsKey(String name, Class<T> type, T defaultValue, Function<String, T> valueParser) {
        super(name, type, defaultValue, valueParser);
    }

    public ObservableSettingsKey(String name, String settingsGroup, Class<T> type, Function<String, T> valueParser) {
        super(name, settingsGroup, type, valueParser);
    }

    public ObservableSettingsKey(String name, Class<T> type, Function<String, T> valueParser) {
        super(name, type, valueParser);
    }

}