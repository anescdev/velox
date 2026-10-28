package es.anescdev.velox.core.settings;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;

import es.anescdev.velox.core.providers.ConfigurationProvider.ConfigurationKey;

public class SettingsKey<T> extends ConfigurationKey<T> {
    // -------------------- GROUPS ----------------------------
    public static final String GENERAL_GROUP = "settings.group.general";
    public static final String SUMATORY_GROUP = "settings.group.sumatory";
    public static final String UI_GROUP = "settings.group.ui";
    // -------------------- KEYS ------------------------------
    public static final ConfigurationKey<String> SUMATORY_NAME_PATTERN = new SettingsKey<>("sumatoryFileName",
            SUMATORY_GROUP,
            String.class, "sum-%n-%d", String::new);
    public static final Pattern SUMATORY_NAME_REGEXP = Pattern.compile("^(?!.*%d.*%d)(?!.*%n.*%n)(?:[^%]|%[dn%])+$");
    public static final ObservableSettingsKey<Boolean> COMPACT_TAB_NAME = new ObservableSettingsKey<Boolean>("compactTabName", UI_GROUP,
            Boolean.class,
            false, Boolean::parseBoolean);

    private final T defaultValue;
    private final String settingsGroup;
    private static List<SettingsKey<?>> CREATED_KEYS;
    private final Function<String, T> valueParser;

    public SettingsKey(String name, String settingsGroup, Class<T> type, T defaultValue,
            Function<String, T> valueParser) {
        super(name, type);
        this.settingsGroup = settingsGroup;
        this.defaultValue = defaultValue;
        this.valueParser = rawValue -> {
            try {
                return valueParser.apply(rawValue);
            } catch (Exception e) {
                return null;
            }
        };
        if (CREATED_KEYS == null)
            CREATED_KEYS = new ArrayList<>();
        CREATED_KEYS.add(this);
    }

    public SettingsKey(String name, Class<T> type, T defaultValue, Function<String, T> valueParser) {
        this(name, GENERAL_GROUP, type, defaultValue, valueParser);
    }

    public SettingsKey(String name, String settingsGroup, Class<T> type, Function<String, T> valueParser) {
        this(name, settingsGroup,
                type, null, valueParser);
    }

    public SettingsKey(String name, Class<T> type, Function<String, T> valueParser) {
        this(name, type, null, valueParser);
    }

    public static Iterator<SettingsKey<?>> getCreatedKeys() {
        return CREATED_KEYS.iterator();
    }

    public T getDefaultValue() {
        return this.defaultValue;
    }

    public String getSettingsGroup() {
        return this.settingsGroup;
    }

    public String toString() {
        return this.settingsGroup + "." + this.getName();
    }

    public Function<String, T> getValueParser() {
        return valueParser;
    }

}