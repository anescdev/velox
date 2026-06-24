package es.anescdev.core.view.components;

import es.anescdev.App;
import es.anescdev.core.view.utils.DialogUtilities;
import javafx.scene.control.Dialog;
import javafx.stage.Modality;

public abstract class BaseDialog<R> extends Dialog<R>{
    public BaseDialog(String title) {
        super();
		this.initOwner(App.getMainStage());
        this.initModality(Modality.APPLICATION_MODAL);
        this.setTitle(title);
        this.getDialogPane().getScene().getStylesheets().addAll(this.getOwner().getScene().getStylesheets());
        DialogUtilities.centerDialog(this);
    }
}
