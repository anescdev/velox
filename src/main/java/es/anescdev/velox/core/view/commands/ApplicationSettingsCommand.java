package es.anescdev.velox.core.view.commands;

import javax.inject.Inject;

import es.anescdev.velox.app.App;
import es.anescdev.velox.core.command.FeatherCommand;
import es.anescdev.velox.core.providers.SettingsProvider;
import es.anescdev.velox.core.settings.CannotSaveSettingFile;
import es.anescdev.velox.core.settings.SettingsStore;
import es.anescdev.velox.core.view.components.SettingsDialog;
import es.anescdev.velox.core.view.utils.NotificationsUtils;

public class ApplicationSettingsCommand extends FeatherCommand<Void> {

    @Inject
    private SettingsProvider settingsProvider;

    @Inject
    private SettingsStore settingsStore;

    @Override
    public Void executeCommand() {
        var settingsDialog = new SettingsDialog(settingsProvider);
        var changes = settingsDialog.showAndWait();
        if (changes.isPresent()) {
            var updateProperties = settingsProvider.toProperties();
            var changesBus = changes.get();
            changesBus.forEach((key, value) -> updateProperties.setProperty(key.toString(), value.toString()));
            try {
                this.settingsStore.save(updateProperties);
                changesBus.forEach((key, value) -> settingsProvider.set(key, value));
            } catch (CannotSaveSettingFile e) {
                NotificationsUtils.showError(App.instance().getMessage("notifications.setting.error.notsaved"), "");
            }
        }
        return null;
    }

}