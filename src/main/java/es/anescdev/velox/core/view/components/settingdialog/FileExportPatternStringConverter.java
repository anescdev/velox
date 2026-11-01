package es.anescdev.velox.core.view.components.settingdialog;

import es.anescdev.velox.app.App;
import javafx.util.StringConverter;

public class FileExportPatternStringConverter extends StringConverter<String> {

    @Override
    public String toString(String object) {
        return object.split("-")[0].trim();
    }

    @Override
    public String fromString(String string) {
        return string + "-"
                + App.instance().getMessage("settings.options.sumatory.exportFilePattern.suggest." + string);
    }

}