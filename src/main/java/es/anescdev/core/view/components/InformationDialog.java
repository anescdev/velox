package es.anescdev.core.view.components;

import java.util.ResourceBundle;

import es.anescdev.App;
import es.anescdev.core.view.controller.AppInfoController;
import javafx.scene.control.DialogPane;

public final class InformationDialog extends BaseDialog<Void> {
    public InformationDialog(ResourceBundle messages) {
        super(messages.getString("info.title"));
        this.setDialogPane(App.<DialogPane, AppInfoController>loadFXML("app_info").node());
    }
};