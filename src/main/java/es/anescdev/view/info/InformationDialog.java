package es.anescdev.view.info;

import java.util.ResourceBundle;

import es.anescdev.App;
import javafx.scene.Scene;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.stage.Modality;

public class InformationDialog extends Dialog<Void>{

    public InformationDialog(ResourceBundle messages, Scene mainScene) {
        super();
        this.initModality(Modality.APPLICATION_MODAL);
		this.initOwner(mainScene.getWindow());
        this.setTitle(messages.getString("info.title"));
		this.setDialogPane(App.<DialogPane>loadFXML("app_info"));
		this.setResizable(false);
    }
}
