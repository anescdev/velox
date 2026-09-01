package es.anescdev.velox.core.view.components;

import java.util.ResourceBundle;

import es.anescdev.velox.app.App;
import es.anescdev.velox.core.view.controller.AppInfoController;
import javafx.scene.layout.BorderPane;

public final class InformationDialog extends BaseDialog<Void> {
    public InformationDialog(ResourceBundle messages) {
        super(messages.getString("info.title"));
        this.setContent(App.<BorderPane, AppInfoController>loadFXML("app_info").node());
    }
};